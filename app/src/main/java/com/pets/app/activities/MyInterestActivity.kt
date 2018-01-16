package com.pets.app.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.pets.app.R
import com.pets.app.adapters.LandingMenuAdapter
import com.pets.app.initialsetup.BaseActivity
import com.pets.app.interfaces.SimpleItemClickListener
import com.pets.app.model.`object`.LandingDetails

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
        val mGridLayoutManager = GridLayoutManager(this, 3)
        mGridLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView?.isNestedScrollingEnabled = false
        recyclerView?.layoutManager = mGridLayoutManager
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
//            0 -> {
//                val mIntent = Intent(this, ProfileActivity::class.java)
//                this.startActivity(mIntent)
//            }
//            1 -> {
//                val mIntent = Intent(this, ProfileActivity::class.java)
//                this.startActivity(mIntent)
//            }
//            2 -> {
//                val mIntent = Intent(this, ProfileActivity::class.java)
//                this.startActivity(mIntent)
//            }
//            3 -> {
//                val mIntent = Intent(this, FindHostelActivity::class.java)
//                this.startActivity(mIntent)
//            }
//            4 -> {
//                val mIntent = Intent(this, ProfileActivity::class.java)
//                this.startActivity(mIntent)
//            }
        }
    }
}
