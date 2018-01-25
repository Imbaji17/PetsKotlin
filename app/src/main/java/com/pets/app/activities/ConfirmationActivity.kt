package com.pets.app.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.pets.app.R

class ConfirmationActivity : AppCompatActivity() {

    companion object {

        fun startActivity(mActivity: Activity) {
            val mIntent = Intent(mActivity, ConfirmationActivity::class.java)
            mActivity.startActivity(mIntent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)
    }
}
