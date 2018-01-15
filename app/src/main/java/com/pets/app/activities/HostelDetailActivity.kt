package com.pets.app.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.google.gson.GsonBuilder
import com.pets.app.R
import com.pets.app.adapters.ImageAdapter
import com.pets.app.common.AppPreferenceManager
import com.pets.app.common.ApplicationsConstants
import com.pets.app.common.Constants
import com.pets.app.common.Enums
import com.pets.app.initialsetup.BaseActivity
import com.pets.app.model.FindHostel
import com.pets.app.model.FindHostelResponse
import com.pets.app.model.NormalResponse
import com.pets.app.utilities.TimeStamp
import com.pets.app.utilities.Utils
import com.pets.app.webservice.RestClient
import com.pets.app.webservice.WebServiceBuilder
import com.viewpagerindicator.CirclePageIndicator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class HostelDetailActivity : BaseActivity(), View.OnClickListener {

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
    private var hostelId: String? = null
    private var viewFlipper: ViewFlipper? = null
    private var btnRetry: Button? = null
    private var rlForLoadingScreen: RelativeLayout? = null
    private var mainLayout: NestedScrollView? = null
    private var llForNoResult: LinearLayout? = null
    private var llForOfflineScreen: LinearLayout? = null
    private var llAddress: LinearLayout? = null
    private var tvAddress: TextView? = null
    private var cvp: CirclePageIndicator? = null

    companion object {
        private val TAG = HostelDetailActivity::class.java.simpleName
        fun startActivity(activity: Activity, hostelId: String) {
            val intent = Intent(activity, HostelDetailActivity::class.java)
            intent.putExtra(ApplicationsConstants.ID, hostelId)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hostel_detail)
        initializeToolbar(getString(R.string.hostel))
        init()
        initView()
        getHostelDetails()
    }

    private fun init() {
        hostelId = intent.getStringExtra(ApplicationsConstants.ID)
    }

    private fun initView() {
        viewPager = findViewById(R.id.viewPager)
        tvName = findViewById(R.id.tvName)
        ratingBar = findViewById(R.id.ratingBar)
        tvReview = findViewById(R.id.tvReview)
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
        llAddress = findViewById(R.id.llAddress)
        tvAddress = findViewById(R.id.tvAddress)
        cvp = findViewById(R.id.cvp)

        viewFlipper = findViewById(R.id.viewFlipper)
        rlForLoadingScreen = findViewById(R.id.rlForLoadingScreen)
        mainLayout = findViewById(R.id.mainLayout)
        llForNoResult = findViewById(R.id.llForNoResult)
        llForOfflineScreen = findViewById(R.id.llForOfflineScreen)
        btnRetry = findViewById(R.id.btnRetry)
        btnRetry?.setOnClickListener(this)
        tvReview?.setOnClickListener(this)
    }

    private fun getHostelDetails() {
        setLoadingLayout()
        val timeStamp = TimeStamp.getTimeStamp()
        val userId = AppPreferenceManager.getUserID()
        val lat = AppPreferenceManager.getUser().lat
        val lng = AppPreferenceManager.getUser().lng

        val key = TimeStamp.getMd5(timeStamp + userId + hostelId + Constants.TIME_STAMP_KEY)
        if (Utils.isOnline(this)) {
            val apiClient = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
            val call = apiClient.hostelDetailsById(hostelId, key, "EN", lat, lng, timeStamp, userId)
            call.enqueue(object : Callback<FindHostelResponse> {
                override fun onResponse(call: Call<FindHostelResponse>, response: Response<FindHostelResponse>?) {
                    if (response != null && response.isSuccessful() && response.body() != null && response.body().result != null) {
                        setMainLayout()
                        setValues(response.body().result)
                    } else {
                        setNoDataLayout()
                        val gson = GsonBuilder().create()
                        val mError: NormalResponse
                        try {
                            mError = gson.fromJson(response!!.errorBody().string(), NormalResponse::class.java)
                            Utils.showToast("" + mError.getMessage())
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<FindHostelResponse>, t: Throwable) {
                    setNoDataLayout()
                }
            })

        } else {
            setOfflineLayout()
        }
    }

    private fun setValues(result: FindHostel) {

        if (!TextUtils.isEmpty(result.contactPerson)) {
            llContactPerson?.visibility = View.VISIBLE
            tvContactPerson?.text = result.contactPerson
        } else {
            llContactPerson?.visibility = View.GONE
        }

        if (!TextUtils.isEmpty(result.contactNumber)) {
            llContact?.visibility = View.VISIBLE
            tvContact?.text = result.contactNumber
        } else {
            llContact?.visibility = View.GONE
        }

        if (!TextUtils.isEmpty(result.address)) {
            llAddress?.visibility = View.VISIBLE
            tvAddress?.text = result.address
        } else {
            llAddress?.visibility = View.GONE
        }

        if (result.lat > 0 && result.lng > 0) {
            llDistance?.visibility = View.VISIBLE
            var distance = Utils.getDistance(this, result.lat.toString(), result.lng.toString())
            tvDistance?.text = String.format(getString(R.string.x_miles), distance);
        } else llDistance?.visibility = View.GONE


        if (!TextUtils.isEmpty(result.description)) {
            llDescription?.visibility = View.VISIBLE
            tvDescription?.text = result.description
        } else {
            llDescription?.visibility = View.GONE
        }

        if (!result.hostelImages.isEmpty()) {
            val adapter = ImageAdapter(this, result.hostelImages)
            viewPager?.adapter = adapter
            cvp!!.setViewPager(viewPager)
        }

        ratingBar?.rating = result.avgRating.toFloat();
        tvReview?.text = resources.getQuantityString(R.plurals.reviews_plural, result.reviewsCount, result.reviewsCount)
        tvName?.text = if (!TextUtils.isEmpty(result.hostelName)) {
            result.hostelName
        } else {
            ""
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnRetry -> {
                getHostelDetails()
            }
            R.id.tvReview -> {
                ReviewActivity.startActivity(this, hostelId!!, Enums.Favourite.HOSTEL.name)
            }
        }
    }


    private fun setOfflineLayout() {
        viewFlipper!!.displayedChild = viewFlipper!!.indexOfChild(llForOfflineScreen)
    }

    private fun setLoadingLayout() {
        viewFlipper!!.displayedChild = viewFlipper!!.indexOfChild(rlForLoadingScreen)
    }

    private fun setMainLayout() {
        viewFlipper!!.displayedChild = viewFlipper!!.indexOfChild(mainLayout)
    }

    private fun setNoDataLayout() {
        viewFlipper!!.displayedChild = viewFlipper!!.indexOfChild(llForNoResult)
    }

}
