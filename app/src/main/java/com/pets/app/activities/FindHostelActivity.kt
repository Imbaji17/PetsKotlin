package com.pets.app.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewFlipper
import com.pets.app.R
import com.pets.app.adapters.FindHostelAdapter
import com.pets.app.common.AppPreferenceManager
import com.pets.app.common.ApplicationsConstants
import com.pets.app.common.Constants
import com.pets.app.common.Enums
import com.pets.app.initialsetup.BaseActivity
import com.pets.app.model.FindHostel
import com.pets.app.model.FindHostelResponse
import com.pets.app.model.NormalResponse
import com.pets.app.model.request.FavouriteHostel
import com.pets.app.utilities.GridSpacingItemDecoration
import com.pets.app.utilities.TimeStamp
import com.pets.app.utilities.Utils
import com.pets.app.webservice.RestClient
import com.pets.app.webservice.WebServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class FindHostelActivity : BaseActivity(), View.OnClickListener, TextView.OnEditorActionListener, TextWatcher {

    private var layoutManager: LinearLayoutManager? = null
    private var adapter: FindHostelAdapter? = null
    private var listItems = ArrayList<Any>()
    private var recyclerView: RecyclerView? = null
    private var viewFlipper: ViewFlipper? = null
    private var nextOffset = 0
    private var gridLayoutManager: GridLayoutManager? = null
    private var latitude: Double? = 0.0
    private var longitude: Double? = 0.0
    private var keyWord: String? = ""
    private val RC_MAP_ACTIVITY: Int = 2
    private var edtSearch: EditText? = null
    private var imgClear: ImageView? = null
    private var findHostel: FindHostel? = null
    private var isInterested: String? = ""

    companion object {
        private val TAG = FindHostelActivity::class.java.simpleName
        fun startActivity(activity: Activity, isInterested: String) {
            val intent = Intent(activity, FindHostelActivity::class.java)
            intent.putExtra(ApplicationsConstants.INTERESTED, isInterested)
            activity.startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_hostel)
        initializeToolbar(getString(R.string.find_hostel))
        init()
        initView()
        setAdapter()
        getHostelList()
    }

    private fun init() {
        isInterested = intent.getStringExtra(ApplicationsConstants.INTERESTED)
    }

    private fun initView() {
        recyclerView = findViewById(R.id.recyclerView)
        viewFlipper = findViewById(R.id.viewFlipper)
        edtSearch = findViewById(R.id.edtSearch)
        imgClear = findViewById(R.id.imgClear)
        edtSearch?.setOnEditorActionListener(this)
        edtSearch?.addTextChangedListener(this)
        imgClear?.setOnClickListener(this)
    }

    private fun setAdapter() {
        val spanCount = 2 // 3 columns
        val spacing = 10 // 50px
        val includeEdge = true
        recyclerView!!.addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, includeEdge))
        gridLayoutManager = GridLayoutManager(this, spanCount)
        recyclerView!!.layoutManager = this.gridLayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
//        val snapHelper = LinearSnapHelper()
//        snapHelper.attachToRecyclerView(recyclerView)
        adapter = FindHostelAdapter(listItems, this)
        recyclerView!!.adapter = adapter
    }

    private fun getHostelList() {
        val timeStamp = TimeStamp.getTimeStamp()
        val userId = AppPreferenceManager.getUserID()
        val key = TimeStamp.getMd5(timeStamp + userId + Constants.TIME_STAMP_KEY)
        val language = Enums.Language.EN.name.toUpperCase()
        val lat = AppPreferenceManager.getUser().lat
        val lng = AppPreferenceManager.getUser().lng


        viewFlipper!!.displayedChild = 0
        if (Utils.isOnline(this)) {
            val apiClient = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
            val call = apiClient.hostelList(key, keyWord, language, lat, lng, nextOffset, timeStamp, userId, isInterested)
            call.enqueue(object : Callback<FindHostelResponse> {
                override fun onResponse(call: Call<FindHostelResponse>, response: Response<FindHostelResponse>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body() != null && response.body().list != null) {
                                listItems.addAll(response.body().list)
                                adapter!!.notifyDataSetChanged()
                            }
                        } else {
//                            Utils.showErrorToast(response.errorBody())
                        }
                    }
                    setNoResult()
                }

                override fun onFailure(call: Call<FindHostelResponse>, t: Throwable) {
                    setNoResult()
                }
            })

        } else {
            viewFlipper!!.displayedChild = 3
        }
    }

    fun setNoResult() {
        if (listItems.size > 0) {
            viewFlipper!!.displayedChild = 1
        } else {
            viewFlipper!!.displayedChild = 2
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.llFindHostel -> {
                findHostel = view.tag as FindHostel
                if (findHostel != null) {
                    HostelDetailActivity.startActivity(this, findHostel!!.hostelId)
                }
            }

            R.id.ivFavourite -> {
                findHostel = view.tag as FindHostel
                if (findHostel != null) {
                    favourite()
                }
            }
            R.id.imgClear -> {
                edtSearch?.setText("")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_find_hostel, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        when (id) {
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.action_map -> {
                FindHostelMapActivity.startActivity(this, listItems, RC_MAP_ACTIVITY, isInterested!!)
            }

            R.id.action_search -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_MAP_ACTIVITY -> if (resultCode == Activity.RESULT_OK) {
                latitude = data!!.getDoubleExtra(ApplicationsConstants.LATITUDE, 0.0)
                longitude = data.getDoubleExtra(ApplicationsConstants.LONGITUDE, 0.0)
                keyWord = data.getStringExtra(ApplicationsConstants.NAME)
                if (!TextUtils.isEmpty(keyWord)) {
                    edtSearch!!.setText(keyWord)
                }
                listItems.clear()
            }
        }
    }


    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            keyWord = edtSearch!!.text.toString()
            nextOffset = 0
            listItems.clear()
            getHostelList()
            return true
        }
        return false
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (p0?.length!! > 0) {
            imgClear?.visibility = View.VISIBLE
        } else {
            imgClear?.visibility = View.GONE
        }
    }


    private fun favourite() {
        val timeStamp = TimeStamp.getTimeStamp()
        val userId = AppPreferenceManager.getUserID()
        val key = TimeStamp.getMd5(timeStamp + userId + Enums.Favourite.HOSTEL.name + findHostel?.hostelId + Constants.TIME_STAMP_KEY)
        val request = FavouriteHostel()

        request.setUserId(userId)
        request.setTimestamp(timeStamp)
        request.setType(Enums.Favourite.HOSTEL.name)
        request.setTypeId(findHostel?.hostelId)
        request.setKey(key)

        showProgressBar()
        val api = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
        val call = api.favourite(request)
        call.enqueue(object : Callback<NormalResponse> {
            override fun onResponse(call: Call<NormalResponse>?, response: Response<NormalResponse>?) {
                hideProgressBar()
                if (response != null) {
                    if (response.body() != null && response.isSuccessful) {
                        var pos = listItems.indexOf(findHostel as Any)
                        findHostel!!.isInterest = !findHostel!!.isInterest
                        adapter!!.notifyItemChanged(pos)
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
