package com.pets.app.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.Button
import com.pets.app.R
import com.pets.app.adapters.CommonAdapter
import com.pets.app.common.AppPreferenceManager
import com.pets.app.common.ApplicationsConstants
import com.pets.app.common.Constants
import com.pets.app.common.Enums
import com.pets.app.initialsetup.BaseActivity
import com.pets.app.interfaces.SimpleItemClickListener
import com.pets.app.model.PetSitterResponse
import com.pets.app.model.`object`.PetSitterDetails
import com.pets.app.utilities.TimeStamp
import com.pets.app.utilities.Utils
import com.pets.app.webservice.RestClient
import com.pets.app.webservice.WebServiceBuilder
import com.pets.app.widgets.SimpleDividerItemDecoration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PetSittersActivity : BaseActivity(), View.OnClickListener, SimpleItemClickListener {

    private var btnRegister: Button? = null
    private var adapter: CommonAdapter? = null
    private var mList: ArrayList<Any>? = ArrayList()
    private var loadMore: Boolean = false
    private var offset: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_sitters)

        initializeToolbar(this.getString(R.string.pet_sitters))
        initViewFlipperWithRecyclerView()
        initView()
        checkValidations()
    }

    private fun initView() {

        btnRegister = findViewById(R.id.btnRegister)
        btnRegister?.setOnClickListener(this)

        val mGridLayoutManager = GridLayoutManager(this, 2)
        mGridLayoutManager.orientation = LinearLayoutManager.VERTICAL
        mRecyclerView?.layoutManager = mGridLayoutManager
        mRecyclerView?.addItemDecoration(SimpleDividerItemDecoration(this))

        adapter = CommonAdapter(this, mList!!)
        mRecyclerView?.adapter = adapter
        adapter!!.setItemClick(this)

        mRecyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy > 0) {
                    val visibleItemCount = mGridLayoutManager.childCount
                    val totalItemCount = mGridLayoutManager.itemCount
                    val pastVisibleItems = mGridLayoutManager.findFirstVisibleItemPosition()
                    if (!loadMore) {
                        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                            loadMore = true
                            if (offset != -1) {
                                if (Utils.isOnline(this@PetSittersActivity))
                                    petSittersListApiCall()
                            }
                        }
                    }
                }
            }
        })
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.btnRetry -> {
                checkValidations()
            }
            R.id.btnRegister -> {
                val mIntent = Intent(this, RegisterPetSitterActivity::class.java)
                this.startActivity(mIntent)
            }
        }
    }

    private fun checkValidations() {

        loadMore = false
        offset = 0

        if (Utils.isOnline(this)) {
            showMainLayout()
            petSittersListApiCall()
        } else {
            showOfflineMode()
        }
    }

    private fun petSittersListApiCall() {

        val userId = AppPreferenceManager.getUserID()
        val language = Enums.Language.EN.name.toUpperCase()
        val latitude = AppPreferenceManager.getUser().lat
        val longitude = AppPreferenceManager.getUser().lng
        val timeStamp = TimeStamp.getTimeStamp()
        val key = TimeStamp.getMd5(timeStamp + userId + Constants.TIME_STAMP_KEY)

        if (loadMore) {
            linLoadMore!!.visibility = View.VISIBLE
        } else {
            showLoader()
        }
        val apiClient = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
        val call = apiClient.myPetSitterList(userId, timeStamp, key, language, offset.toString(), latitude, longitude)
        call.enqueue(object : Callback<PetSitterResponse> {
            override fun onResponse(call: Call<PetSitterResponse>, response: Response<PetSitterResponse>?) {
                showMainLayout()
                if (response != null) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            checkResponse(response.body())
                        }
                    } else {
                        Utils.showErrorToast(response.errorBody())
                    }
                }
            }

            override fun onFailure(call: Call<PetSitterResponse>, t: Throwable) {
                showNoDataFound(this@PetSittersActivity.getString(R.string.server_not_responding))
            }
        })
    }

    private fun checkResponse(petResponse: PetSitterResponse?) {

        if (TextUtils.isEmpty(petResponse?.pet_status)) {
            btnRegister?.visibility = View.VISIBLE
        } else {
            btnRegister?.visibility = View.GONE
        }

        offset = petResponse!!.nextOffset

        if (petResponse.list != null && petResponse.list.isNotEmpty()) {

            if (!loadMore)
                mList!!.clear()

            mList!!.addAll(petResponse.list)
            adapter!!.notifyItemInserted(mList!!.size)
        } else {
            if (mList!!.isEmpty())
                showNoDataFound(this.getString(R.string.no_result_found))
        }
    }

    override fun onItemClick(`object`: Any?) {
        if (`object` is PetSitterDetails) {
            val mIntent = Intent(this, RegisterPetSitterActivity::class.java)
            mIntent.putExtra(ApplicationsConstants.ID, `object`.pet_sitter_id)
            this.startActivity(mIntent)
        }
    }
}
