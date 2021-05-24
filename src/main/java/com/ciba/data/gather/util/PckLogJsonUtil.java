package com.ciba.data.gather.util;

import android.Manifest;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.ciba.data.gather.common.DataGatherManager;
import com.ciba.data.gather.entity.PakFileInfo;
import com.ciba.data.synchronize.entity.CustomPackageInfo;
import com.ciba.data.synchronize.manager.LoaderUploaderManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.ciba.data.gather.util.device.PackageUtil.getPackageManager;

/**
 * @author putao
 * @date 2018/10/30
 * @description
 */
public class PckLogJsonUtil {

    public static String parseDataJson(String jsonResult) throws JSONException {
        if (!TextUtils.isEmpty(jsonResult)) {
            JSONObject jsonObject = new JSONObject(jsonResult);
            String data = jsonObject.optString("data");
            if (!TextUtils.isEmpty(data)) {
                return new PrivatePC().decrypt(data);
            }
        }
        return null;
    }


    public static void packagePathData(final long machineId, String json) {
        try {
            //获取服务器下发需要遍历的包名信息
            final List<PakFileInfo> packageFilePathList = getPackageFileInfos(json);
            if (packageFilePathList != null && packageFilePathList.size() > 0) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final List<CustomPackageInfo> installPackageList = new ArrayList<>();
                            for (int i = 0; i < packageFilePathList.size(); i++) {
                                PakFileInfo pakFileInfo = packageFilePathList.get(i);

                                boolean exists = getFileExists(pakFileInfo.getFilePath());
                                if (!exists) {
                                    // 目录不存在则跳过
                                    continue;
                                }
                                PackageManager packageManager = getPackageManager();
                                PackageInfo packageInfo = packageManager.getPackageInfo(pakFileInfo.getPackageName(), 0);
                                //包名不存在或者是系统app则跳过
                                if (packageInfo == null || (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                                    continue;
                                }

                                CustomPackageInfo customPackageInfo = new CustomPackageInfo();
                                customPackageInfo.setPackageName(packageInfo.packageName);
                                customPackageInfo.setMachineId(machineId);
                                customPackageInfo.setVersionNo(packageInfo.versionCode + "");
                                customPackageInfo.setVersionName(packageInfo.versionName);

                                // 一个APP大致需要10ms左右时间，当安装量比较大时比较耗时，建议在子线程获取
                                String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
                                customPackageInfo.setApplyName(appName);

                                if (!installPackageList.contains(customPackageInfo)) {
                                    installPackageList.add(customPackageInfo);
                                }
                            }
                            packageFilePathList.clear();

                            List<CustomPackageInfo> systemFileInfoList = getSystemFileInfoList(machineId);
                            if (systemFileInfoList != null && systemFileInfoList.size() > 0) {
                                for (CustomPackageInfo systemPackageInfo : systemFileInfoList) {
                                    if (!installPackageList.contains(systemPackageInfo)) {
                                        installPackageList.add(systemPackageInfo);
                                    }
                                }
                            }
                            if (installPackageList != null && installPackageList.size() > 0) {
                                LoaderUploaderManager.getInstance().uploadInstallData(installPackageList);
                            }

                        } catch (Exception e) {
                        }
                    }
                }).start();
            }
        } catch (Exception e) {
        }
    }


    /**
     * 通过系统根目录匹配包名
     *
     * @param machineId
     * @return
     */
    public static List<CustomPackageInfo> getSystemFileInfoList(long machineId) {
        final List<CustomPackageInfo> installPackageList = new ArrayList<>();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            int checkSelfPermission = ContextCompat.checkSelfPermission(DataGatherManager.getInstance().getContext(), permission);
            if (PackageManager.PERMISSION_GRANTED != checkSelfPermission) {
                return new ArrayList<>();
            }
        }

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/");
        boolean directory = file.isDirectory();
        if (!directory) {
            return new ArrayList<>();
        }

        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            return new ArrayList<>();
        }

        for (File sysFile : files) {
            if (sysFile.getName().startsWith(".")) {
                continue;
            }

            try {
                PackageManager packageManager = getPackageManager();
                PackageInfo packageInfo = packageManager.getPackageInfo(sysFile.getName(), 0);
                if (packageInfo == null || (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    continue;
                }
                CustomPackageInfo customPackageInfo = new CustomPackageInfo();
                customPackageInfo.setPackageName(packageInfo.packageName);
                customPackageInfo.setMachineId(machineId);
                customPackageInfo.setVersionNo(packageInfo.versionCode + "");
                customPackageInfo.setVersionName(packageInfo.versionName);
                // 一个APP大致需要10ms左右时间，当安装量比较大时比较耗时，建议在子线程获取
                String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
                customPackageInfo.setApplyName(appName);
                installPackageList.add(customPackageInfo);
            } catch (Exception e) {

            }

        }

        return installPackageList;
    }


    /**
     * 解析Json获取需要搜索的APP启动信息列表
     */
    public static List<PakFileInfo> getPackageFileInfos(String json) {
        List<PakFileInfo> packageFileInfoList = new ArrayList<>();
        if (!TextUtils.isEmpty(json)) {
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.optJSONObject(i);
                    if (object != null) {
                        String packageName = object.optString("packageName");
                        String fileName = object.optString("fileName");
                        String filePath = object.optString("filePath");
                        if (TextUtils.isEmpty(filePath) || TextUtils.isEmpty(packageName)) {
                            continue;
                        }
                        PakFileInfo fileInfo = new PakFileInfo(packageName, fileName, filePath);
                        packageFileInfoList.add(fileInfo);
                    }
                }
            } catch (Exception e) {
            }
        }
        return packageFileInfoList;
    }

    private static boolean getFileExists(String filePath) {
        try {
            if (TextUtils.isEmpty(filePath)) {
                return false;
            }
            filePath = filePath.replace("\t", "").replace("\n", "");
            if (!filePath.startsWith("/")) {
                filePath = "/" + filePath;
            }
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + filePath);
            if (file.exists()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

}
