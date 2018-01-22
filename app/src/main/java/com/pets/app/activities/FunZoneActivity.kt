package com.pets.app.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.google.gson.GsonBuilder
import com.pets.app.R
import com.pets.app.adapters.FunZoneAdapter
import com.pets.app.common.AppPreferenceManager
import com.pets.app.common.Constants
import com.pets.app.common.Enums
import com.pets.app.initialsetup.BaseActivity
import com.pets.app.model.FunZone
import com.pets.app.model.FunZoneResponse
import com.pets.app.model.NormalResponse
import com.pets.app.model.request.FavouriteHostel
import com.pets.app.utilities.TimeStamp
import com.pets.app.utilities.Utils
import com.pets.app.webservice.RestClient
import com.pets.app.webservice.WebServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class FunZoneActivity : BaseActivity(), View.OnClickListener {

    private var viewFlipper: ViewFlipper? = null
    private var rlForLoadingScreen: RelativeLayout? = null
    private var recyclerView: RecyclerView? = null
    private var llForNoResult: LinearLayout? = null
    private var llForOfflineScreen: LinearLayout? = null
    private var tvNoResult: TextView? = null
    private var btnRetry: Button? = null

    private var adapter: FunZoneAdapter? = null
    private var listItems = ArrayList<Any>()
    private var layoutManager: LinearLayoutManager? = null

    private var nextOffset = 0
    private var loading = true
    private val RC_POST: Int = 2

    companion object {
        private val TAG = FunZoneActivity::class.java.simpleName
        fun startActivity(activity: Activity) {
            val intent = Intent(activity, FunZoneActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fun_zone)
        initializeToolbar(getString(R.string.fun_zone))
        initView()
        setAdapter()
        getFunZone()
    }

    private fun initView() {
        viewFlipper = findViewById(R.id.viewFlipper)
        rlForLoadingScreen = findViewById(R.id.rlForLoadingScreen)
        recyclerView = findViewById(R.id.recyclerView)
        llForNoResult = findViewById(R.id.llForNoResult)
        llForOfflineScreen = findViewById(R.id.llForOfflineScreen)
        btnRetry = findViewById(R.id.btnRetry)
        tvNoResult = findViewById(R.id.tvNoResult)
        btnRetry?.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.ivMore -> {
                val funZone = p0.tag as FunZone
                showMenu(p0, funZone)
            }

            R.id.ivPlay -> {
                val funZone = p0.tag as FunZone
                if (funZone != null && !TextUtils.isEmpty(funZone.funZoneImage))
                    VideoViewActivity.startActivity(this, funZone.funZoneImage)
            }
            R.id.tvComment -> {
                val funZone = p0.tag as FunZone
                if (funZone != null)
                    FunZoneCommentActivity.startActivity(this, funZone)
            }


        }
    }

    @SuppressLint("NewApi")
    private fun showMenu(view: View, funZone: FunZone) {
        val menu = PopupMenu(this, view, Gravity.RIGHT)
        menu.menuInflater.inflate(R.menu.menu_funzone, menu.menu)
        menu.show()
        menu.setOnMenuItemClickListener { item ->
            when (item.itemId) {

                R.id.action_cancel -> {
                }

                R.id.action_edit -> {
                    AddFunZoneActivity.startActivity(this, RC_POST, 1, funZone)
                }

                R.id.action_delete -> {
                    if (Utils.isOnline(this)) {
                        deleteFunZone(funZone)
                    } else {
                        Utils.showToast(getString(R.string.please_check_internet_connection))
                    }
                }
            }
            false
        }
    }


    private fun setAdapter() {
        layoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
//        val snapHelper = LinearSnapHelper()
//        snapHelper.attachToRecyclerView(recyclerView)
        adapter = FunZoneAdapter(listItems, this)
        recyclerView!!.adapter = adapter
    }

    private fun getFunZone() {
        setLoadingLayout()
        val timeStamp = TimeStamp.getTimeStamp()
        val language = Enums.Language.EN.name.toUpperCase()
        val userId = AppPreferenceManager.getUserID()
        val key = TimeStamp.getMd5(timeStamp + userId + Constants.TIME_STAMP_KEY)
        val lat = AppPreferenceManager.getUser().lat
        val lng = AppPreferenceManager.getUser().lng

//        @Query("user_id") String user_id, @Query("timestamp") String timeStamp, @Query("key") String key,
//        @Query("language_code") String languageCode, @Query("next_offset") int nextOffset,
//        @Query("lat") String lat, @Query("lng") String lng

        if (Utils.isOnline(this)) {
            val apiClient = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
            val call = apiClient.getFunZoneList(userId, timeStamp, key, language, nextOffset, lat, lng)
            call.enqueue(object : Callback<FunZoneResponse> {
                override fun onResponse(call: Call<FunZoneResponse>, response: Response<FunZoneResponse>?) {
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
                        setMainLayout()
                    } else {
                        setNoDataLayout()
                    }
                }

                override fun onFailure(call: Call<FunZoneResponse>, t: Throwable) {
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
        tvNoResult?.text = getString(R.string.no_result_found)
        viewFlipper!!.displayedChild = viewFlipper!!.indexOfChild(llForNoResult)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_post, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        when (id) {
            R.id.action_post -> {
                AddFunZoneActivity.startActivity(this, RC_POST, 0, null)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun deleteFunZone(funZone: FunZone) {
        val timeStamp = TimeStamp.getTimeStamp()
        val userId = AppPreferenceManager.getUserID()
        val key = TimeStamp.getMd5(timeStamp + userId + funZone?.funZoneId + Constants.TIME_STAMP_KEY)
        val request = FunZone()

        request.setUserId(userId)
        request.setTimestamp(timeStamp)
        request.setKey(key)
        request.funZoneId = funZone?.funZoneId

        showProgressBar()
        val api = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
        val call = api.funZoneDelete(request)
        call.enqueue(object : Callback<NormalResponse> {
            override fun onResponse(call: Call<NormalResponse>?, response: Response<NormalResponse>?) {
                hideProgressBar()
                if (response != null) {
                    if (response.body() != null && response.isSuccessful) {
                        var pos = listItems.indexOf(funZone as Any)
                        listItems.remove(funZone)
                        adapter!!.notifyItemRemoved(pos)
                        if (listItems.size == 0) {
                            setNoDataLayout()
                        }
                    } else {
                        Utils.showErrorToast(response.errorBody())
                    }
                }
            }

            override fun onFailure(call: Call<NormalResponse>?, t: Throwable?) {
                hideProgressBar()
            }
        })
    }


}
