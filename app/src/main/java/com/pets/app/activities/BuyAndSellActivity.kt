package com.pets.app.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.Button
import com.pets.app.R
import com.pets.app.activities.adoption.PostProductActivity
import com.pets.app.adapters.BuySellPagerAdapter
import com.pets.app.common.Enums
import com.pets.app.initialsetup.BaseActivity


class BuyAndSellActivity : BaseActivity(), View.OnClickListener {

    private var vpBuySell: ViewPager? = null
    private var tlBuySell: TabLayout? = null
    private var adapter: BuySellPagerAdapter? = null
    private var btnPostProduct: Button? = null
    private val RC_ADD_PRODUCT: Int = 100

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
        btnPostProduct = findViewById(R.id.btnPostProduct)
        adapter = BuySellPagerAdapter(this, supportFragmentManager)
        vpBuySell!!.adapter = adapter
        tlBuySell!!.setupWithViewPager(vpBuySell)
        btnPostProduct!!.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.btnPostProduct -> {
                var type = ""
                if (vpBuySell!!.currentItem == 0) {
                    type = Enums.buySell.BUY.name
                } else {
                    type = Enums.buySell.SELL.name
                }
                PostProductActivity.startActivity(this, RC_ADD_PRODUCT, type)
            }
        }
    }

}
