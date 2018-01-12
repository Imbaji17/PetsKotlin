package com.pets.app.activities.adoption

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
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
import com.google.gson.GsonBuilder
import com.pets.app.R
import com.pets.app.activities.HostelDetailActivity
import com.pets.app.adapters.AdoptionListAdapter
import com.pets.app.common.AppPreferenceManager
import com.pets.app.common.ApplicationsConstants
import com.pets.app.common.Constants
import com.pets.app.common.Enums
import com.pets.app.initialsetup.BaseActivity
import com.pets.app.model.Adoption
import com.pets.app.model.AdoptionResponse
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
import java.io.IOException
import java.util.*

class AdoptionListActivity : BaseActivity(), View.OnClickListener, TextView.OnEditorActionListener, TextWatcher {

    private var adapter: AdoptionListAdapter? = null
    private var listItems = ArrayList<Any>()
    private var recyclerView: RecyclerView? = null
    private var viewFlipper: ViewFlipper? = null
    private var nextOffset = 0
    private var gridLayoutManager: GridLayoutManager? = null
    private var latitude: Double? = 0.0
    private var longitude: Double? = 0.0

    private val RC_MAP_ACTIVITY: Int = 2
    private var edtSearch: EditText? = null
    private var imgClear: ImageView? = null
    private var adoption: Adoption? = null

    private var petsTypeId: String? = ""
    private var breedId: String? = ""
    private var gender: String? = ""
    private var distance: String? = ""

    companion object {
        private val TAG = AdoptionListActivity::class.java.simpleName
        fun startActivity(activity: Activity) {
            val intent = Intent(activity, AdoptionListActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_hostel)
        initializeToolbar(getString(R.string.find_hostel))
        initView()
        setAdapter()
        getAdoptionList()
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
        recyclerView!!.layoutManager = this!!.gridLayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        adapter = AdoptionListAdapter(listItems, this)
        recyclerView!!.adapter = adapter
    }

    private fun getAdoptionList() {
        val userId = AppPreferenceManager.getUserID()
        val timeStamp = TimeStamp.getTimeStamp()
        val key = TimeStamp.getMd5(timeStamp + userId + Constants.TIME_STAMP_KEY)
        val language = Enums.Language.EN.name.toUpperCase()


//        @Query("user_id") String user_id, @Query("timestamp") String timestamp, @Query("key") String key,
//        @Query("language_code") String languageCode, @Query("next_offset") int next_offset, @Query("lat") String lat,
//        @Query("lng") String lng, @Query("pets_type_id") String petsTypeId, @Query("breed_id") String breedId,
//        @Query("gender") String gender, @Query("distance") String distance

        viewFlipper!!.displayedChild = 0
        if (Utils.isOnline(this)) {
            val apiClient = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
            val call = apiClient.adoptionList(userId, timeStamp, key, language, nextOffset, latitude.toString(), longitude.toString(),
                    petsTypeId, breedId, gender, distance)
            call.enqueue(object : Callback<AdoptionResponse> {
                override fun onResponse(call: Call<AdoptionResponse>, response: Response<AdoptionResponse>?) {
                    if (response != null) {
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().list != null) {
                                listItems.addAll(response.body().list);
                                adapter!!.notifyDataSetChanged()
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
                    setNoResult()
                }

                override fun onFailure(call: Call<AdoptionResponse>, t: Throwable) {
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
                adoption = view.tag as Adoption
                if (adoption != null) {
                    HostelDetailActivity.startActivity(this, adoption!!.adoptionId)
                }
            }

            R.id.ivFavourite -> {
                adoption = view.tag as Adoption
                if (adoption != null) {
                    favourite()
                }
            }
            R.id.imgClear -> {
                edtSearch?.setText("")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_adoption, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        when (id) {
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.action_add -> {
//                FindHostelMapActivity.startActivity(this, listItems, RC_MAP_ACTIVITY)
            }

            R.id.action_filter -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_MAP_ACTIVITY -> if (resultCode == Activity.RESULT_OK) {
                latitude = data!!.getDoubleExtra(ApplicationsConstants.LATITUDE, 0.0);
                longitude = data!!.getDoubleExtra(ApplicationsConstants.LONGITUDE, 0.0);
//                keyWord = data.getStringExtra(ApplicationsConstants.NAME)
//                if (!TextUtils.isEmpty(keyWord)) {
//                    edtSearch!!.setText(keyWord)
//                }
//                listItems.clear()
            }
        }
    }


    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//            keyWord = edtSearch!!.text.toString()
            nextOffset = 0
            listItems.clear()
            getAdoptionList();
            return true;
        }
        return false;
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
        val key = TimeStamp.getMd5(timeStamp + 10 + Enums.Favourite.ADOPTION.name + adoption?.adoptionId + Constants.TIME_STAMP_KEY)
        val request = FavouriteHostel()
        val userId = AppPreferenceManager.getUserID()
        request.setUserId(userId)
        request.setTimestamp(timeStamp)
        request.setType(Enums.Favourite.ADOPTION.name)
        request.setTypeId(adoption?.adoptionId)
        request.setKey(key)

        showProgressBar()
        val api = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
        val call = api.favourite(request)
        call.enqueue(object : Callback<NormalResponse> {
            override fun onResponse(call: Call<NormalResponse>?, response: Response<NormalResponse>?) {
                hideProgressBar()
                if (response != null) {
                    if (response.body() != null && response.isSuccessful()) {
                        var pos = listItems.indexOf(adoption as Any)
                        adoption!!.isInterest = !adoption!!.isInterest
                        adapter!!.notifyItemChanged(pos)
                    } else if (response.code() == 403) {
                        val gson = GsonBuilder().create()
                        val mError: NormalResponse
                        try {
                            mError = gson.fromJson(response.errorBody().string(), NormalResponse::class.java)
                            Utils.showToast("" + mError.getMessage())
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