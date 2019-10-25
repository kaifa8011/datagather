package com.ciba.data.gather.util;

import android.util.Log;

import com.ciba.datagather.BuildConfig;

/**
 * @author parting_soul
 * @date 17-12-31
 */
public class LogUtils {
    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int NONE = 6;

    private static int currentLevel = BuildConfig.DEBUG ? VERBOSE : ERROR;

    public static void v(String tag, String msg) {
        if (currentLevel <= VERBOSE) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (currentLevel <= DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (currentLevel <= INFO) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (currentLevel <= WARN) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (currentLevel <= ERROR) {
            Log.e(tag, msg);
        }
    }

    /*****以下方法默认的TAG为调用类的类名****/

    public static void v(String msg) {
        if (currentLevel <= VERBOSE) {
            StackTraceElement st = new Exception().getStackTrace()[1];
            String fileName = st.getFileName();
            int lineNum = st.getLineNumber();
            String className = st.getClassName().substring(st.getClassName().lastIndexOf('.') + 1);
            String methodName = st.getMethodName();
            Log.v(className, "┌─────────────────────────────────────────────────────────────────────────────");
            Log.v(className, "│ Thread:" + Thread.currentThread().getName() + " - " + className + "." + methodName + " (" + fileName + ":" + lineNum + ")");
            Log.v(className, "├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄");
            Log.v(className, "│ " + msg);
            Log.v(className, "└─────────────────────────────────────────────────────────────────────────────");
        }
    }

    public static void d(String msg) {
        if (currentLevel <= DEBUG) {
            StackTraceElement st = new Exception().getStackTrace()[1];
            String fileName = st.getFileName();
            int lineNum = st.getLineNumber();
            String className = st.getClassName().substring(st.getClassName().lastIndexOf('.') + 1);
            String methodName = st.getMethodName();
            Log.d(className, "┌─────────────────────────────────────────────────────────────────────────────");
            Log.d(className, "│ Thread:" + Thread.currentThread().getName() + " - " + className + "." + methodName + " (" + fileName + ":" + lineNum + ")");
            Log.d(className, "├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄");
            Log.d(className, "│ " + msg);
            Log.d(className, "└─────────────────────────────────────────────────────────────────────────────");
        }
    }

    public static void i(String msg) {
        if (currentLevel <= INFO) {
            StackTraceElement st = new Exception().getStackTrace()[1];
            String fileName = st.getFileName();
            int lineNum = st.getLineNumber();
            String className = st.getClassName().substring(st.getClassName().lastIndexOf('.') + 1);
            String methodName = st.getMethodName();
            Log.i(className, "┌─────────────────────────────────────────────────────────────────────────────");
            Log.i(className, "│ Thread:" + Thread.currentThread().getName() + " - " + className + "." + methodName + " (" + fileName + ":" + lineNum + ")");
            Log.i(className, "├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄");
            Log.i(className, "│ " + msg);
            Log.i(className, "└─────────────────────────────────────────────────────────────────────────────");
        }
    }

    public static void w(String msg) {
        if (currentLevel <= WARN) {
            StackTraceElement st = new Exception().getStackTrace()[1];
            String fileName = st.getFileName();
            int lineNum = st.getLineNumber();
            String className = st.getClassName().substring(st.getClassName().lastIndexOf('.') + 1);
            String methodName = st.getMethodName();
            Log.w(className, "┌─────────────────────────────────────────────────────────────────────────────");
            Log.w(className, "│ Thread:" + Thread.currentThread().getName() + " - " + className + "." + methodName + " (" + fileName + ":" + lineNum + ")");
            Log.w(className, "├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄");
            Log.w(className, "│ " + msg);
            Log.w(className, "└─────────────────────────────────────────────────────────────────────────────");
        }
    }

    public static void e(String msg) {
        if (currentLevel <= ERROR) {
            StackTraceElement st = new Exception().getStackTrace()[1];
            String fileName = st.getFileName();
            int lineNum = st.getLineNumber();
            String className = st.getClassName().substring(st.getClassName().lastIndexOf('.') + 1);
            String methodName = st.getMethodName();
            Log.e(className, "┌─────────────────────────────────────────────────────────────────────────────");
            Log.e(className, "│ Thread:" + Thread.currentThread().getName() + " - " + className + "." + methodName + " (" + fileName + ":" + lineNum + ")");
            Log.e(className, "├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄");
            Log.e(className, "│ " + msg);
            Log.e(className, "└─────────────────────────────────────────────────────────────────────────────");
        }
    }

}
