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
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(mActivity, mDateSetListener, year, month, day);
        return dialog;
    }

}
