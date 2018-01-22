package com.pets.app.activities.adoption

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.google.gson.GsonBuilder
import com.pets.app.R
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

class AdoptionListActivity : BaseActivity(), View.OnClickListener {

    private var adapter: AdoptionListAdapter? = null
    private var listItems = ArrayList<Any>()
    private var recyclerView: RecyclerView? = null
    private var nextOffset = 0
    private var gridLayoutManager: GridLayoutManager? = null

    private val RC_FILTER: Int = 2
    private val RC_ADD_ADOPTION: Int = 3
    private var adoption: Adoption? = null

    private var petsTypeId: String? = ""
    private var petsTypeStr: String? = ""
    private var breedId: String? = ""
    private var breedStr: String? = ""
    private var gender: String? = ""
    private var distance: String? = ""
    private var latitude: Double? = 0.0
    private var longitude: Double? = 0.0
    private var location: String? = ""

    private var viewFlipper: ViewFlipper? = null
    private var rlForLoadingScreen: RelativeLayout? = null
    private var llForNoResult: LinearLayout? = null
    private var llForOfflineScreen: LinearLayout? = null
    private var tvNoResult: TextView? = null
    private var btnRetry: Button? = null
    private var llLoadMore: LinearLayout? = null
    private var llForRecyclerView: LinearLayout? = null

    private var loading = true
    private var pastVisibleItems: Int = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0

    companion object {
        private val TAG = AdoptionListActivity::class.java.simpleName
        fun startActivity(activity: Activity) {
            val intent = Intent(activity, AdoptionListActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adoption)
        initializeToolbar(getString(R.string.adoption))
        initView()
        setAdapter()
        getAdoptionList()

        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy > 0) {
                    //check for scroll down
                    if (listItems != null && listItems.size > 0) {
                        if (nextOffset != -1) {
                            visibleItemCount = gridLayoutManager!!.childCount
                            totalItemCount = gridLayoutManager!!.itemCount
                            pastVisibleItems = gridLayoutManager!!.findFirstVisibleItemPosition()
                            if (loading) {
                                if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                                    loading = false
                                    getAdoptionList()
                                }
                            }
                        }
                    }
                }
            }
        })

    }

    private fun initView() {
        recyclerView = findViewById(R.id.recyclerView)
        viewFlipper = findViewById(R.id.viewFlipper)

        rlForLoadingScreen = findViewById(R.id.rlForLoadingScreen)
        llForRecyclerView = findViewById(R.id.llForRecyclerView)
        llForNoResult = findViewById(R.id.llForNoResult)
        llForOfflineScreen = findViewById(R.id.llForOfflineScreen)
        btnRetry = findViewById(R.id.btnRetry)
        tvNoResult = findViewById(R.id.tvNoResult)
        recyclerView = findViewById(R.id.recyclerView)
        llLoadMore = findViewById(R.id.llLoadMore)

        btnRetry!!.setOnClickListener(this)
    }

    private fun setAdapter() {
        val spanCount = 2 // 3 columns
        val spacing = 10 // 50px
        val includeEdge = true
        recyclerView!!.addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, includeEdge))
        gridLayoutManager = GridLayoutManager(this, spanCount)
        recyclerView!!.layoutManager = this.gridLayoutManager
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

        setLoadingLayout()
        if (Utils.isOnline(this)) {
            val apiClient = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
            val call = apiClient.adoptionList(userId, timeStamp, key, language, nextOffset, latitude.toString(), longitude.toString(),
                    petsTypeId, breedId, gender, distance!!)
            call.enqueue(object : Callback<AdoptionResponse> {
                override fun onResponse(call: Call<AdoptionResponse>, response: Response<AdoptionResponse>?) {
                    loading = true
                    if (response != null) {
                        if (response.isSuccessful) {
                            nextOffset = response.body().nextOffset
                            if (response.body() != null && response.body().list != null) {
                                listItems.addAll(response.body().list)
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
                    loading = true
                    setNoResult()
                }
            })

        } else {
            setOfflineLayout()
        }
    }

    fun setNoResult() {
        if (listItems.size > 0) {
            setMainLayout()
        } else {
            setNoDataLayout()
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.llFindHostel -> {
                adoption = view.tag as Adoption
                if (adoption != null) {
                    AdoptionDetailsActivity.startActivity(this, adoption!!.adoptionId)
                }
            }

            R.id.ivFavourite -> {
                adoption = view.tag as Adoption
                if (adoption != null) {
                    favourite()
                }
            }

            R.id.btnRetry -> {
                getAdoptionList()
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
                AddAdoptionActivity.startActivity(this, RC_ADD_ADOPTION)
            }

            R.id.action_filter -> {
                FilterAdoptionActivity.startActivity(this, RC_FILTER, petsTypeId!!, petsTypeStr!!, breedId!!, breedStr!!, gender!!, distance!!, latitude!!, longitude!!, location!!)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_FILTER -> if (resultCode == Activity.RESULT_OK) {
                latitude = data!!.getDoubleExtra(ApplicationsConstants.LATITUDE, 0.0)
                longitude = data.getDoubleExtra(ApplicationsConstants.LONGITUDE, 0.0)
                petsTypeId = data.getStringExtra(ApplicationsConstants.PETS_TYPE_ID)
                petsTypeStr = data.getStringExtra(ApplicationsConstants.PETS_TYPE_NAME)
                breedId = data.getStringExtra(ApplicationsConstants.BREED_ID)
                breedStr = data.getStringExtra(ApplicationsConstants.BREED_NAME)
                gender = data.getStringExtra(ApplicationsConstants.GENDER)
                distance = data.getStringExtra(ApplicationsConstants.DISTANCE)
                latitude = data.getDoubleExtra(ApplicationsConstants.LATITUDE, 0.0)
                longitude = data.getDoubleExtra(ApplicationsConstants.LONGITUDE, 0.0)
                location = data.getStringExtra(ApplicationsConstants.LOCATION)
                listItems.clear()
                adapter!!.notifyDataSetChanged()
                nextOffset = 0
                getAdoptionList()
            }

            RC_ADD_ADOPTION -> if (resultCode == Activity.RESULT_OK) {
                listItems.clear()
                adapter!!.notifyDataSetChanged()
                nextOffset = 0
                getAdoptionList()
            }
        }
    }

    private fun favourite() {
        val timeStamp = TimeStamp.getTimeStamp()
        val userId = AppPreferenceManager.getUserID()
        val key = TimeStamp.getMd5(timeStamp + userId + Enums.Favourite.ADOPTION.name + adoption?.adoptionId + Constants.TIME_STAMP_KEY)
        val request = FavouriteHostel()

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
                    if (response.body() != null && response.isSuccessful) {
                        var pos = listItems.indexOf(adoption as Any)
                        adoption!!.isInterest = !adoption!!.isInterest
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

    private fun setOfflineLayout() {
        viewFlipper!!.displayedChild = viewFlipper!!.indexOfChild(llForOfflineScreen)
    }

    private fun setLoadingLayout() {
        viewFlipper!!.displayedChild = viewFlipper!!.indexOfChild(rlForLoadingScreen)
    }

    private fun setMainLayout() {
        viewFlipper!!.displayedChild = viewFlipper!!.indexOfChild(llForRecyclerView)
    }

    private fun setNoDataLayout() {
        tvNoResult?.text = getString(R.string.no_result_found)
        viewFlipper!!.displayedChild = viewFlipper!!.indexOfChild(llForNoResult)
    }
}