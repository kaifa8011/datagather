package com.ciba.data.gather.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import com.ciba.data.gather.common.DataGatherManager;

/**
 * @author parting_soul
 * @date 2020-06-09
 */
public class PermissionUtils {
    /**
     * @param context
     * @param isOr
     * @param permissions
     * @return
     */
    public static boolean hasPermissions(Context context, boolean isOr, String... permissions) {
        if (permissions == null) {
            return false;
        }
        boolean hasPermission = !isOr;
        for (int i = 0; i < permissions.length; i++) {
            if (isOr) {
                hasPermission |= hasPermission(context, permissions[i]);
            } else {
                hasPermission &= hasPermission(context, permissions[i]);
            }
        }
        return hasPermission;
    }

    public static boolean hasPermission(Context context, String permssion) {
        return ContextCompat.checkSelfPermission(context, permssion) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean hasExternalPermission() {
        boolean hasPermission;
        String[] permissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE};
        } else {
            permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }
        hasPermission = PermissionUtils.hasPermissions(DataGatherManager.getInstance().getContext(),
                true, permissions);
        return hasPermission;
    }
}
