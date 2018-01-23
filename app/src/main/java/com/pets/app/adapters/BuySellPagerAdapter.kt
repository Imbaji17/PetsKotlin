package com.pets.app.adapters

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

import com.pets.app.R
import com.pets.app.fragments.BuyFragment
import com.pets.app.fragments.SellFragment

/**
 * Created by admin on 23/01/18.
 */

class BuySellPagerAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {

        return if (position == 0) {
            BuyFragment()
        } else if (position == 1) {
            SellFragment()
        } else {
            null
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> context.getString(R.string.buy)
            1 -> context.getString(R.string.sell)
            else -> null
        }
    }

}