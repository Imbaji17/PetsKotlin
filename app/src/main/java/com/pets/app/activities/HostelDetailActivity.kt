package com.pets.app.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import com.pets.app.R

class HostelDetailActivity : AppCompatActivity() {

    private var viewPager: ViewPager? = null
    private var tvName: TextView? = null
    private var ratingBar: RatingBar? = null
    private var tvReview: TextView? = null
    private var llContactPerson: LinearLayout? = null
    private var tvContactPerson: TextView? = null
    private var llContact: LinearLayout? = null
    private var tvContact: TextView? = null
    private var llDistance: LinearLayout? = null
    private var tvDistance: TextView? = null
    private var llDescription: LinearLayout? = null
    private var tvDescription: TextView? = null
    private var llImages: LinearLayout? = null
    private var recyclerView: RecyclerView? = null
    var hostelId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hostel_detail)
    }

    private fun init() {

    }

    private fun initView() {
//        viewPager = findViewById(R.id.viewPager)
//        tvName = findViewById(R.id.tvName)
//        ratingBar = findViewById(R.id.ratingBar)
//        tvReview = findViewById(R.id.tvReview)
        llContactPerson = findViewById(R.id.llContactPerson)
        tvContactPerson = findViewById(R.id.tvContactPerson)
        llContact = findViewById(R.id.llContact)
        tvContact = findViewById(R.id.tvContact)
        llDistance = findViewById(R.id.llDistance)
        tvDistance = findViewById(R.id.tvDistance)
        llDescription = findViewById(R.id.llDescription)
        tvDescription = findViewById(R.id.tvDescription)
        llImages = findViewById(R.id.llImages)
        recyclerView = findViewById(R.id.recyclerView)
    }

    private fun setValues() {

    }
}
