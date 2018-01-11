package com.pets.app.initialsetup

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import com.pets.app.R
import com.pets.app.common.AppPreferenceManager

class SplashScreenActivity : BaseActivity() {

    private var mHandler: Handler? = null;
    private val DELAY: Long = 3000;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        callHandler();
    }

    private fun callHandler() {

        mHandler = Handler()
        mHandler!!.postDelayed(Runnable {
            val mIntent: Intent?
            if (TextUtils.isEmpty(AppPreferenceManager.getUserID())) {
                mIntent = Intent(this, LoginActivity::class.java);
//                mIntent = Intent(this, FindHostelActivity::class.java);
            } else {
                mIntent = Intent(this, LandingActivity::class.java);
            }
            this.startActivity(mIntent)
            this.finish();
        }, DELAY)
    }
}
