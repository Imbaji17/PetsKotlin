package com.pets.app.activities.adoption

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v4.widget.NestedScrollView
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.google.gson.GsonBuilder
import com.pets.app.R
import com.pets.app.adapters.ImageAdapter
import com.pets.app.common.AppPreferenceManager
import com.pets.app.common.ApplicationsConstants
import com.pets.app.common.Constants
import com.pets.app.initialsetup.BaseActivity
import com.pets.app.model.Adoption
import com.pets.app.model.AdoptionResponse
import com.pets.app.model.HostelImage
import com.pets.app.model.NormalResponse
import com.pets.app.utilities.TimeStamp
import com.pets.app.utilities.Utils
import com.pets.app.webservice.RestClient
import com.pets.app.webservice.WebServiceBuilder
import com.viewpagerindicator.CirclePageIndicator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class AdoptionDetailsActivity : BaseActivity(), View.OnClickListener {

    private var cvp: CirclePageIndicator? = null
    private var viewPager: ViewPager? = null
    private var tvName: TextView? = null
    private var llContactPerson: LinearLayout? = null
    private var tvContactPerson: TextView? = null
    private var llContact: LinearLayout? = null
    private var tvContact: TextView? = null
    private var llAddress: LinearLayout? = null
    private var tvAddress: TextView? = null
    private var llType: LinearLayout? = null
    private var tvType: TextView? = null
    private var llBreed: LinearLayout? = null
    private var tvBreed: TextView? = null
    private var llCertification: LinearLayout? = null
    private var btnView: Button? = null
    private var llDescription: LinearLayout? = null
    private var tvDescription: TextView? = null
    private var viewFlipper: ViewFlipper? = null
    private var rlForLoadingScreen: RelativeLayout? = null
    private var mainLayout: NestedScrollView? = null
    private var llForNoResult: LinearLayout? = null
    private var llForOfflineScreen: LinearLayout? = null
    private var adoptionId: String? = ""
    private var tvAvailable: TextView? = null
    private var ivFavourite: ImageView? = null

    companion object {
        private val TAG = AdoptionDetailsActivity::class.java.simpleName
        fun startActivity(activity: Activity, hostelId: String) {
            val intent = Intent(activity, AdoptionDetailsActivity::class.java)
            intent.putExtra(ApplicationsConstants.ID, hostelId)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adoption_details)
        initializeToolbar(getString(R.string.adoption))
        init()
        initView()
        getAdoptionDetails()
    }

    private fun init() {
        adoptionId = intent.getStringExtra(ApplicationsConstants.ID)
    }

    private fun initView() {
        cvp = findViewById(R.id.cvp)
        viewPager = findViewById(R.id.viewPager)
        tvName = findViewById(R.id.tvName)
        llContactPerson = findViewById(R.id.llContactPerson)
        tvContactPerson = findViewById(R.id.tvContactPerson)
        llContact = findViewById(R.id.llContact)
        tvContact = findViewById(R.id.tvContact)
        llType = findViewById(R.id.llType)
        tvType = findViewById(R.id.tvType)
        llBreed = findViewById(R.id.llBreed)
        tvBreed = findViewById(R.id.tvBreed)
        llCertification = findViewById(R.id.llCertification)
        btnView = findViewById(R.id.btnView)
        llDescription = findViewById(R.id.llDescription)
        tvDescription = findViewById(R.id.tvDescription)
        llAddress = findViewById(R.id.llAddress)
        tvAddress = findViewById(R.id.tvAddress)
        tvAvailable = findViewById(R.id.tvAvailable)
        ivFavourite = findViewById(R.id.ivFavourite)
        viewFlipper = findViewById(R.id.viewFlipper)
        rlForLoadingScreen = findViewById(R.id.rlForLoadingScreen)
        mainLayout = findViewById(R.id.mainLayout)
        llForNoResult = findViewById(R.id.llForNoResult)
        llForOfflineScreen = findViewById(R.id.llForOfflineScreen)
        btnRetry = findViewById(R.id.btnRetry)

        btnRetry?.setOnClickListener(this)
        btnView!!.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.btnRetry -> {
                getAdoptionDetails()
            }
            R.id.btnView -> {

            }
        }
    }

    private fun getAdoptionDetails() {
        setLoadingLayout()
        val timeStamp = TimeStamp.getTimeStamp()
        val userId = AppPreferenceManager.getUserID()
        val lat = AppPreferenceManager.getUser().lat
        val lng = AppPreferenceManager.getUser().lng

        val key = TimeStamp.getMd5(timeStamp + userId + adoptionId + Constants.TIME_STAMP_KEY)
        if (Utils.isOnline(this)) {
            val apiClient = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
            val call = apiClient.adoptionDetails(adoptionId, key, "EN", lat, lng, timeStamp, userId)
            call.enqueue(object : Callback<AdoptionResponse> {
                override fun onResponse(call: Call<AdoptionResponse>, response: Response<AdoptionResponse>?) {
                    if (response != null && response.isSuccessful && response.body() != null && response.body().result != null) {
                        setMainLayout()
                        setValues(response.body().result)
                    } else {
                        setNoDataLayout()
                        val gson = GsonBuilder().create()
                        val mError: NormalResponse
                        try {
                            mError = gson.fromJson(response!!.errorBody().string(), NormalResponse::class.java)
                            Utils.showToast("" + mError.message)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<AdoptionResponse>, t: Throwable) {
                    setNoDataLayout()
                }
            })

        } else {
            setOfflineLayout()
        }
    }

    private fun setValues(result: Adoption?) {
        if (!TextUtils.isEmpty(result!!.contactPerson)) {
            llContactPerson?.visibility = View.VISIBLE
            tvContactPerson?.text = result.contactPerson
        } else {
            llContactPerson?.visibility = View.GONE
        }

        if (!TextUtils.isEmpty(result.contactNo)) {
            llContact?.visibility = View.VISIBLE
            tvContact?.text = result.contactNo
        } else {
            llContact?.visibility = View.GONE
        }

        if (!TextUtils.isEmpty(result.address)) {
            llAddress?.visibility = View.VISIBLE
            tvAddress?.text = result.address
        } else {
            llAddress?.visibility = View.GONE
        }

        if (result.petsType != null && !TextUtils.isEmpty(result.petsType.typeName)) {
            llType?.visibility = View.VISIBLE
            tvType?.text = result.petsType.typeName
        } else {
            llType?.visibility = View.GONE
        }

        if (result.breed != null && !TextUtils.isEmpty(result.breed.breed_name)) {
            llBreed?.visibility = View.VISIBLE
            tvBreed?.text = result.breed.breed_name
        } else {
            llType?.visibility = View.GONE
        }

        if (!TextUtils.isEmpty(result.description)) {
            llDescription?.visibility = View.VISIBLE
            tvDescription?.text = result.description
        } else {
            llDescription?.visibility = View.GONE
        }

        var imageList = ArrayList<HostelImage>()

        if (!TextUtils.isEmpty(result.profileImage)) {
            var hostelImage = HostelImage()
            hostelImage.image = result.profileImage
            imageList.add(hostelImage)
        }

        if (!result.adoptionImages.isEmpty()) {
            imageList.addAll(result.adoptionImages)

        }

        if (imageList.size > 0) {
            val adapter = ImageAdapter(this, imageList)
            viewPager?.adapter = adapter
            cvp!!.setViewPager(viewPager)
        }

        tvName?.text = if (!TextUtils.isEmpty(result.petName)) {
            result.petName
        } else {
            ""
        }
        ivFavourite!!.setImageResource(if (result.isInterest) R.drawable.fav1 else R.drawable.fav2)
    }

    private fun setOfflineLayout() {
        viewFlipper!!.displayedChild = viewFlipper!!.indexOfChild(llForOfflineScreen)
    }

    private fun setLoadingLayout() {
        viewFlipper!!.displayedChild = viewFlipper!!.indexOfChild(rlForLoadingScreen)
    }

    private fun setMainLayout() {
        viewFlipper!!.displayedChild = viewFlipper!!.indexOfChild(mainLayout)
    }

    private fun setNoDataLayout() {
        viewFlipper!!.displayedChild = viewFlipper!!.indexOfChild(llForNoResult)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_chat, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_chat -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

}
