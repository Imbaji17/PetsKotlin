package com.pets.app.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.pets.app.R
import com.pets.app.initialsetup.BaseActivity
import kotlinx.android.synthetic.main.activity_confirmation.*

class ConfirmationActivity : BaseActivity() {

    companion object {

        fun startActivity(mActivity: Activity) {
            val mIntent = Intent(mActivity, ConfirmationActivity::class.java)
            mActivity.startActivity(mIntent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        initView()
    }

    private fun initView() {


        btnNo?.setOnClickListener {
            this.finish()
        }

        btnYes?.setOnClickListener {

        }
    }
}
