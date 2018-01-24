package com.pets.app.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import com.pets.app.R
import com.pets.app.adapters.BuySellPagerAdapter
import com.pets.app.initialsetup.BaseActivity


class BuyAndSellActivity : BaseActivity() {

    private var vpBuySell: ViewPager? = null
    private var tlBuySell: TabLayout? = null
    private var adapter: BuySellPagerAdapter? = null

    companion object {
        private val TAG = BuyAndSellActivity::class.java.simpleName
        fun startActivity(activity: Activity) {
            val intent = Intent(activity, BuyAndSellActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_and_sell)
        initializeToolbar(getString(R.string.buy_and_sell))
        initView()
    }

    fun initView() {
        vpBuySell = findViewById(R.id.vpBuySell)
        tlBuySell = findViewById(R.id.tlBuySell)
        adapter = BuySellPagerAdapter(this, supportFragmentManager)
        vpBuySell!!.adapter = adapter;
        tlBuySell!!.setupWithViewPager(vpBuySell);
    }
}
