package com.pets.app.activities

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ViewFlipper
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

    private var mViewFlipper: ViewFlipper? = null
    private var mRecyclerView: RecyclerView? = null
    private var linLoadMore: LinearLayout? = null
    private var tvMessage: TextView? = null
    private var btnRetry: Button? = null
    private var adapter: CommonAdapter? = null
    private var mList: ArrayList<Any>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_pets)

        initializeToolbar(this.getString(R.string.my_pets))
        initView()
        checkValidations()
    }

    private fun initView() {

        mViewFlipper = findViewById(R.id.viewFlipper)
        mRecyclerView = findViewById(R.id.recyclerView)
        linLoadMore = findViewById(R.id.linLoadMore)
        tvMessage = findViewById(R.id.tvNoResult)
        btnRetry = findViewById(R.id.btnRetry)

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
            mViewFlipper?.displayedChild = 0
            myPetsApiCall()
        } else {
            mViewFlipper?.displayedChild = 3
        }
    }

    private fun myPetsApiCall() {

        val offset = "0"
        val userId = AppPreferenceManager.getUserID()
        val language = Enums.Language.EN.name.toUpperCase()
        val timeStamp = TimeStamp.getTimeStamp()
        val key = TimeStamp.getMd5(timeStamp + userId + Constants.TIME_STAMP_KEY)

        mViewFlipper?.displayedChild = 0
        val apiClient = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
        val call = apiClient.myPetsList(userId, timeStamp, key, language, offset)
        call.enqueue(object : Callback<PetResponse> {
            override fun onResponse(call: Call<PetResponse>, response: Response<PetResponse>?) {
                mViewFlipper?.displayedChild = 1
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
                mViewFlipper?.displayedChild = 1
            }
        })
    }

    private fun checkResponse(petResponse: PetResponse?) {

        if (petResponse!!.list != null && petResponse.list.isNotEmpty()) {
            mViewFlipper?.displayedChild = 1

            mList!!.clear()
            mList!!.addAll(petResponse.list)
            adapter!!.notifyItemInserted(mList!!.size)
        } else {
            mViewFlipper?.displayedChild = 0
        }
    }

    override fun onItemClick(`object`: Any?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
