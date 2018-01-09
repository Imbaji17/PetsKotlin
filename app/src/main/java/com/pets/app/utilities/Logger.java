package com.pets.app.utilities;

import android.util.Log;

import com.pets.app.common.Constants;


/**
 * class for view log.
 */
public class Logger {

    /**
     * method for print log.
     */
    public static void logger(String message) {
        if (Constants.isInDebugMode) {
            Log.i("karyab", message == null ? "" : message);
        }
    }

    public static void logger(String TAG, String message) {
        if (Constants.isInDebugMode) {
            Log.i(TAG, message == null ? "" : message);
        }
    }

    public static void errorLog(String message) {
        if (Constants.isInDebugMode) {
            Log.e("karyab", message == null ? "" : message);
        }
    }
}