package com.pets.app.initialsetup

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.pets.app.R
import com.pets.app.mediator.SocialIntegratorInterface
import kotlinx.android.synthetic.main.app_loading_screen.*

/**
 * Created by BAJIRAO on 05 January 2018.
 */
@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    private var mProgressBar: RelativeLayout? = null
    protected var isDataChanged: Boolean = false
    protected var isHeaderImage: Boolean = false
    protected var toolbar: Toolbar? = null
    private var mProgressDialog: ProgressDialog? = null
    private var mFrameLayout: FrameLayout? = null
    /*ViewFlipper Recycler View*/
    protected var mViewFlipper: ViewFlipper? = null
    protected var mRecyclerView: RecyclerView? = null
    protected var linLoadMore: LinearLayout? = null
    protected var tvMessage: TextView? = null
    protected var btnRetry: Button? = null

    companion object {

        var mSocialIntegratorInterface: SocialIntegratorInterface? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale)
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate)
    }

    override fun setContentView(@LayoutRes layoutResID: Int) {
        super.setContentView(R.layout.activity_base)
        initializeView(layoutResID)
    }

    private fun initializeView(layoutResID: Int) {

        mFrameLayout = findViewById(R.id.frame_layout)
        mProgressBar = findViewById(R.id.rel_progress_bar)
        mProgressBar!!.isClickable = true

        val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mFrameLayout!!.addView(layoutInflater.inflate(layoutResID, null))
    }

    /**
     * Initialize Toolbar
     */
    protected fun initializeToolbar(cmgString: String) {

        toolbar = findViewById(R.id.toolbar)
        if (toolbar != null) {
            val tvToolbar = toolbar!!.findViewById<TextView>(R.id.tvToolbar)
            tvToolbar.text = cmgString
            val imgView = toolbar!!.findViewById<ImageView>(R.id.imgHeader)
            if (isHeaderImage) {
                imgView.visibility = View.VISIBLE
            }
            setSupportActionBar(toolbar)
        }
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.setDisplayShowTitleEnabled(false)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.app_gradient))
            actionBar.setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.back))
        }
    }

    protected fun initViewFlipperWithRecyclerView() {

        mViewFlipper = findViewById(R.id.viewFlipper)
        mRecyclerView = findViewById(R.id.recyclerView)
        linLoadMore = findViewById(R.id.linLoadMore)
        tvMessage = findViewById(R.id.tvNoResult)
        btnRetry = findViewById(R.id.btnRetry)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    protected fun showProgressBar() {
        mProgressBar!!.visibility = View.VISIBLE
        mFrameLayout!!.isClickable = false
    }

    protected fun hideProgressBar() {
        mProgressBar!!.visibility = View.GONE
        mFrameLayout!!.isClickable = true
    }

    protected fun showProgressDialog() {
        mProgressDialog = ProgressDialog(this@BaseActivity)
        mProgressDialog!!.setMessage(getString(R.string.please_wait))
        mProgressDialog!!.setCancelable(false)
        mProgressDialog!!.show()
    }

    protected fun showProgressDialog(msg: String) {
        mProgressDialog = ProgressDialog(this@BaseActivity)
        mProgressDialog!!.setMessage(msg)
        mProgressDialog!!.setCancelable(false)
        mProgressDialog!!.show()
    }

    protected fun hideProgressDialogue() {
        if (mProgressDialog != null)
            mProgressDialog!!.dismiss()
    }

    fun showOfflineMode() {
        mViewFlipper?.displayedChild = mViewFlipper!!.indexOfChild(rlForLoadingScreen)
    }

    fun showLoader() {
        mViewFlipper?.displayedChild = mViewFlipper!!.indexOfChild(rlForLoadingScreen)
    }

    fun showMainLayout() {
        mViewFlipper?.displayedChild = mViewFlipper!!.indexOfChild(rlForLoadingScreen)
    }

    fun showNoDataFound() {
        mViewFlipper?.displayedChild = mViewFlipper!!.indexOfChild(rlForLoadingScreen)
    }

}
