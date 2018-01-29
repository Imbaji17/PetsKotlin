package com.pets.app.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.pets.app.R
import com.pets.app.activities.adoption.AdoptionListActivity
import com.pets.app.adapters.LandingMenuAdapter
import com.pets.app.initialsetup.BaseActivity
import com.pets.app.interfaces.SimpleItemClickListener
import com.pets.app.model.`object`.LandingDetails
import com.pets.app.utilities.GridSpacingItemDecoration

class MyInterestActivity : BaseActivity(), SimpleItemClickListener {


    private var recyclerView: RecyclerView? = null

    companion object {
        private val TAG = ReviewActivity::class.java.simpleName
        fun startActivity(activity: Activity) {
            val intent = Intent(activity, MyInterestActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_interest)
        initializeToolbar(getString(R.string.my_interests))
        initView()
    }

    fun initView() {
        recyclerView = findViewById(R.id.recyclerView)

        val spanCount = 2 // 3 columns
        val spacing = 50 // 50px
        val includeEdge = true
        recyclerView!!.addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, includeEdge))
        val gridLayoutManager = GridLayoutManager(this, spanCount)

//        val mGridLayoutManager = GridLayoutManager(this, 2)
//        mGridLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView?.isNestedScrollingEnabled = false
        recyclerView?.layoutManager = gridLayoutManager
        val mList = ArrayList<LandingDetails>()
        mList.add(object : LandingDetails(this.getString(R.string.vets), R.drawable.find_vet) {})
        mList.add(object : LandingDetails(this.getString(R.string.hostel), R.drawable.find_hostel) {})
        mList.add(object : LandingDetails(this.getString(R.string.adoption), R.drawable.adoption) {})
        mList.add(object : LandingDetails(this.getString(R.string.pets_match), R.drawable.find_match) {})
        val adapter = LandingMenuAdapter(this, mList)
        recyclerView?.adapter = adapter
        adapter.setItemClick(this)
    }

    override fun onItemClick(`object`: Any?) {
        when (`object` as Int) {
            0 -> {
            }
            1 -> {
                FindHostelActivity.startActivity(this, "Y")
            }
            2 -> {
                AdoptionListActivity.startActivity(this, "Y")
            }
            3 -> {
            }
        }
    }
}
