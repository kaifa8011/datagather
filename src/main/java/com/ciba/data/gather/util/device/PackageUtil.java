package com.ciba.data.gather.util.device;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.ciba.data.gather.common.DataGatherManager;
import com.ciba.data.gather.util.DataGatherLog;
import com.ciba.data.gather.util.FileUtils;
import com.ciba.data.synchronize.entity.CustomPackageInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ciba
 * @description 与安装包有关的工具类
 * @date 2018/12/3
 */
public class PackageUtil {

    /**
     * 获取包名
     */
    public static String getPackageName() {
        return DataGatherManager.getInstance().getContext().getPackageName();
    }

    /**
     * 获取版本名称
     */
    public static String getVersionName() {
        try {
            PackageInfo packageInfo = getPackageInfo();
            if (packageInfo != null) {
                return packageInfo.versionName;
            }
        } catch (Exception e) {
            DataGatherLog.innerI(e.getMessage());
        }
        return "";
    }

    /**
     * 获取版本号
     */
    public static int getVersionCode() {
        try {
            PackageInfo packageInfo = getPackageInfo();
            if (packageInfo != null) {
                return packageInfo.versionCode;
            }
        } catch (Exception e) {
            DataGatherLog.innerI(e.getMessage());
        }
        return 0;
    }

    /**
     * 获取包信息
     */
    public static PackageInfo getPackageInfo() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (Exception e) {
            DataGatherLog.innerI(e.getMessage());
            return null;
        }
    }

//    /**
//     * 获取手机中安装的应用列表
//     * <p>
//     * packageInfo.applicationInfo.loadLabel() 一个APP大致需要10ms左右时间，当安装量比较大时比较耗时，建议在子线程获取
//     *
//     * @param withOutSystem ：是否排除系统应用
//     */
//    public static List<CustomPackageInfo> getInstallPackageList(boolean withOutSystem) {
//        List<CustomPackageInfo> customPackageInfoList = new ArrayList<>();
//        try {
//            PackageManager packageManager = getPackageManager();
//            List<PackageInfo> installedList = packageManager.getInstalledPackages(0);
//            if (installedList == null || installedList.isEmpty()) {
//                return customPackageInfoList;
//            }
//            for (int i = 0; i < installedList.size(); i++) {
//                PackageInfo packageInfo = installedList.get(i);
//                if (withOutSystem) {
//                    if (packageInfo == null || (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
//                        continue;
//                    }
//                }
//                CustomPackageInfo customPackageInfo = new CustomPackageInfo();
//                customPackageInfo.setPackageName(packageInfo.packageName);
//                customPackageInfo.setVersionName(packageInfo.versionName);
//                customPackageInfo.setVersionNo(packageInfo.versionCode + "");
//
//                // 一个APP大致需要10ms左右时间，当安装量比较大时比较耗时，建议在子线程获取
//                String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
//                customPackageInfo.setApplyName(appName);
//
//                customPackageInfoList.add(customPackageInfo);
//            }
//            installedList.clear();
//        } catch (Exception e) {
//            DataGatherLog.innerI(e.getMessage());
//        }
//        return customPackageInfoList;
//    }


    /**
     * 获取手机中安装的应用列表
     * <p>
     * packageInfo.applicationInfo.loadLabel() 一个APP大致需要10ms左右时间，当安装量比较大时比较耗时，建议在子线程获取
     *
     * @param withOutSystem ：是否排除系统应用
     */
    public static List<CustomPackageInfo> getInstallPackageList(boolean withOutSystem) {
        List<CustomPackageInfo> customPackageInfoList = new ArrayList<>();

        //如果是小米系统并且MIUI版本大于10，则不读取列表
        boolean isMIUI = XiaoMiDeviceUtil.checkMIUI();
        if (isMIUI) {
            boolean miuiVersionNameLtV10 = XiaoMiDeviceUtil.checkMIUIVersionNameLtV10();
            if (!miuiVersionNameLtV10) {
                //miui大于10 不读取列表
                return customPackageInfoList;
            }
        }

        try {
            PackageManager packageManager = getPackageManager();
            List<String> packageList = runCommandPackageList();
            if (packageList == null || packageList.isEmpty()) {
                return customPackageInfoList;
            }
            for (int i = 0; i < packageList.size(); i++) {
                PackageInfo packageInfo = packageManager.getPackageInfo(packageList.get(i), 0);
                if (packageInfo == null) {
                    continue;
                }
                CustomPackageInfo customPackageInfo = new CustomPackageInfo();
                customPackageInfo.setPackageName(packageInfo.packageName);
                customPackageInfo.setVersionName(packageInfo.versionName);
                customPackageInfo.setVersionNo(packageInfo.versionCode + "");

                // 一个APP大致需要10ms左右时间，当安装量比较大时比较耗时，建议在子线程获取
                String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
                customPackageInfo.setApplyName(appName);

                customPackageInfoList.add(customPackageInfo);
            }
            packageList.clear();
        } catch (Exception e) {
            DataGatherLog.innerI(e.getMessage());
        }
        return customPackageInfoList;
    }

    private static List<String> runCommandPackageList() {
        BufferedReader bis = null;
        InputStreamReader isr = null;
        List<String> packageList = new ArrayList<>();
        try {
            Process process = Runtime.getRuntime().exec("pm list package -3");
            isr = new InputStreamReader(process.getInputStream());
            bis = new BufferedReader(isr);
            String line;
            while ((line = bis.readLine()) != null) {
                packageList.add(line.replace("package:", ""));
            }
        } catch (Exception e) {
        } finally {
            FileUtils.close(bis, isr);
        }
        return packageList;
    }

    /**
     * 获取包管理器
     */
    public static PackageManager getPackageManager() {
        return DataGatherManager.getInstance().getContext().getPackageManager();
    }
}
