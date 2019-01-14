package com.ciba.datagather.handler;

import com.ciba.datagather.util.device.PackageUtil;
import com.ciba.datasynchronize.manager.DataCacheManager;

import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;


/**
 * <UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告>
 */
public class CrashHandler implements UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    public void init() {
        // 获取系统默认的 UncaughtException 处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该 CrashHandler 为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当 UncaughtException 发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            //出现异常直接退出程序
        }
    }

    /**
     * <自定义错误处理，收集错误信息，发送错误报告等操作均在此完成>
     */
    private boolean handleException(Throwable ex) {
        saveCrashInfo2File(ex);
        return false;
    }


    /**
     * <保存错误信息到文件中>
     */
    private void saveCrashInfo2File(Throwable throwable) {
        if (throwable == null) {
            return;
        }
        Writer writer = null;
        PrintWriter printWriter = null;
        try {
            writer = new StringWriter();
            printWriter = new PrintWriter(writer);
            throwable.printStackTrace(printWriter);
            Throwable cause = throwable.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("version", PackageUtil.getVersionName());
            jsonObject.put("data", writer.toString());
            DataCacheManager.getInstance().saveCrashData(jsonObject.toString());
            jsonObject = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                    writer = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (printWriter != null) {
                printWriter.close();
                printWriter = null;
            }
        }
    }
}