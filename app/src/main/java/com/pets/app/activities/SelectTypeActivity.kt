package com.pets.app.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.ViewFlipper
import com.pets.app.R
import com.pets.app.adapters.CommonAdapter
import com.pets.app.common.AppPreferenceManager
import com.pets.app.common.ApplicationsConstants
import com.pets.app.common.Constants
import com.pets.app.common.Enums
import com.pets.app.initialsetup.BaseActivity
import com.pets.app.interfaces.SimpleItemClickListener
import com.pets.app.model.*
import com.pets.app.utilities.TimeStamp
import com.pets.app.utilities.Utils
import com.pets.app.webservice.RestClient
import com.pets.app.webservice.WebServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectTypeActivity : BaseActivity(), SimpleItemClickListener {

    private var viewFlipper: ViewFlipper? = null
    private var mList: ArrayList<Any>? = null
    private var adapter: CommonAdapter? = null
    private var selectedId: String? = ""
    private var petTypeId: String? = ""
    private var navigationType: Int? = 0 // 0- PetType, 1- breed, 2-category

    companion object {
        private val TAG = SelectTypeActivity::class.java.simpleName
        fun startActivity(activity: Activity, requestCode: Int, selectedId: String, navigationType: Int, petsTypeId: String) {
            val intent = Intent(activity, SelectTypeActivity::class.java)
            intent.putExtra(ApplicationsConstants.SELECTION, selectedId)
            intent.putExtra(ApplicationsConstants.NAVIGATION_TYPE, navigationType)
            intent.putExtra(ApplicationsConstants.DATA, petsTypeId)
            activity.startActivityForResult(intent, requestCode)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_type)
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
        navigationType = intent.getIntExtra(ApplicationsConstants.NAVIGATION_TYPE, 0)
        selectedId = intent.getStringExtra(ApplicationsConstants.SELECTION)
        petTypeId = intent.getStringExtra(ApplicationsConstants.DATA)

        if (navigationType == 0) {
            initializeToolbar(this.getString(R.string.pet_type))
            if (Utils.isOnline(this)) {
                getPetTypeApiCall()
            } else {
                viewFlipper?.displayedChild = 2
            }
        } else if (navigationType == 1) {

            initializeToolbar(this.getString(R.string.breed))
            if (Utils.isOnline(this)) {
                getPetBreedApiCall()
            } else {
                viewFlipper?.displayedChild = 2
            }
        } else if (navigationType == 2) {

            initializeToolbar(this.getString(R.string.category))
            if (Utils.isOnline(this)) {
                getCategory()
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
                    } else {
                        Utils.showErrorToast(response.errorBody())
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
            if (!selectedId.isNullOrEmpty()) {
                for (obj in list) {
                    if (obj.petsTypeId.equals(selectedId, true)) {
                        obj.isSelected = true
                        break
                    }
                }
            }
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
                    } else {
                        Utils.showErrorToast(response.errorBody())
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
            if (!selectedId.isNullOrEmpty()) {
                for (obj in list) {
                    if (obj.breed_id.equals(selectedId, true)) {
                        obj.isSelected = true
                        break
                    }
                }
            }
            mList?.addAll(list)
            adapter = CommonAdapter(this, mList!!)
            mRecyclerView?.adapter = adapter
            adapter?.setItemClick(this)
        } else {
            viewFlipper?.displayedChild = 1
        }
    }

    private fun getCategory() {
        val languageCode = Enums.Language.EN.name
        val timeStamp = TimeStamp.getTimeStamp()
        val key = TimeStamp.getMd5(timeStamp + AppPreferenceManager.getUserID() + Constants.TIME_STAMP_KEY)

        showProgressBar()
        val api = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
        val call = api.getProductCategory(AppPreferenceManager.getUserID(), timeStamp, key, languageCode)
        call.enqueue(object : Callback<CategoryResponse> {
            override fun onResponse(call: Call<CategoryResponse>?, response: Response<CategoryResponse>?) {
                hideProgressBar()
                if (response != null) {
                    if (response.body() != null && response.isSuccessful) {
                        checkCategoryResponse(response.body().list)
                    } else {
                        Utils.showErrorToast(response.errorBody())
                    }
                }
            }

            override fun onFailure(call: Call<CategoryResponse>?, t: Throwable?) {
                hideProgressBar()
            }
        })
    }

    private fun checkCategoryResponse(list: ArrayList<Category>?) {
        viewFlipper?.displayedChild = 0
        if (list != null && list.isNotEmpty()) {
            if (!selectedId.isNullOrEmpty()) {
                for (obj in list) {
                    if (obj.productCategoryId.equals(selectedId, true)) {
                        obj.isSelected = true
                        break
                    }
                }
            }
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
            type.isSelected = true
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
            breed.isSelected = true
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
        } else if (`object` is Category) {
            val categoty = `object`
            categoty.isSelected = true
            for (i in mList!!.indices) {
                if (i != mList!!.indexOf(categoty)) {
                    (mList?.get(i) as Category).isSelected = false
                }
            }
            adapter?.notifyDataSetChanged()
            val mIntent = Intent()
            mIntent.putExtra(ApplicationsConstants.DATA, categoty)
            setResult(Activity.RESULT_OK, mIntent)
            finish()
        }
    }
}
