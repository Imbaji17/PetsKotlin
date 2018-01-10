package com.pets.app.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ViewFlipper
import com.google.gson.GsonBuilder
import com.pets.app.R
import com.pets.app.adapters.FindHostelAdapter
import com.pets.app.common.Constants
import com.pets.app.model.FindHostelResponse
import com.pets.app.model.NormalResponse
import com.pets.app.utilities.TimeStamp
import com.pets.app.utilities.Utils
import com.pets.app.webservice.RestClient
import com.pets.app.webservice.WebServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*


class FindHostelActivity : AppCompatActivity(), View.OnClickListener {

    private var layoutManager: LinearLayoutManager? = null
    private var adapter: FindHostelAdapter? = null
    private val listItems = ArrayList<Any>()
    private var recyclerView: RecyclerView? = null
    private var viewFlipper: ViewFlipper? = null
    private var nextOffset = 0
    private var layoutManger: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_hostel)
        initView()
        setAdapter()
        getHostelList()
    }

    fun initView() {
        recyclerView = findViewById(R.id.recyclerView)
        viewFlipper = findViewById(R.id.viewFlipper)

    }

    private fun setAdapter() {
        layoutManger = LinearLayoutManager(this)
        recyclerView!!.layoutManager = layoutManger
        adapter = FindHostelAdapter(listItems, this)
        recyclerView!!.adapter = adapter
    }

    private fun getHostelList() {

        val timeStamp = TimeStamp.getTimeStamp()
        val key = TimeStamp.getMd5(timeStamp + "10" + Constants.TIME_STAMP_KEY)

//        @Field("key") String key, @Field("keyword") String keyword, @Field("language_code") String languageCode, @Field("lat") String lat, @Field("lng") String lng, @Field("next_offset") String next_offset, @Field("timestamp") String timestamp, @Field("user_id") String user_id

//        http@ //34.199.202.75/pets/api/PetsApi/hostel_list?key=6b88b734ebb2c9f02ffe2cfdc1f40020&keyword=
// &language_code=EN&lat=0.000000&lng=0.000000&next_offset=0&timestamp=1515495550497.3&user_id=10

        viewFlipper!!.displayedChild = 0
        if (Utils.isOnline(this)) {
            val apiClient = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
            val call = apiClient.hostelList(key, "", "EN", "0.000000", "0.000000", nextOffset, timeStamp, "10")


            call.enqueue(object : Callback<FindHostelResponse> {
                override fun onResponse(call: Call<FindHostelResponse>, response: Response<FindHostelResponse>?) {
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

                override fun onFailure(call: Call<FindHostelResponse>, t: Throwable) {
//                    mProgressBar.setVisibility(View.GONE)
//                    Utils.showToast(mActivity, t.message)
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

    }

}
