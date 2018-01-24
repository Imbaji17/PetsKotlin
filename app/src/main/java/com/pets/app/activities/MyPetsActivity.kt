package com.pets.app.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.pets.app.R
import com.pets.app.adapters.CommonAdapter
import com.pets.app.common.AppPreferenceManager
import com.pets.app.common.Constants
import com.pets.app.common.Enums
import com.pets.app.initialsetup.BaseActivity
import com.pets.app.interfaces.SimpleItemClickListener
import com.pets.app.model.PetResponse
import com.pets.app.model.`object`.PetDetails
import com.pets.app.utilities.TimeStamp
import com.pets.app.utilities.Utils
import com.pets.app.webservice.RestClient
import com.pets.app.webservice.WebServiceBuilder
import com.pets.app.widgets.SimpleDividerItemDecoration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPetsActivity : BaseActivity(), SimpleItemClickListener {

    private val RC_ADD_PET: Int = 100
    private var adapter: CommonAdapter? = null
    private var mList: ArrayList<Any>? = ArrayList()
    private var loadMore: Boolean = false
    private var offset: Int = 0

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

        mRecyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy > 0) {
                    val visibleItemCount = mLinearLayoutManager.childCount
                    val totalItemCount = mLinearLayoutManager.itemCount
                    val pastVisibleItems = mLinearLayoutManager.findFirstVisibleItemPosition()
                    if (!loadMore) {
                        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                            loadMore = true
                            if (offset != -1) {
                                if (Utils.isOnline(this@MyPetsActivity))
                                    myPetsApiCall()
                            }
                        }
                    }
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_add -> {
                val mIntent = Intent(this, AddPetActivity::class.java)
                this.startActivityForResult(mIntent, RC_ADD_PET)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_ADD_PET -> {
                if (data != null) {
                    if (Utils.isOnline(this)) {
                        loadMore = false
                        offset = 0
                        myPetsApiCall()
                    }
                }
            }
        }
    }

    private fun checkValidations() {

        loadMore = false
        offset = 0

        if (Utils.isOnline(this)) {
            showMainLayout()
            myPetsApiCall()
        } else {
            showOfflineMode()
        }
    }

    private fun myPetsApiCall() {

        val userId = AppPreferenceManager.getUserID()
        val language = Enums.Language.EN.name.toUpperCase()
        val timeStamp = TimeStamp.getTimeStamp()
        val key = TimeStamp.getMd5(timeStamp + userId + Constants.TIME_STAMP_KEY)

        if (loadMore) {
            linLoadMore!!.visibility = View.VISIBLE
        } else {
            showLoader()
        }
        val apiClient = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
        val call = apiClient.myPetsList(userId, timeStamp, key, language, offset.toString())
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
                showNoDataFound(this@MyPetsActivity.getString(R.string.server_not_responding))
            }
        })
    }

    private fun checkResponse(petResponse: PetResponse?) {

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
        if (`object` is PetDetails) {
            PetDetailsActivity.startActivity(this, `object`)
        }
    }
}
