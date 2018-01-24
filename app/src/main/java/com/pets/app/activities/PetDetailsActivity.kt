package com.pets.app.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.pets.app.R
import com.pets.app.common.AppPreferenceManager
import com.pets.app.common.ApplicationsConstants
import com.pets.app.common.Constants
import com.pets.app.common.Enums
import com.pets.app.initialsetup.BaseActivity
import com.pets.app.model.PetResponse
import com.pets.app.model.`object`.PetDetails
import com.pets.app.utilities.TimeStamp
import com.pets.app.utilities.Utils
import com.pets.app.webservice.RestClient
import com.pets.app.webservice.WebServiceBuilder
import kotlinx.android.synthetic.main.app_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PetDetailsActivity : BaseActivity() {

    private val RC_EDIT_PET: Int = 100
    private var petObj: PetDetails? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_details)

        isHeaderImage = true
        imgHeader.setImageResource(R.drawable.logo_header)
        initializeToolbar("")
        initView()
        getIntentData()
        clickListeners()
    }

    private fun initView() {

    }

    private fun clickListeners() {


    }

    private fun getIntentData() {

        petObj = intent.getSerializableExtra(ApplicationsConstants.DATA) as PetDetails

        if (petObj != null) {
            checkValidations()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_add -> {
                val mIntent = Intent(this, AddPetActivity::class.java)
                this.startActivityForResult(mIntent, RC_EDIT_PET)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
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

        val userId = AppPreferenceManager.getUserID()
        val language = Enums.Language.EN.name.toUpperCase()
        val timeStamp = TimeStamp.getTimeStamp()
        val key = TimeStamp.getMd5(timeStamp + userId + Constants.TIME_STAMP_KEY)

        showProgressBar()
        val apiClient = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
        val call = apiClient.getPetDetails(userId, timeStamp, key, language, petObj!!.pet_id)
        call.enqueue(object : Callback<PetResponse> {
            override fun onResponse(call: Call<PetResponse>, response: Response<PetResponse>?) {
                hideProgressBar()
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
                hideProgressBar()
                showNoDataFound(this@PetDetailsActivity.getString(R.string.server_not_responding))
            }
        })
    }

    private fun checkResponse(petResponse: PetResponse?) {

        if (petResponse!!.result != null) {
        }
    }
}
