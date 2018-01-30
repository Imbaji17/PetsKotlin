package com.pets.app.activities

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v4.widget.NestedScrollView
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import android.widget.ViewFlipper
import com.pets.app.R
import com.pets.app.common.AppPreferenceManager
import com.pets.app.common.Constants
import com.pets.app.initialsetup.BaseActivity
import com.pets.app.model.FindHostelResponse
import com.pets.app.utilities.TimeStamp
import com.pets.app.utilities.Utils
import com.pets.app.webservice.RestClient
import com.pets.app.webservice.WebServiceBuilder
import com.viewpagerindicator.CirclePageIndicator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PetSitterDetailsActivity : BaseActivity(), View.OnClickListener {

    private var viewPager: ViewPager? = null
    private var pageIndicator: CirclePageIndicator? = null
    private var tvName: TextView? = null
    private var ratingBar: RatingBar? = null
    private var tvReview: TextView? = null
    private var tvContactPerson: TextView? = null
    private var tvContact: TextView? = null
    private var tvDistance: TextView? = null
    private var tvDescription: TextView? = null
    private var viewFlipper: ViewFlipper? = null
    private var mainLayout: NestedScrollView? = null
    private var tvAddress: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_sitter_details)

        initializeToolbar(getString(R.string.pet_sitter))
        initView()
        clickListeners()
        checkValidations()
    }

    private fun initView() {

        viewPager = findViewById(R.id.viewPager)
        tvName = findViewById(R.id.tvName)
        ratingBar = findViewById(R.id.ratingBar)
        tvReview = findViewById(R.id.tvReview)
        tvContactPerson = findViewById(R.id.tvContactPerson)
        tvContact = findViewById(R.id.tvContact)
        tvDistance = findViewById(R.id.tvDistance)
        tvDescription = findViewById(R.id.tvDescription)
        tvAddress = findViewById(R.id.tvAddress)
        pageIndicator = findViewById(R.id.cvp)

        viewFlipper = findViewById(R.id.viewFlipper)
        mainLayout = findViewById(R.id.mainLayout)
        btnRetry = findViewById(R.id.btnRetry)
    }

    private fun clickListeners() {

        btnRetry?.setOnClickListener(this)
        tvReview?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.btnRetry -> {

            }
            R.id.tvReview -> {

            }
        }
    }

    private fun checkValidations() {

        if (Utils.isOnline(this)) {
            showMainScreenLayout()
            getPetSitterDetailsApiCall()
        } else {
            showOfflineMode()
        }
    }

    private fun getPetSitterDetailsApiCall() {

        val userId = AppPreferenceManager.getUserID()
        val timestamp = TimeStamp.getTimeStamp()
        val key = TimeStamp.getMd5(timestamp + userId + Constants.TIME_STAMP_KEY)

        showLoader()
        val apiClient = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
        val call = apiClient.hostelDetailsById(hostelId, key, "EN", lat, lng, timeStamp, userId)
        call.enqueue(object : Callback<FindHostelResponse> {
            override fun onResponse(call: Call<FindHostelResponse>, response: Response<FindHostelResponse>?) {
                if (response != null && response.isSuccessful && response.body() != null && response.body().result != null) {
                } else {
                    Utils.showErrorToast(response?.errorBody())
                }
            }

            override fun onFailure(call: Call<FindHostelResponse>, t: Throwable) {
            }
        })
    }

    private fun showMainScreenLayout() {
        mViewFlipper?.displayedChild = mViewFlipper!!.indexOfChild(mainLayout)
    }
}
