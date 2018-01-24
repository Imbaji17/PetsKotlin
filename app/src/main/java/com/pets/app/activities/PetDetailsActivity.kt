package com.pets.app.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.pets.app.R
import com.pets.app.adapters.LandingImageAdapter
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
import com.viewpagerindicator.CirclePageIndicator
import kotlinx.android.synthetic.main.app_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PetDetailsActivity : BaseActivity(), View.OnClickListener {

    private var viewPager: ViewPager? = null
    private var pageIndicator: CirclePageIndicator? = null
    private var tvName: TextView? = null
    private var tvBirthDate: TextView? = null
    private var tvContactPerson: TextView? = null
    private var tvContact: TextView? = null
    private var tvEmailAddress: TextView? = null
    private var tvAddress: TextView? = null
    private var tvType: TextView? = null
    private var tvBreed: TextView? = null
    private var btnView: Button? = null
    private var tvDescription: TextView? = null

    private val RC_EDIT_PET: Int = 100
    private var petObj: PetDetails? = null

    companion object {
        fun startActivity(mActivity: Activity, any: Any?) {
            val mIntent = Intent(mActivity, PetDetailsActivity::class.java)
            mIntent.putExtra(ApplicationsConstants.DATA, any as PetDetails)
            mActivity.startActivity(mIntent)
        }
    }

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

        viewPager = findViewById(R.id.viewPager)
        pageIndicator = findViewById(R.id.cvp)
        tvName = findViewById(R.id.tvName)
        tvBirthDate = findViewById(R.id.tvBirthDate)
        tvContactPerson = findViewById(R.id.tvContactPerson)
        tvContact = findViewById(R.id.tvContact)
        tvEmailAddress = findViewById(R.id.tvEmiAddress)
        tvAddress = findViewById(R.id.tvAddress)
        tvType = findViewById(R.id.tvType)
        tvBreed = findViewById(R.id.tvBreed)
        btnView = findViewById(R.id.btnView)
        tvDescription = findViewById(R.id.tvDescription)
        btnView = findViewById(R.id.btnView)
        btnRetry = findViewById(R.id.btnRetry)
    }

    private fun clickListeners() {

        btnView!!.setOnClickListener(this)
        btnRetry?.setOnClickListener(this)
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
        return when (item?.itemId) {
            R.id.action_add -> {
                val mIntent = Intent(this, AddPetActivity::class.java)
                this.startActivityForResult(mIntent, RC_EDIT_PET)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.btnView -> {
                if (petObj != null) {
                    if (TextUtils.isEmpty(petObj?.certificate_image)) {
                        Utils.showToast(this.getString(R.string.pet_certificate_doesnt_exist))
                    }
                }
            }
            R.id.btnRetry -> checkValidations()
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
        val key = TimeStamp.getMd5(timeStamp + userId + petObj!!.pet_id + Constants.TIME_STAMP_KEY)

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

            petObj = petResponse.result

            val mList: ArrayList<PetDetails>? = ArrayList()

            val petDetails = PetDetails()
            petDetails.pet_image = petObj?.pet_image
            mList?.add(petDetails)

            if (petResponse.result.petImages.isNotEmpty()) {
                for (item in petResponse.result.petImages) {
                    val petDetails = PetDetails()
                    petDetails.pet_image = item.pet_image
                    mList?.add(petDetails)
                }
            }
            val adapter = LandingImageAdapter(this, mList!!)
            viewPager?.adapter = adapter
            pageIndicator?.setViewPager(viewPager)

            tvName?.text = if (!TextUtils.isEmpty(petObj!!.pet_name)) petObj!!.pet_name else ""
            tvBirthDate?.text = this@PetDetailsActivity.getString(R.string.birthday).plus(petResponse.result.dob.replace("-", "/"))
            tvContactPerson?.text = if (!TextUtils.isEmpty(petObj!!.user!!.name)) petObj!!.user!!.name else ""
            tvContact?.text = if (!TextUtils.isEmpty(petObj!!.user!!.phone_number)) petObj!!.user!!.phone_number else ""
            tvEmailAddress?.text = if (!TextUtils.isEmpty(petObj!!.user!!.email_id)) petObj!!.user!!.email_id else ""
            tvAddress?.text = if (!TextUtils.isEmpty(petObj!!.user!!.location)) petObj!!.user!!.location else ""
            tvType?.text = if (!TextUtils.isEmpty(petObj!!.petsType!!.typeName)) petObj!!.petsType!!.typeName else ""
            tvBreed?.text = if (!TextUtils.isEmpty(petObj!!.breed!!.breed_name)) petObj!!.breed!!.breed_name else ""
            tvDescription?.text = if (!TextUtils.isEmpty(petObj!!.description)) petObj!!.description else ""

        }
    }
}
