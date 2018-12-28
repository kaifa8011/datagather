package com.ciba.datagather.util.device;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.ciba.datagather.common.DataGatherManager;
import com.ciba.datasynchronize.entity.CustomPackageInfo;

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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取手机中安装的应用列表
     *
     * packageInfo.applicationInfo.loadLabel() 一个APP大致需要10ms左右时间，当安装量比较大时比较耗时，建议在子线程获取
     *
     * @param withOutSystem ：是否排除系统应用
     */
    public static List<CustomPackageInfo> getInstallPackageList(boolean withOutSystem) {
        List<CustomPackageInfo> customPackageInfoList = new ArrayList<>();
        try {
            PackageManager packageManager = getPackageManager();
            List<PackageInfo> installedList = packageManager.getInstalledPackages(0);
            if (installedList == null || installedList.isEmpty()) {
                return customPackageInfoList;
            }
            for (int i = 0; i < installedList.size(); i++) {
                PackageInfo packageInfo = installedList.get(i);
                if (withOutSystem) {
                    if (packageInfo == null || (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                        continue;
                    }
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
            installedList.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customPackageInfoList;
    }

    /**
     * 获取包管理器
     */
    public static PackageManager getPackageManager() {
        return DataGatherManager.getInstance().getContext().getPackageManager();
    }
}
