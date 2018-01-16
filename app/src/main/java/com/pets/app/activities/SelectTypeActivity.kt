package com.pets.app.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ViewFlipper
import com.google.gson.GsonBuilder
import com.pets.app.R
import com.pets.app.adapters.CommonAdapter
import com.pets.app.common.AppPreferenceManager
import com.pets.app.common.ApplicationsConstants
import com.pets.app.common.Constants
import com.pets.app.common.Enums
import com.pets.app.initialsetup.BaseActivity
import com.pets.app.interfaces.SimpleItemClickListener
import com.pets.app.model.Breed
import com.pets.app.model.BreedResponse
import com.pets.app.model.PetsType
import com.pets.app.model.PetsTypeResponse
import com.pets.app.utilities.TimeStamp
import com.pets.app.utilities.Utils
import com.pets.app.webservice.RestClient
import com.pets.app.webservice.WebServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class SelectTypeActivity : BaseActivity(), SimpleItemClickListener {

    private var viewFlipper: ViewFlipper? = null
    private var mRecyclerView: RecyclerView? = null
    private var mList: ArrayList<Any>? = null
    private var adapter: CommonAdapter? = null
    private val petTypeId: String? = "1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_type)

        initializeToolbar(this.getString(R.string.choose_country))
        initView()
        getIntentData()
    }

    private fun initView() {

        mList = ArrayList()

        viewFlipper = findViewById(R.id.viewFlipper)
        mRecyclerView = findViewById(R.id.recyclerView)

        val mLinearLayoutManager = LinearLayoutManager(this)
        mLinearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        mRecyclerView?.layoutManager = mLinearLayoutManager
    }

    private fun getIntentData() {

        if (intent.getBooleanExtra(ApplicationsConstants.NAVIGATION_TYPE, false)) {
            if (Utils.isOnline(this)) {
                getPetTypeApiCall()
            } else {
                viewFlipper?.displayedChild = 2
            }
        } else {
            if (Utils.isOnline(this)) {
                getPetBreedApiCall()
            } else {
                viewFlipper?.displayedChild = 2
            }
        }
    }

    private fun getPetTypeApiCall() {

        val languageCode = Enums.Language.EN.name
        val timeStamp = TimeStamp.getTimeStamp()
        val key = TimeStamp.getMd5(timeStamp + AppPreferenceManager.getUserID() + Constants.TIME_STAMP_KEY)

        showProgressBar()
        val api = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
        val call = api.getPetTypeList(key, languageCode, timeStamp, AppPreferenceManager.getUserID())
        call.enqueue(object : Callback<PetsTypeResponse> {
            override fun onResponse(call: Call<PetsTypeResponse>?, response: Response<PetsTypeResponse>?) {
                hideProgressBar()
                if (response != null) {
                    if (response.body() != null && response.isSuccessful) {
                        checkResponse(response.body().list)
                    } else if (response.code() == 403) {
                        val gson = GsonBuilder().create()
                        val mError: PetsTypeResponse
                        try {
                            mError = gson.fromJson(response.errorBody().string(), PetsTypeResponse::class.java)
                            Utils.showToast("" + mError.message)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<PetsTypeResponse>?, t: Throwable?) {
                hideProgressBar()
            }
        })
    }

    private fun checkResponse(list: ArrayList<PetsType>?) {
        viewFlipper?.displayedChild = 0
        if (list != null && list.isNotEmpty()) {
            mList?.addAll(list)
            adapter = CommonAdapter(this, mList!!)
            mRecyclerView?.adapter = adapter
            adapter?.setItemClick(this)
        } else {
            viewFlipper?.displayedChild = 1
        }
    }

    private fun getPetBreedApiCall() {

        val languageCode = Enums.Language.EN.name
        val timeStamp = TimeStamp.getTimeStamp()
        val key = TimeStamp.getMd5(timeStamp + AppPreferenceManager.getUserID() + petTypeId + Constants.TIME_STAMP_KEY)

        showProgressBar()
        val api = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
        val call = api.getBreedList(key, languageCode, timeStamp, AppPreferenceManager.getUserID(), petTypeId)
        call.enqueue(object : Callback<BreedResponse> {
            override fun onResponse(call: Call<BreedResponse>?, response: Response<BreedResponse>?) {
                hideProgressBar()
                if (response != null) {
                    if (response.body() != null && response.isSuccessful) {
                        checkBreedResponse(response.body().list)
                    } else if (response.code() == 403) {
                        val gson = GsonBuilder().create()
                        val mError: BreedResponse
                        try {
                            mError = gson.fromJson(response.errorBody().string(), BreedResponse::class.java)
                            Utils.showToast("" + mError.message)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<BreedResponse>?, t: Throwable?) {
                hideProgressBar()
            }
        })
    }

    private fun checkBreedResponse(list: ArrayList<Breed>?) {
        viewFlipper?.displayedChild = 0
        if (list != null && list.isNotEmpty()) {
            mList?.addAll(list)
            adapter = CommonAdapter(this, mList!!)
            mRecyclerView?.adapter = adapter
            adapter?.setItemClick(this)
        } else {
            viewFlipper?.displayedChild = 1
        }
    }

    override fun onItemClick(`object`: Any?) {

        if (`object` is PetsType) {
            val type = `object`
            type.isSelected = !type.isSelected
            for (i in mList!!.indices) {
                if (i != mList!!.indexOf(type)) {
                    (mList?.get(i) as PetsType).isSelected = false
                }
            }
            adapter?.notifyDataSetChanged()
            val mIntent = Intent()
            mIntent.putExtra(ApplicationsConstants.DATA, type)
            setResult(Activity.RESULT_OK, mIntent)
            finish()
        } else if (`object` is Breed) {
            val breed = `object`
            breed.isSelected = !breed.isSelected
            for (i in mList!!.indices) {
                if (i != mList!!.indexOf(breed)) {
                    (mList?.get(i) as Breed).isSelected = false
                }
            }
            adapter?.notifyDataSetChanged()
            val mIntent = Intent()
            mIntent.putExtra(ApplicationsConstants.DATA, breed)
            setResult(Activity.RESULT_OK, mIntent)
            finish()
        }
    }
}
