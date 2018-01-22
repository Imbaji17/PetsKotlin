package com.pets.app.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.pets.app.R
import com.pets.app.adapters.FunZoneCommentAdapter
import com.pets.app.common.*
import com.pets.app.initialsetup.BaseActivity
import com.pets.app.model.FunZone
import com.pets.app.model.FunZoneCommentResponse
import com.pets.app.utilities.DateFormatter
import com.pets.app.utilities.TimeStamp
import com.pets.app.utilities.Utils
import com.pets.app.webservice.RestClient
import com.pets.app.webservice.WebServiceBuilder
import com.pets.app.widgets.SimpleDividerItemDecoration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class FunZoneCommentActivity : BaseActivity(), View.OnClickListener {


    private var ivProfile: ImageView? = null
    private var tvName: TextView? = null
    private var tvDate: TextView? = null
    private var ivFunZone: ImageView? = null
    private var tvHelpful: TextView? = null
    private var tvComment: TextView? = null
    private var tvShare: TextView? = null
    private var recyclerView: RecyclerView? = null
    private var etComment: EditText? = null
    private var ivSend: ImageView? = null
    private var funZone: FunZone? = null
    private var nsv: NestedScrollView? = null

    private var adapter: FunZoneCommentAdapter? = null
    private var listItems = ArrayList<Any>()
    private var layoutManager: LinearLayoutManager? = null
    private var loading = true
    private var nextOffset = 0

    companion object {
        private val TAG = FunZoneCommentActivity::class.java.simpleName
        fun startActivity(activity: Activity, funZone: FunZone) {
            val intent = Intent(activity, FunZoneCommentActivity::class.java)
            intent.putExtra(ApplicationsConstants.DATA, funZone)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fun_zone_comment)
        init()
        initViews()
        setValues()
        setAdapter()
        getFunZoneComment()

        nsv!!.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {

                var visibleItemCount = 0;
                var totalItemCount = 0
                var pastVisibleItems = 0

                if (v!!.getChildAt(v!!.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1)
                            .getMeasuredHeight() - v.getMeasuredHeight())) &&
                            scrollY > oldScrollY) {

                        visibleItemCount = layoutManager!!.getChildCount();
                        totalItemCount = layoutManager!!.getItemCount();
                        pastVisibleItems = layoutManager!!.findFirstVisibleItemPosition();

                        if (loading) {
                            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                loading = false;
                                if (nextOffset != -1) {
                                    getFunZoneComment()
                                }
                            }
                        }
                    }
                }


            }
        })
    }

    fun init() {
        funZone = intent.getSerializableExtra(ApplicationsConstants.DATA) as FunZone?
    }

    private fun initViews() {
        ivProfile = findViewById(R.id.ivProfile)
        tvName = findViewById(R.id.tvName)
        tvDate = findViewById(R.id.tvDate)
        ivFunZone = findViewById(R.id.ivFunZone)
        tvHelpful = findViewById(R.id.tvHelpful)
        tvComment = findViewById(R.id.tvComment)
        tvShare = findViewById(R.id.tvShare)
        recyclerView = findViewById(R.id.recyclerView)
        etComment = findViewById(R.id.etComment)
        ivSend = findViewById(R.id.ivSend)
        nsv = findViewById(R.id.nsv)
        ivSend!!.setOnClickListener(this)
    }

    private fun setValues() {
        if (funZone!!.user != null) {
            if (!TextUtils.isEmpty(funZone!!.user.name)) {
                tvName!!.text = funZone!!.user.name
                initializeToolbar(funZone!!.user.name)
            } else {
                tvName!!.text = ""
                initializeToolbar("")
            }
            ImageSetter.loadRoundedImage(this, funZone!!.user.profile_image, R.drawable.alert_placeholder, ivProfile)
        }

        tvDate!!.text = DateFormatter.getFormattedDate(DateFormatter.yyyy_mm_dd_HH_mm_ss, funZone!!.createdDate, DateFormatter.REVIEW_DATE_FORMAT)
        ImageSetter.loadImage(this, funZone!!.funZoneImage, R.drawable.alert_placeholder, ivFunZone)
        if (!TextUtils.isEmpty(funZone!!.type) && funZone!!.type == Enums.FunZoneType.VIDEO.name) {
            ImageSetter.loadImage(this, funZone!!.videoThumb, R.drawable.alert_placeholder, ivFunZone)
        } else {
            ImageSetter.loadImage(this, funZone!!.funZoneImage, R.drawable.alert_placeholder, ivFunZone)
        }
    }

    private fun setAdapter() {
        layoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.addItemDecoration(SimpleDividerItemDecoration(this))
        adapter = FunZoneCommentAdapter(listItems, this)
        recyclerView!!.adapter = adapter
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.ivSend -> {

            }
        }
    }

    private fun getFunZoneComment() {

        val timeStamp = TimeStamp.getTimeStamp()
        val language = Enums.Language.EN.name.toUpperCase()
        val userId = AppPreferenceManager.getUserID()
        val key = TimeStamp.getMd5(timeStamp + userId + funZone!!.funZoneId + Constants.TIME_STAMP_KEY)

//        @Query("user_id") String user_id, @Query("timestamp") String timeStamp, @Query("key") String key,
//        @Query("language_code") String languageCode, @Query("next_offset") int nextOffset,
//        @Query("fun_zone_id") String fun_zone_id

        if (Utils.isOnline(this)) {
            val apiClient = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
            val call = apiClient.getFunZoneCommentList(userId, timeStamp, key, language, nextOffset, funZone!!.funZoneId)
            call.enqueue(object : Callback<FunZoneCommentResponse> {
                override fun onResponse(call: Call<FunZoneCommentResponse>, response: Response<FunZoneCommentResponse>?) {
                    loading = true
                    if (response != null && response.isSuccessful() && response.body() != null) {
                        nextOffset = response.body().nextOffset
                        if (response.body().list != null) {
                            listItems.addAll(response.body().list)
                            adapter!!.notifyDataSetChanged()
                        }
                    } else {
                        Utils.showErrorToast(response?.errorBody())
                    }
                    if (listItems.size > 0) {
                    } else {
                    }
                }

                override fun onFailure(call: Call<FunZoneCommentResponse>, t: Throwable) {
                    loading = true
                }
            })
        } else {
            Utils.showToast(getString(R.string.please_check_internet_connection))
        }
    }

//    private fun addComment() {
//
//        val timeStamp = TimeStamp.getTimeStamp()
//        val userId = AppPreferenceManager.getUserID()
//        val key = TimeStamp.getMd5(timeStamp + userId + Enums.Favourite.ADOPTION.name + adoption?.adoptionId + Constants.TIME_STAMP_KEY)
//        val request = FavouriteHostel()
//
//        request.setUserId(userId)
//        request.setTimestamp(timeStamp)
//        request.setType(Enums.Favourite.ADOPTION.name)
//        request.setTypeId(adoption?.adoptionId)
//        request.setKey(key)
//
//        showProgressBar()
//        val api = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
//        val call = api.favourite(request)
//        call.enqueue(object : Callback<NormalResponse> {
//            override fun onResponse(call: Call<NormalResponse>?, response: Response<NormalResponse>?) {
//                hideProgressBar()
//                if (response != null) {
//                    if (response.body() != null && response.isSuccessful) {
//                        var pos = listItems.indexOf(adoption as Any)
//                        adoption!!.isInterest = !adoption!!.isInterest
//                        adapter!!.notifyItemChanged(pos)
//                    } else {
//                        Utils.showErrorToast(response.errorBody())
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<NormalResponse>?, t: Throwable?) {
//                hideProgressBar()
//            }
//        })
//
//    }

}
