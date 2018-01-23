package com.pets.app.activities

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.pets.app.R
import com.pets.app.adapters.CommonAdapter
import com.pets.app.common.AppPreferenceManager
import com.pets.app.common.Constants
import com.pets.app.common.Enums
import com.pets.app.initialsetup.BaseActivity
import com.pets.app.interfaces.SimpleItemClickListener
import com.pets.app.model.PetResponse
import com.pets.app.utilities.TimeStamp
import com.pets.app.utilities.Utils
import com.pets.app.webservice.RestClient
import com.pets.app.webservice.WebServiceBuilder
import com.pets.app.widgets.SimpleDividerItemDecoration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPetsActivity : BaseActivity(), SimpleItemClickListener {

    private var adapter: CommonAdapter? = null
    private var mList: ArrayList<Any>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_pets)

        initializeToolbar(this.getString(R.string.my_pets))
        initViewFlipperWithRecyclerView()
        initView()
        checkValidations()
    }

    private fun initView() {

        val mLinearLayoutManager = LinearLayoutManager(this)
        mLinearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        mRecyclerView?.layoutManager = mLinearLayoutManager
        mRecyclerView?.addItemDecoration(SimpleDividerItemDecoration(this))

        adapter = CommonAdapter(this, mList!!)
        mRecyclerView?.adapter = adapter
        adapter!!.setItemClick(this)
    }

    private fun checkValidations() {

        if (Utils.isOnline(this)) {
            showMainLayout()
            myPetsApiCall()
        } else {
            showOfflineMode()
        }
    }

    private fun myPetsApiCall() {

        val offset = "0"
        val userId = AppPreferenceManager.getUserID()
        val language = Enums.Language.EN.name.toUpperCase()
        val timeStamp = TimeStamp.getTimeStamp()
        val key = TimeStamp.getMd5(timeStamp + userId + Constants.TIME_STAMP_KEY)

        showLoader()
        val apiClient = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
        val call = apiClient.myPetsList(userId, timeStamp, key, language, offset)
        call.enqueue(object : Callback<PetResponse> {
            override fun onResponse(call: Call<PetResponse>, response: Response<PetResponse>?) {
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

            override fun onFailure(call: Call<PetResponse>, t: Throwable) {
                showMainLayout()
            }
        })
    }

    private fun checkResponse(petResponse: PetResponse?) {

        if (petResponse!!.list != null && petResponse.list.isNotEmpty()) {
            showMainLayout()

            mList!!.clear()
            mList!!.addAll(petResponse.list)
            adapter!!.notifyItemInserted(mList!!.size)
        } else {
            showNoDataFound()
        }
    }

    override fun onItemClick(`object`: Any?) {
    }
}
