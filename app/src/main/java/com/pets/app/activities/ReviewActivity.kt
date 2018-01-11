package com.pets.app.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.ViewFlipper
import com.google.gson.GsonBuilder
import com.pets.app.R
import com.pets.app.common.ApplicationsConstants
import com.pets.app.common.Constants
import com.pets.app.model.FindHostelResponse
import com.pets.app.model.NormalResponse
import com.pets.app.utilities.TimeStamp
import com.pets.app.utilities.Utils
import com.pets.app.webservice.RestClient
import com.pets.app.webservice.WebServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ReviewActivity : AppCompatActivity(), View.OnClickListener {


    private var viewFlipper: ViewFlipper? = null
    private var rlForLoadingScreen: RelativeLayout? = null
    private var recyclerView: RecyclerView? = null
    private var llForNoResult: LinearLayout? = null
    private var llForOfflineScreen: LinearLayout? = null
    private var btnRetry: Button? = null
    private var hostelId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
    }

    private fun init() {
        hostelId = intent.getStringExtra(ApplicationsConstants.ID)
    }

    private fun initView() {

        viewFlipper = findViewById(R.id.viewFlipper)
        rlForLoadingScreen = findViewById(R.id.rlForLoadingScreen)
        recyclerView = findViewById(R.id.recyclerView)
        llForNoResult = findViewById(R.id.llForNoResult)
        llForOfflineScreen = findViewById(R.id.llForOfflineScreen)
        btnRetry = findViewById(R.id.btnRetry)
        btnRetry?.setOnClickListener(this)
    }


    override fun onClick(p0: View?) {


    }

//    private fun getHostelDetails() {
//        setLoadingLayout()
//        val timeStamp = TimeStamp.getTimeStamp()
//        val key = TimeStamp.getMd5(timeStamp + "10" + hostelId + Constants.TIME_STAMP_KEY)
//        if (Utils.isOnline(this)) {
//            val apiClient = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
//            val call = apiClient.hostelDetailsById(hostelId, key, "EN", "18.499666", "73.865377", timeStamp, "10")
//            call.enqueue(object : Callback<FindHostelResponse> {
//                override fun onResponse(call: Call<FindHostelResponse>, response: Response<FindHostelResponse>?) {
//                    if (response != null && response.isSuccessful() && response.body() != null && response.body().result != null) {
//                        setMainLayout()
//                        setValues(response.body().result)
//                    } else {
//                        setNoDataLayout()
//                        val gson = GsonBuilder().create()
//                        val mError: NormalResponse
//                        try {
//                            mError = gson.fromJson(response!!.errorBody().string(), NormalResponse::class.java)
//                            Utils.showToast("" + mError.getMessage())
//                        } catch (e: IOException) {
//                            e.printStackTrace()
//                        }
//                    }
//                }
//
//                override fun onFailure(call: Call<FindHostelResponse>, t: Throwable) {
//                    setNoDataLayout()
//                }
//            })
//
//        } else {
//            setOfflineLayout()
//        }
//    }


    private fun setOfflineLayout() {
        viewFlipper!!.displayedChild = viewFlipper!!.indexOfChild(llForOfflineScreen)
    }

    private fun setLoadingLayout() {
        viewFlipper!!.displayedChild = viewFlipper!!.indexOfChild(rlForLoadingScreen)
    }

    private fun setMainLayout() {
        viewFlipper!!.displayedChild = viewFlipper!!.indexOfChild(recyclerView)
    }

    private fun setNoDataLayout() {
        viewFlipper!!.displayedChild = viewFlipper!!.indexOfChild(llForNoResult)
    }

}
