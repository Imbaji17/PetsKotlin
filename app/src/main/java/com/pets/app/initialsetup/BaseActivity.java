package com.pets.app.initialsetup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.pets.app.R;

/**
 * Created by BAJIRAO on 05 January 2018.
 */
@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    protected Activity mActivity;
    private ProgressDialog mProgressDialog;
    protected RelativeLayout mProgressBar;
    private FrameLayout mFrameLayout;
    protected boolean isDataChanged;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(R.layout.activity_base);
        initializeView(layoutResID);
    }

    /**
     * Initialize View
     */
    private void initializeView(int layoutResID) {

        mFrameLayout = findViewById(R.id.frame_layout);
        mProgressBar = findViewById(R.id.rel_progress_bar);
        mProgressBar.setClickable(true);

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        mFrameLayout.addView(layoutInflater.inflate(layoutResID, null));
    }

    protected void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        mFrameLayout.setClickable(false);
    }

    protected void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
        mFrameLayout.setClickable(true);
    }

    protected void showProgressDialog() {
        mProgressDialog = new ProgressDialog(BaseActivity.this);
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    protected void showProgressDialog(String msg) {
        mProgressDialog = new ProgressDialog(BaseActivity.this);
        mProgressDialog.setMessage(msg);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    protected void hideProgressDialogue() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
