package com.pets.app.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.pets.app.R
import com.pets.app.common.ApplicationsConstants
import com.pets.app.initialsetup.BaseActivity
import com.pets.app.model.FindHostel
import java.util.ArrayList
import com.pets.app.R.mipmap.ic_launcher


class FindHostelMapActivity : BaseActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private var listItems = java.util.ArrayList<Any>()

    companion object {
        private val TAG = FindHostelMapActivity::class.java.simpleName
        fun startActivity(activity: Activity, list: ArrayList<Any>) {
            val intent = Intent(activity, FindHostelMapActivity::class.java)
            intent.putExtra(ApplicationsConstants.DATA, list)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_hostel_map)
        initializeToolbar(getString(R.string.find_hostel))
        init()
        initView()
        invalidateOptionsMenu();
    }

    private fun init() {
        listItems = intent.getSerializableExtra(ApplicationsConstants.DATA) as java.util.ArrayList<Any>
    }

    private fun initView() {
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap!!.clear()
        if (!listItems.isEmpty()) {
            var cnt = 0
            for (i in listItems.indices) {
                var findHostel = listItems[i] as FindHostel
                googleMap!!.addMarker(MarkerOptions()
                        .position(LatLng(findHostel.lat, findHostel.lng))
                        .title(findHostel.hostelName)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.location))
                        .snippet(findHostel.address))

                if (cnt == 0) {
                    cnt++
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(findHostel.lat, findHostel.lng), 7f))
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_find_hostel, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val settingsItem = menu!!.findItem(R.id.action_map)
        // set your desired icon here based on a flag if you like
        settingsItem.icon = ContextCompat.getDrawable(this, R.drawable.menu)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        when (id) {
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.action_map -> {
            }

            R.id.action_search -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }
}
