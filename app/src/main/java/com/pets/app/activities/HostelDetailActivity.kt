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

    var viewPager: ViewPager? = null
    var tvName: TextView? = null
    var ratingBar: RatingBar? = null
    var tvReview: TextView? = null
    var llContactPerson: LinearLayout? = null
    var tvContactPerson: TextView? = null
    var llContact: LinearLayout? = null
    var tvContact: TextView? = null
    var llDistance: LinearLayout? = null
    var tvDistance: TextView? = null
    var llDescription: LinearLayout? = null
    var tvDescription: TextView? = null
    var llImages: LinearLayout? = null
    var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hostel_detail)
    }

    private fun init() {

    }

    private fun initView() {
        viewPager = findViewById(R.id.viewPager)
        tvName = findViewById(R.id.tvName)
        ratingBar = findViewById(R.id.ratingBar)
    }
}
