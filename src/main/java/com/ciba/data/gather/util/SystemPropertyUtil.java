package com.ciba.data.gather.util;

/**
 * @author songzi
 * @date 2021/8/13
 */

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Android 系统属性读取工具类
 * @author oumatsumatsu
 */
public class SystemPropertyUtil {

    private static String VERSION_CODE = "";

    static {
        try {
            boolean checkUI = checkMIUI();
            if (checkUI) {
                VERSION_CODE = getMIUIVersion();
            } else {
                checkUI = checkEMUI();
                if (checkUI) {
                    VERSION_CODE = getEMUIVersion();
                } else {
                    checkUI = checkColorOS();
                    if (checkUI) {
                        VERSION_CODE = getColorOSVersion();
                    } else {
                        checkUI = checkFuntouchOS();
                        if (checkUI) {
                            VERSION_CODE = getFuntouchOSVersion();
                        } else {
                            checkUI = checkH2OS();
                            if (checkUI) {
                                VERSION_CODE = getH2OSVersion();
                            } else {
                                checkUI = checkFlyme();
                                if (checkUI) {
                                    VERSION_CODE = getFlymeVersion();
                                } else {
                                    checkUI = checkSamsung();
                                    if (checkUI) {
                                        VERSION_CODE = getSamsungVersion();
                                    } else {
                                        checkUI = checkSmartisanOS();
                                        if (checkUI) {
                                            VERSION_CODE = getSmartisanOSVersion();
                                        } else {
                                            VERSION_CODE = getAndroidVersion();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (TextUtils.isEmpty(VERSION_CODE)) {
                VERSION_CODE = "";
            }
        } catch (Exception e) {
            VERSION_CODE = "";
        }
    }

    /**
     * 使用命令方式读取系统属性
     *
     * @param propName
     * @return
     */
    private static String getSystemProperty(String propName) {
        String line = "";
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
        return line;
    }


    private static boolean checkMIUI() {
        try {
            String rom = getSystemProperty("ro.miui.ui.version.name");
            if (!TextUtils.isEmpty(rom)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private static String getMIUIVersion() {
        return getSystemProperty("ro.build.version.incremental");
    }

    private static boolean checkEMUI() {
        String rom = getSystemProperty("ro.build.version.emui");

        if (TextUtils.isEmpty(rom)) {
            return false;
        }

        if (rom.startsWith("EmotionUI")) {
            return true;
        }
        return false;
    }

    private static String getEMUIVersion() {
        return getSystemProperty("ro.build.version.incremental");
    }


    private static boolean checkFlyme() {
        String rom = getSystemProperty("ro.build.display.id");

        if (TextUtils.isEmpty(rom)) {
            return false;
        }

        if (rom.startsWith("Flyme")) {
            return true;
        }

        return false;
    }

    private static String getFlymeVersion() {
        return getSystemProperty("ro.build.display.id");
    }

    private static boolean checkColorOS() {
        String rom = getSystemProperty("ro.build.version.opporom");

        if (!TextUtils.isEmpty(rom)) {
            return true;
        }
        return false;
    }

    private static String getColorOSVersion() {
        return getSystemProperty("ro.build.version.opporom");
    }


    private static boolean checkFuntouchOS() {
        String rom = getSystemProperty("ro.vivo.os.name");

        if (TextUtils.isEmpty(rom)) {
            return false;
        }

        if (rom.startsWith("Funtouch")) {
            return true;
        }

        return false;
    }

    private static String getFuntouchOSVersion() {
        return getSystemProperty("ro.vivo.product.version");
    }

    private static boolean checkSamsung() {
        String rom = getSystemProperty("ro.build.user");

        if (TextUtils.isEmpty(rom)) {
            return false;
        }

        if ("dpi".equalsIgnoreCase(rom)) {
            return true;
        }
        return false;
    }

    private static String getSamsungVersion() {
        return getSystemProperty("ro.build.display.id");
    }

    private static boolean checkSmartisanOS() {
        String rom = getSystemProperty("ro.smartisan.version");
        if (!TextUtils.isEmpty(rom)) {
            return true;
        }
        return false;
    }

    private static String getSmartisanOSVersion() {
        return getSystemProperty("ro.build.version.incremental");
    }

    private static boolean checkH2OS() {
        String rom = getSystemProperty("ro.build.product");

        if (TextUtils.isEmpty(rom)) {
            return false;
        }

        if (rom.toLowerCase().contains("oneplus")) {
            return true;
        }
        return false;
    }

    private static String getH2OSVersion() {
        return getSystemProperty("ro.build.display.id");
    }

    private static String getAndroidVersion() {
        return getSystemProperty("ro.build.display.id");
    }

    public static String getRomVersionCode() {
        return VERSION_CODE;
    }
}

