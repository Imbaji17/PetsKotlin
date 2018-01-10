package com.pets.app.mediator;

import android.text.Editable;

/**
 * Created by BAJIRAO on 05 January 2018.
 */
public abstract class AppTextWatcher implements android.text.TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
