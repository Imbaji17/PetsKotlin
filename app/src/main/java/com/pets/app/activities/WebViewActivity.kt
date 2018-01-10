package com.pets.app.activities

import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import android.widget.ViewFlipper
import com.pets.app.R
import com.pets.app.initialsetup.BaseActivity

class WebViewActivity : BaseActivity() {

    private var viewFlipper: ViewFlipper? = null
    private var webView: WebView? = null
    private var btnTry: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        initializeToolbar(this.getString(R.string.app_name))
        initView();
    }

    private fun initView() {

        viewFlipper = findViewById(R.id.viewFlipper)
        webView = findViewById(R.id.webView)
        btnTry = findViewById(R.id.btnRetry)

        viewFlipper!!.displayedChild = 1

        btnTry
    }
}
