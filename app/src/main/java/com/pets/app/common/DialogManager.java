package com.pets.app.common;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;

import com.pets.app.R;

/**
 * Created by BAJIRAO on 05 January 2018.
 */
public class DialogManager {

    public static void showDialogWithOkAndCancel(Context context, String message, DialogInterface.OnClickListener onClickListener) {
        if (context instanceof Activity && !checkIfActivityIsRunning((Activity) context)) {
            return;
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(MyApplication.getInstance().getString(R.string.app_name))
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(MyApplication.getInstance().getString(R.string.ok), onClickListener)
                .setNegativeButton(MyApplication.getInstance().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    public static void showDialogWithYesAndNo(Context context, String message, DialogInterface.OnClickListener onClickListener) {
        if (context instanceof Activity && !checkIfActivityIsRunning((Activity) context)) {
            return;
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(MyApplication.getInstance().getString(R.string.app_name))
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(MyApplication.getInstance().getString(R.string.yes), onClickListener)
                .setNegativeButton(MyApplication.getInstance().getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    public static void showDialogWithOkButton(Context context, String message) {
        if (context instanceof Activity && !checkIfActivityIsRunning((Activity) context)) {
            return;
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(MyApplication.getInstance().getString(R.string.app_name))
                .setMessage(message)
                .setCancelable(false)
                .setNeutralButton(MyApplication.getInstance().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    private static boolean checkIfActivityIsRunning(Activity activity) {
        if (activity.isFinishing()) {
            return false;
        }
        return !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed());
    }
}
