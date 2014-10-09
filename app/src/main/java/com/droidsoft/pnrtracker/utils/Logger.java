package com.droidsoft.pnrtracker.utils;

import android.util.Log;

/**
 * Created by mitesh on 14. 10. 9.
 */
public class Logger {

    public static final String TAG = "PNRVIEW";

    public static void LogD(String msg) {
        Log.d(TAG, msg);
    }

    public static void LogV(String msg) {
        Log.v(TAG, msg);
    }

    public static void LogE(String msg) {
        Log.e(TAG, msg);
    }
}
