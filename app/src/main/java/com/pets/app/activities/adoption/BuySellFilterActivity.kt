package com.pets.app.activities.adoption

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.pets.app.R
import com.pets.app.initialsetup.BaseActivity

class BuySellFilterActivity : BaseActivity() {

    companion object {
        private val TAG = BuySellFilterActivity::class.java.simpleName
        fun startActivity(activity: Activity, requestCode: Int) {
            val intent = Intent(activity, BuySellFilterActivity::class.java)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_sell_filter)
    }
}
