package com.pets.app.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import com.google.gson.GsonBuilder
import com.pets.app.R
import com.pets.app.adapters.ReviewsAdapter
import com.pets.app.common.AppPreferenceManager
import com.pets.app.common.ApplicationsConstants
import com.pets.app.common.Constants
import com.pets.app.common.Enums
import com.pets.app.initialsetup.BaseActivity
import com.pets.app.model.NormalResponse
import com.pets.app.model.Reviews
import com.pets.app.model.ReviewsResponse
import com.pets.app.model.request.WriteReview
import com.pets.app.utilities.TimeStamp
import com.pets.app.utilities.Utils
import com.pets.app.webservice.RestClient
import com.pets.app.webservice.WebServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class ReviewActivity : BaseActivity(), View.OnClickListener {

    private var adapter: ReviewsAdapter? = null
    private var listItems = ArrayList<Any>()
    private var layoutManager: LinearLayoutManager? = null

    private var viewFlipper: ViewFlipper? = null
    private var rlForLoadingScreen: RelativeLayout? = null
    private var recyclerView: RecyclerView? = null
    private var llForNoResult: LinearLayout? = null
    private var llForOfflineScreen: LinearLayout? = null
    private var tvNoResult: TextView? = null
    private var typeId: String? = null
    private var type: String? = null
    private var nextOffset = 0
    private var btnWriteReview: Button? = null
    val DEVICE_TYPE = "ANDROID"
    private var loading = true
    private var pastVisibleItems: Int = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0

    companion object {
        val TAG = ReviewActivity::class.java.simpleName
        fun startActivity(activity: Activity, hostelId: String, type: String) {
            val intent = Intent(activity, ReviewActivity::class.java)
            intent.putExtra(ApplicationsConstants.ID, hostelId)
            intent.putExtra(ApplicationsConstants.DATA, type)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
        initializeToolbar(getString(R.string.reviews))
        init()
        initView()
        setAdapter()
        getReview()

        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy > 0) {
                    //check for scroll down
                    if (listItems.size > 0) {
                        if (nextOffset != -1) {
                            visibleItemCount = layoutManager!!.childCount
                            totalItemCount = layoutManager!!.itemCount
                            pastVisibleItems = layoutManager!!.findFirstVisibleItemPosition()
                            if (loading) {
                                if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                                    loading = false
                                    getReview()
                                }
                            }
                        }
                    }
                }
            }
        })
    }

    private fun init() {
        typeId = intent.getStringExtra(ApplicationsConstants.ID)
        type = intent.getStringExtra(ApplicationsConstants.DATA)
    }

    private fun initView() {
        viewFlipper = findViewById(R.id.viewFlipper)
        rlForLoadingScreen = findViewById(R.id.rlForLoadingScreen)
        recyclerView = findViewById(R.id.recyclerView)
        llForNoResult = findViewById(R.id.llForNoResult)
        llForOfflineScreen = findViewById(R.id.llForOfflineScreen)
        btnRetry = findViewById(R.id.btnRetry)
        tvNoResult = findViewById(R.id.tvNoResult)
        btnWriteReview = findViewById(R.id.btnWriteReview)
        btnRetry?.setOnClickListener(this)
        btnWriteReview!!.setOnClickListener(this)
    }

    private fun setAdapter() {
        layoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
//        val snapHelper = LinearSnapHelper()
//        snapHelper.attachToRecyclerView(recyclerView)
        adapter = ReviewsAdapter(listItems, this)
        recyclerView!!.adapter = adapter
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnRetry -> {
                getReview()
            }

            R.id.btnWriteReview -> {
                WriteReviewActivity.startActivity(this, typeId!!, type!!, 0, Reviews())
            }
            R.id.ivDelete -> {
                val review = p0.tag as Reviews
                if (review != null) {
                    if (Utils.isOnline(this)) {
                        deleteReview(review)
                    } else {
                        Utils.showToast(getString(R.string.please_check_internet_connection))
                    }
                }
            }
            R.id.llReview -> {
                val review = p0.tag as Reviews
                if (review != null) {
                    WriteReviewActivity.startActivity(this, typeId!!, type!!, 1, review)
                }
            }
        }
    }

    private fun getReview() {
        setLoadingLayout()
        val timeStamp = TimeStamp.getTimeStamp()
        val language = Enums.Language.EN.name.toUpperCase()
        val userId = AppPreferenceManager.getUserID()

        val key = TimeStamp.getMd5(timeStamp + userId + typeId + Constants.TIME_STAMP_KEY)
        if (Utils.isOnline(this)) {
            val apiClient = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
            val call = apiClient.reviewsByType(key, language, nextOffset, timeStamp, type, typeId, userId)
            call.enqueue(object : Callback<ReviewsResponse> {
                override fun onResponse(call: Call<ReviewsResponse>, response: Response<ReviewsResponse>?) {
                    loading = true
                    if (response != null && response.isSuccessful && response.body() != null) {
                        nextOffset = response.body().next_offset
                        if (response.body().list != null) {
                            listItems.addAll(response.body().list)
                            adapter!!.notifyDataSetChanged()
                        }
                    } else {
                        val gson = GsonBuilder().create()
                        val mError: NormalResponse
                        try {
                            mError = gson.fromJson(response!!.errorBody().string(), NormalResponse::class.java)
                            Utils.showToast("" + mError.message)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                    if (listItems.size > 0) {
                        setMainLayout()
                    } else {
                        setNoDataLayout()
                    }
                }

                override fun onFailure(call: Call<ReviewsResponse>, t: Throwable) {
                    loading = true
                    setNoDataLayout()
                }
            })

        } else {
            setOfflineLayout()
        }
    }


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
        tvNoResult?.text = getString(R.string.no_review_found)
        viewFlipper!!.displayedChild = viewFlipper!!.indexOfChild(llForNoResult)
    }


    private fun deleteReview(review: Reviews) {
        val timeStamp = TimeStamp.getTimeStamp()
        val key = TimeStamp.getMd5(timeStamp + 10 + review.reviewId + Constants.TIME_STAMP_KEY)
        val userId = AppPreferenceManager.getUserID()

        val request = WriteReview()
        request.setUserId(userId)
        request.setTimestamp(timeStamp)
        request.setKey(key)
        request.setReviewId(review.reviewId)

        showProgressBar()
        val api = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
        val call = api.deleteReview(request)
        call.enqueue(object : Callback<NormalResponse> {
            override fun onResponse(call: Call<NormalResponse>?, response: Response<NormalResponse>?) {
                hideProgressBar()
                if (response != null) {
                    if (response.body() != null && response.isSuccessful) {
                        var pos = listItems.indexOf(review as Any)
                        listItems.remove(review)
                        adapter!!.notifyItemRemoved(pos)
                    } else if (response.code() == 403) {
                        val gson = GsonBuilder().create()
                        val mError: NormalResponse
                        try {
                            mError = gson.fromJson(response.errorBody().string(), NormalResponse::class.java)
                            Utils.showToast("" + mError.message)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<NormalResponse>?, t: Throwable?) {
                hideProgressBar()
            }
        })
    }
}
