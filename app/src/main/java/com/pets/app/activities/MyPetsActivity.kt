package com.pets.app.activities

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ViewFlipper
import com.pets.app.R
import com.pets.app.initialsetup.BaseActivity

class MyPetsActivity : BaseActivity() {

    private var mViewFlipper: ViewFlipper? = null
    private var mRecyclerView: RecyclerView? = null
    private var linLoadMore: LinearLayout? = null
    private var tvMessage: TextView? = null
    private var btnRetry: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_pets)

        initializeToolbar(this.getString(R.string.my_pets))
        initView()
    }

    private fun initView() {

        mViewFlipper = findViewById(R.id.viewFlipper)
        mRecyclerView = findViewById(R.id.recyclerView)
        linLoadMore = findViewById(R.id.linLoadMore)
        tvMessage = findViewById(R.id.tvNoResult)
        btnRetry = findViewById(R.id.btnRetry)

        val mLinearLayoutManager = LinearLayoutManager(this)
        mLinearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        mRecyclerView?.layoutManager = mLinearLayoutManager
    }
}
