package com.ciba.data.gather.util.device;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.text.TextUtils;

import com.ciba.data.gather.common.DataGatherManager;
import com.ciba.data.gather.process.AndroidProcesses;
import com.ciba.data.gather.process.models.AndroidAppProcess;
import com.ciba.data.gather.util.DataGatherLog;
import com.ciba.data.synchronize.entity.ProcessData;
import com.ciba.data.synchronize.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ciba
 * @description 进程工具类
 * @date 2018/12/3
 */
public class ProcessUtil {

    /**
     * 获取当前运行中应用进程集合 Android系统目前已经无法读取进行，直接返回空集合
     *
     * @param withoutSystem ：是否排除系统应用
     * @param appOnly       ：是否一个App只拿一个进程
     */
    public static List<ProcessData> getAppProcessList(boolean withoutSystem, boolean appOnly) {
        List<ProcessData> processList = new ArrayList<>();
        return processList;
//        try {
//            List<AndroidAppProcess> processes = AndroidProcesses.getRunningAppProcesses();
//            if (processes == null || processes.isEmpty()) {
//                return processList;
//            }
//            for (int index = 0; index < processes.size(); index++) {
//                AndroidAppProcess process = processes.get(index);
//                if (process == null || TextUtils.isEmpty(process.getPackageName())) {
//                    continue;
//                }
//                String packageName = process.getPackageName();
//
//                if (withoutSystem) {
//                    PackageInfo packageInfo = process.getPackageInfo(DataGatherManager.getInstance().getContext(), 0);
//                    // 移除系统应用进程信息
//                    if (packageInfo == null
//                            || packageInfo.applicationInfo == null
//                            || (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
//                        continue;
//                    }
//                }
//
//                if (appOnly) {
//                    // 移除相同包名的进程信息（一个APP可能有多个进程）
//                    boolean hasThisProcess = false;
//                    int size = processList.size();
//                    for (int i = 0; i < size; i++) {
//                        ProcessData appProcess = processList.get(i);
//                        if (packageName.equals(appProcess.getPackageName())) {
//                            hasThisProcess = true;
//                            break;
//                        }
//                    }
//                    if (hasThisProcess) {
//                        continue;
//                    }
//                }
//                processList.add(new ProcessData(packageName, TimeUtil.getCurrentTime(), process.name
//                        , process.foreground, process.uid, process.pid));
//            }
//            processes.clear();
//        } catch (Exception e) {
//            DataGatherLog.innerI(e.getMessage());
//        }
//        return processList;
    }

    /**
     * 当前进程是否是主进程
     */
    public static boolean isMainProcess() {
        return TextUtils.equals(PackageUtil.getPackageName(), getCurrentProcessName());
    }

    /**
     * 获取当前进程名
     */
    public static String getCurrentProcessName() {
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager manager = (ActivityManager) DataGatherManager.getInstance().getContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = manager.getRunningAppProcesses();
        if (runningAppProcesses != null && runningAppProcesses.size() > 0) {
            for (int i = 0; i < runningAppProcesses.size(); i++) {
                ActivityManager.RunningAppProcessInfo process = runningAppProcesses.get(i);
                if (process.pid == pid) {
                    processName = process.processName;
                }
            }
        }
        return processName;
    }
}
