package com.system.management.attendance.wams;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Created by koichi on 2017/11/27.
 */

public class Utils {
    public static boolean hasSelfPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkGrantResults(int... grantResults) {
        if (grantResults.length == 0) throw new IllegalArgumentException("grantResults is empty");
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
