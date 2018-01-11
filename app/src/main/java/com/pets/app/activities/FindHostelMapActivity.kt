package com.pets.app.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ViewFlipper
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.GsonBuilder
import com.pets.app.R
import com.pets.app.common.ApplicationsConstants
import com.pets.app.common.Constants
import com.pets.app.initialsetup.BaseActivity
import com.pets.app.model.FindHostel
import com.pets.app.model.FindHostelResponse
import com.pets.app.model.NormalResponse
import com.pets.app.utilities.Logger
import com.pets.app.utilities.TimeStamp
import com.pets.app.utilities.Utils
import com.pets.app.webservice.RestClient
import com.pets.app.webservice.WebServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*


class FindHostelMapActivity : BaseActivity(), OnMapReadyCallback, View.OnClickListener {

    private var mMap: GoogleMap? = null
    private var listItems = java.util.ArrayList<Any>()
    private var edtSearch: EditText? = null
    private var latitude: Double? = 0.0
    private var longitude: Double? = 0.0
    private val RC_AUTOCOMPLETE: Int = 100
    private var viewFlipper: ViewFlipper? = null
    private val nextOffset: Int = 0
    private var mapFragment: SupportMapFragment? = null

    companion object {
        private val TAG = FindHostelMapActivity::class.java.simpleName
        fun startActivity(activity: Activity, list: ArrayList<Any>, requestCode: Int) {
            val intent = Intent(activity, FindHostelMapActivity::class.java)
            intent.putExtra(ApplicationsConstants.DATA, list)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_hostel_map)
        initializeToolbar(getString(R.string.find_hostel))
        init()
        initView()
        invalidateOptionsMenu();
        viewFlipper!!.displayedChild = 0

    }

    private fun init() {
        listItems = intent.getSerializableExtra(ApplicationsConstants.DATA) as java.util.ArrayList<Any>
    }

    @SuppressLint("NewApi")
    private fun initView() {
        mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment!!.getMapAsync(this)
        viewFlipper = findViewById(R.id.viewFlipper)
        edtSearch = findViewById(R.id.edtSearch)
        edtSearch!!.isFocusable = false
        edtSearch!!.setOnClickListener(this)
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
                    if (latitude!! > 0 && longitude!! > 0) {
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude!!, longitude!!), 7f))
                    } else {
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(findHostel.lat, findHostel.lng), 7f))
                    }
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
        settingsItem.icon = ContextCompat.getDrawable(this, R.drawable.menu)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        when (id) {
            android.R.id.home -> {
                finishActivity()
                return true
            }

            R.id.action_map -> {
                finishActivity()
            }

            R.id.action_search -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.edtSearch -> {
                openAutocompleteActivity()
            }
        }
    }

    private fun openAutocompleteActivity() {

        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(this)
            startActivityForResult(intent, RC_AUTOCOMPLETE)
        } catch (e: GooglePlayServicesRepairableException) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.connectionStatusCode,
                    0 /* requestCode */).show()
        } catch (e: GooglePlayServicesNotAvailableException) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            val message = "Google Play Services is not available: " + GoogleApiAvailability.getInstance().getErrorString(e.errorCode)

            Log.e("Tag", message)
            Utils.showToast(message)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        Logger.errorLog("requestCode : " + requestCode)
        if (requestCode == RC_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                val place = PlaceAutocomplete.getPlace(this, data)
                Log.i("TAG", "Place Selected: " + place.name)
                val latLng = place.latLng
                latitude = latLng.latitude
                longitude = latLng.longitude

                if (!TextUtils.isEmpty(place.address))
                    edtSearch!!.setText(place.address)

                listItems.clear()
                mapFragment!!.getMapAsync(this@FindHostelMapActivity)
                getHostelList()
                Logger.errorLog(place.id + "\n" + place.placeTypes + "\n" + place.address + "\n" + place.locale)
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                val status = PlaceAutocomplete.getStatus(this, data)
                Log.e("TAG", "Error: Status = " + status.toString())
            } else if (resultCode == RESULT_CANCELED) {
            }
        }
    }

    private fun getHostelList() {
        val timeStamp = TimeStamp.getTimeStamp()
        val key = TimeStamp.getMd5(timeStamp + "10" + Constants.TIME_STAMP_KEY)
        viewFlipper!!.displayedChild = 0
        if (Utils.isOnline(this)) {
            val apiClient = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
            val call = apiClient.hostelList(key, "", "EN", latitude.toString(), longitude.toString(), nextOffset, timeStamp, "10")
            call.enqueue(object : Callback<FindHostelResponse> {
                override fun onResponse(call: Call<FindHostelResponse>, response: Response<FindHostelResponse>?) {
                    if (response != null) {
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().list != null) {
                                listItems.addAll(response.body().list);
                            }
                        } else if (response.code() == 403) {
                            val gson = GsonBuilder().create()
                            val mError: NormalResponse
                            try {
                                mError = gson.fromJson(response.errorBody().string(), NormalResponse::class.java)
//                                Utils.showToast(mActivity, "" + mError.getMessage())
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                    }
                    mapFragment!!.getMapAsync(this@FindHostelMapActivity)
                }

                override fun onFailure(call: Call<FindHostelResponse>, t: Throwable) {
                }
            })

        } else {
            viewFlipper!!.displayedChild = 3
        }
    }

    override fun onBackPressed() {
        finishActivity()
    }

    private fun finishActivity() {
        val selectedAddIntent = Intent()
        selectedAddIntent.putExtra(ApplicationsConstants.LATITUDE, latitude)
        selectedAddIntent.putExtra(ApplicationsConstants.LATITUDE, longitude)
        selectedAddIntent.putExtra(ApplicationsConstants.DATA, listItems)
        selectedAddIntent.putExtra(ApplicationsConstants.NAME, edtSearch!!.text.toString())
        setResult(Activity.RESULT_OK, selectedAddIntent)
        finish()
    }

}
