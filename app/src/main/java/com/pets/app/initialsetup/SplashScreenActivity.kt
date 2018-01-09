package com.pets.app.initialsetup

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.pets.app.R

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

            val mIntent = Intent(this, LoginActivity::class.java);
            startActivity(mIntent)
            this.finish();
        }, DELAY)
    }
}
