package com.nsnv.refreshlinearly;

import android.util.Log;

/**
 */
public class MLog {

    private static boolean enable = true;

    public static void e(Object obj, String msg) {
        if (enable) {
            Log.e(obj.getClass().getSimpleName(), msg);
        }
    }
}
