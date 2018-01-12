package com.pets.app.activities

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.ViewFlipper
import com.pets.app.R
import com.pets.app.common.ApplicationsConstants
import com.pets.app.common.Constants
import com.pets.app.initialsetup.BaseActivity
import com.pets.app.utilities.Utils
import kotlinx.android.synthetic.main.app_toolbar.*

class WebViewActivity : BaseActivity() {

    private var viewFlipper: ViewFlipper? = null
    private var webView: WebView? = null
    private var btnTry: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        initializeToolbar(this.getString(R.string.app_name))
        initView();
        getIntentData()
    }

    private fun initView() {

        viewFlipper = findViewById(R.id.viewFlipper)
        webView = findViewById(R.id.webView)
        btnTry = findViewById(R.id.btnRetry)

        viewFlipper!!.displayedChild = 1

        btnTry!!.setOnClickListener(View.OnClickListener {
            if (Utils.isOnline(this)) {
                viewFlipper?.displayedChild = 0
                setDataToWebView()
            } else {
                viewFlipper?.displayedChild = 1
                Utils.showToast(this.getString(R.string.device_is_offline))
            }
        })
    }

    private fun getIntentData() {

        if (intent.getStringExtra(ApplicationsConstants.NAVIGATION_TYPE).equals(Constants.TERMS_AND_CONDITIONS_URL, true)) {
            tvToolbar?.setText(this.getText(R.string.terms_and_conditions))
        }

        if (Utils.isOnline(this)) {
            viewFlipper?.displayedChild = 0
            setDataToWebView()
        } else {
            viewFlipper?.displayedChild = 1
        }
    }

    private fun setDataToWebView() {

        if (intent.getStringExtra(ApplicationsConstants.NAVIGATION_TYPE).equals(Constants.TERMS_AND_CONDITIONS_URL, true)) {
            webView?.loadUrl(Constants.TERMS_AND_CONDITIONS_URL)
        }
    }
}
