package com.pets.app.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.widget.ViewFlipper
import com.pets.app.R

class MyPetsActivity : AppCompatActivity() {

    private var mViewFlipper: ViewFlipper? = null
    private var mRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_pets)
    }
}
