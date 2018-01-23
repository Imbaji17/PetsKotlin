package com.pets.app.common

import android.annotation.SuppressLint
import android.widget.ViewFlipper
import com.pets.app.initialsetup.BaseActivity
import kotlinx.android.synthetic.main.app_loading_screen.*

@SuppressLint("Registered")
/**
 * Created by BAJIRAO on 23 January 2018.
 */
class AppViewFlipper : BaseActivity() {

    fun showOfflineMode(mViewFlipper: ViewFlipper?) {
        mViewFlipper!!.displayedChild = mViewFlipper.indexOfChild(rlForLoadingScreen)
    }

    fun showLoader(mViewFlipper: ViewFlipper?) {
        mViewFlipper!!.displayedChild = mViewFlipper.indexOfChild(rlForLoadingScreen)
    }

    fun showMainLayout(mViewFlipper: ViewFlipper?) {
        mViewFlipper!!.displayedChild = mViewFlipper.indexOfChild(rlForLoadingScreen)
    }

    fun showNoDataFound(mViewFlipper: ViewFlipper?, string: String) {
        mViewFlipper!!.displayedChild = mViewFlipper.indexOfChild(rlForLoadingScreen)
    }
}