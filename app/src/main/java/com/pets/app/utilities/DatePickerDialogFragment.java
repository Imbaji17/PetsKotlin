package com.pets.app.utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by BAJIRAO on 02 December 2017.
 */
@SuppressLint("ValidFragment")
public class DatePickerDialogFragment extends DialogFragment {

    private final Activity mActivity;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @SuppressLint("ValidFragment")
    public DatePickerDialogFragment(Activity mActivity, DatePickerDialog.OnDateSetListener callback) {
        // nothing to see here, move along
        this.mActivity = mActivity;
        mDateSetListener = callback;
    }

    @SuppressLint("ValidFragment")
    public DatePickerDialogFragment(Activity mActivity, DatePickerDialog.OnDateSetListener callback, Date date) {
        // nothing to see here, move along
        this.mActivity = mActivity;
        mDateSetListener = callback;
    }


    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
//        c.set(c.get(Calendar.YEAR) - 16, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH) - 1);
        DatePickerDialog dialog = new DatePickerDialog(mActivity, mDateSetListener, year, month, day);
//        dialog.getDatePicker().setMaxDate(c.getTimeInMillis());

        return dialog;
    }

}
