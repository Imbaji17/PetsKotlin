package com.pets.app.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import com.google.gson.GsonBuilder
import com.pets.app.R
import com.pets.app.common.ApplicationsConstants
import com.pets.app.common.Constants
import com.pets.app.common.Enums
import com.pets.app.initialsetup.BaseActivity
import com.pets.app.model.NormalResponse
import com.pets.app.model.ReviewsResponse
import com.pets.app.model.request.FavouriteHostel
import com.pets.app.model.request.WriteReview
import com.pets.app.utilities.TimeStamp
import com.pets.app.utilities.Utils
import com.pets.app.webservice.RestClient
import com.pets.app.webservice.WebServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class WriteReviewActivity : BaseActivity(), View.OnClickListener {

    private var ratingBar: RatingBar? = null
    private var etReview: EditText? = null
    private var btnSubmit: Button? = null
    private var id: String? = null
    private var type: String? = null

    companion object {
        private val TAG = WriteReviewActivity::class.java.simpleName
        fun startActivity(activity: Activity, id: String, type: String) {
            val intent = Intent(activity, WriteReviewActivity::class.java)
            intent.putExtra(ApplicationsConstants.ID, id)
            intent.putExtra(ApplicationsConstants.DATA, type)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_review)
        initializeToolbar(getString(R.string.write_reviews))
    }

    init {
        id = intent.getStringExtra(ApplicationsConstants.ID)
        type = intent.getStringExtra(ApplicationsConstants.DATA)
    }

    fun init() {
        ratingBar = findViewById(R.id.ratingBar)
        etReview = findViewById(R.id.etReview)
        btnSubmit = findViewById(R.id.btnSubmit)
        btnSubmit!!.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.btnSubmit -> {

            }
        }
    }


//    private fun writeReview() {
//
//        val comment = etReview?.text.toString().trim()
//        val rating = ratingBar!!.rating
//
//        if (TextUtils.isEmpty(comment)){
//            Utils.showToast(getString(R.string.please_write_your_review))
//        } else if(rating eqa 0)
//
//        val timeStamp = TimeStamp.getTimeStamp()
//        val key = TimeStamp.getMd5(timeStamp + 10 + id + Constants.TIME_STAMP_KEY)
//        val request = WriteReview()
//        request.setUserId("10")
//        request.setKey(key)
//        request.setTimestamp(timeStamp)
//        request.setType(type)
//        request.setTypeId(id)
//        request.setComment()
//        request.setRating()
////        request.setReviewId()
//
//        showProgressBar()
//        val api = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
//        val call = api.writeReview(request)
//        call.enqueue(object : Callback<NormalResponse> {
//            override fun onResponse(call: Call<NormalResponse>?, response: Response<NormalResponse>?) {
//                hideProgressBar()
//                if (response != null) {
//                    if (response.body() != null && response.isSuccessful()) {
//                        var pos = listItems.indexOf(findHostel as Any)
//                        findHostel!!.isInterest = !findHostel!!.isInterest
//                        adapter!!.notifyItemChanged(pos)
//                    } else if (response.code() == 403) {
//                        val gson = GsonBuilder().create()
//                        val mError: NormalResponse
//                        try {
//                            mError = gson.fromJson(response.errorBody().string(), NormalResponse::class.java)
//                            Utils.showToast("" + mError.getMessage())
//                        } catch (e: IOException) {
//                            e.printStackTrace()
//                        }
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<NormalResponse>?, t: Throwable?) {
//                hideProgressBar()
//            }
//        })
//    }
}
