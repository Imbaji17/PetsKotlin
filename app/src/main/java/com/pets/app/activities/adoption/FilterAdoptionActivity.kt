package com.pets.app.activities.adoption

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.NumberPicker
import android.widget.RelativeLayout
import android.widget.TextView
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.gson.GsonBuilder
import com.pets.app.R
import com.pets.app.common.AppPreferenceManager
import com.pets.app.common.ApplicationsConstants
import com.pets.app.common.Constants
import com.pets.app.common.Enums
import com.pets.app.initialsetup.BaseActivity
import com.pets.app.model.*
import com.pets.app.utilities.Logger
import com.pets.app.utilities.TimeStamp
import com.pets.app.utilities.Utils
import com.pets.app.webservice.RestClient
import com.pets.app.webservice.WebServiceBuilder
import com.pets.app.widgets.RangeSeekBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class FilterAdoptionActivity : BaseActivity(), View.OnClickListener {

    private var rlType: RelativeLayout? = null
    private var tvType: TextView? = null
    private var rlBreed: RelativeLayout? = null
    private var tvBreed: TextView? = null
    private var rlLocation: RelativeLayout? = null
    private var tvLocation: TextView? = null
    private var rsbDistance: RangeSeekBar<Number>? = null
    private var rlGender: RelativeLayout? = null
    private var tvGender: TextView? = null
    private var btnSave: Button? = null
    private var petsTypeList = ArrayList<PetsType>()
//    private var breedList = ArrayList<Breed>()

    private val RC_AUTOCOMPLETE: Int = 100
    private var petsTypeId: String? = "";
    private var petsTypeStr: String? = ""
    private var breedId: String? = "";
    private var breedStr: String? = ""
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var gender: String? = ""
    private var distance: Int? = 0
    private var location: String? = ""


    companion object {
        private val TAG = FilterAdoptionActivity::class.java.simpleName
        fun startActivity(activity: Activity, requestCode: Int, petsTypeId: String, petsTypeStr: String,
                          breedId: String, breedStr: String, gender: String, distance: Int, latitude: Double,
                          longitude: Double, location: String) {
            val intent = Intent(activity, FilterAdoptionActivity::class.java)
            intent.putExtra(ApplicationsConstants.PETS_TYPE_ID, petsTypeId)
            intent.putExtra(ApplicationsConstants.PETS_TYPE_NAME, petsTypeStr)
            intent.putExtra(ApplicationsConstants.BREED_ID, breedId)
            intent.putExtra(ApplicationsConstants.BREED_NAME, breedStr)
            intent.putExtra(ApplicationsConstants.GENDER, gender)
            intent.putExtra(ApplicationsConstants.DISTANCE, distance)
            intent.putExtra(ApplicationsConstants.LATITUDE, latitude)
            intent.putExtra(ApplicationsConstants.LONGITUDE, longitude)
            intent.putExtra(ApplicationsConstants.LOCATION, location)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_adoption)
        initializeToolbar(getString(R.string.filter))
        init()
        initView()
        setValues()
        getPetTypeList()
    }

    private fun init() {
        petsTypeId = intent.getStringExtra(ApplicationsConstants.PETS_TYPE_ID)
        petsTypeStr = intent.getStringExtra(ApplicationsConstants.PETS_TYPE_NAME)
        breedId = intent.getStringExtra(ApplicationsConstants.BREED_ID)
        breedStr = intent.getStringExtra(ApplicationsConstants.BREED_NAME)
        gender = intent.getStringExtra(ApplicationsConstants.GENDER)
        distance = intent.getIntExtra(ApplicationsConstants.DISTANCE, 0)
        latitude = intent.getDoubleExtra(ApplicationsConstants.LATITUDE, 0.0)
        longitude = intent.getDoubleExtra(ApplicationsConstants.LONGITUDE, 0.0)
        location = intent.getStringExtra(ApplicationsConstants.LOCATION)
    }

    private fun initView() {
        rlType = findViewById(R.id.rlType)
        tvType = findViewById(R.id.tvType)
        rlBreed = findViewById(R.id.rlBreed)
        tvBreed = findViewById(R.id.tvBreed)
        rlLocation = findViewById(R.id.rlLocation)
        tvLocation = findViewById(R.id.tvLocation)
        rlGender = findViewById(R.id.rlGender)
        tvGender = findViewById(R.id.tvGender)
        btnSave = findViewById(R.id.btnSave)
        rsbDistance = findViewById(R.id.rsbDistance)

        rlType!!.setOnClickListener(this)
        rlBreed!!.setOnClickListener(this)
        rlLocation!!.setOnClickListener(this)
        rlGender!!.setOnClickListener(this)
        btnSave!!.setOnClickListener(this)
        rsbDistance!!.selectedMaxValue = 0
    }

    private fun setValues() {
        if (!TextUtils.isEmpty(petsTypeStr))
            tvType!!.text = petsTypeStr

        if (!TextUtils.isEmpty(breedStr))
            tvBreed!!.text = breedStr

        if (!TextUtils.isEmpty(location))
            tvLocation!!.text = location

        rsbDistance!!.selectedMaxValue = distance

        if (!TextUtils.isEmpty(gender))
            tvGender!!.text = gender
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.rlType -> {
                if (petsTypeList.size > 0)
                    showTypeDialog(petsTypeList)
            }

            R.id.rlBreed -> {
                if (!TextUtils.isEmpty(petsTypeId))
                    getBreedList(petsTypeId!!)
            }

            R.id.rlLocation -> {
                openAutocompleteActivity()
            }
            R.id.rlGender -> {
                showGenderDialog()
            }
            R.id.btnSave -> {

                if (!TextUtils.isEmpty(tvType!!.text.toString().trim()))
                    petsTypeStr = tvType!!.text.toString()

                if (!TextUtils.isEmpty(tvBreed!!.text.toString().trim()))
                    breedStr = tvBreed!!.text.toString()

                if (!TextUtils.isEmpty(tvGender!!.text.toString().trim()))
                    gender = tvGender!!.text.toString()
                if (!TextUtils.isEmpty(tvLocation!!.text.toString().trim()))
                    location = tvLocation!!.text.toString()

                distance = rsbDistance!!.selectedMaxValue as Int?

                val intent = Intent()
                intent.putExtra(ApplicationsConstants.PETS_TYPE_ID, petsTypeId)
                intent.putExtra(ApplicationsConstants.PETS_TYPE_NAME, petsTypeStr)
                intent.putExtra(ApplicationsConstants.BREED_ID, breedId)
                intent.putExtra(ApplicationsConstants.BREED_NAME, breedStr)
                intent.putExtra(ApplicationsConstants.GENDER, gender)
                intent.putExtra(ApplicationsConstants.DISTANCE, distance)
                intent.putExtra(ApplicationsConstants.LATITUDE, latitude)
                intent.putExtra(ApplicationsConstants.LATITUDE, longitude)
                intent.putExtra(ApplicationsConstants.LOCATION, location)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    private fun getPetTypeList() {
        val timeStamp = TimeStamp.getTimeStamp()
        val language = Enums.Language.EN.name.toUpperCase()
        val userId = AppPreferenceManager.getUserID()
        val key = TimeStamp.getMd5(timeStamp + userId + Constants.TIME_STAMP_KEY)
        if (Utils.isOnline(this)) {
            val apiClient = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
            val call = apiClient.getPetTypeList(key, language, timeStamp, userId)
            call.enqueue(object : Callback<PetsTypeResponse> {
                override fun onResponse(call: Call<PetsTypeResponse>, response: Response<PetsTypeResponse>?) {
                    if (response != null && response.isSuccessful() && response.body() != null) {
                        if (response.body().list != null) {
                            petsTypeList.clear()
                            petsTypeList.addAll(response.body().list)
                        }
                    } else {
                        val gson = GsonBuilder().create()
                        val mError: NormalResponse
                        try {
                            mError = gson.fromJson(response!!.errorBody().string(), NormalResponse::class.java)
                            Logger.errorLog("" + mError.getMessage())
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<PetsTypeResponse>, t: Throwable) {
                }
            })
        }
    }

    private fun getBreedList(petTypeId: String) {
        val timeStamp = TimeStamp.getTimeStamp()
        val language = Enums.Language.EN.name.toUpperCase()
        val userId = AppPreferenceManager.getUserID()
        val key = TimeStamp.getMd5(timeStamp + userId + petTypeId + Constants.TIME_STAMP_KEY)
        if (Utils.isOnline(this)) {
            val apiClient = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
            val call = apiClient.getBreedList(key, language, timeStamp, userId, petTypeId)
            call.enqueue(object : Callback<BreedResponse> {
                override fun onResponse(call: Call<BreedResponse>, response: Response<BreedResponse>?) {
                    if (response != null && response.isSuccessful() && response.body() != null) {
                        if (response.body().list != null && response.body().list.size > 0) {
//                            breedList.clear()
//                            breedList.addAll(response.body().list)
                            showBreedDialog(response.body().list)
                        }
                    } else {
                        val gson = GsonBuilder().create()
                        val mError: NormalResponse
                        try {
                            mError = gson.fromJson(response!!.errorBody().string(), NormalResponse::class.java)
                            Logger.errorLog("" + mError.getMessage())
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<BreedResponse>, t: Throwable) {
                }
            })
        }
    }


    private fun showTypeDialog(list: ArrayList<PetsType>) {
        var firstLineArrays: Array<String> = Array(list.size) { "" }
        for (i in list.indices) {
            firstLineArrays[i] = list[i].typeName
        }
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val contentView = inflater.inflate(R.layout.single_dial_row, null)
        val mDialog = Dialog(this, R.style.dialogStyle)
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.setContentView(contentView)
        mDialog.setCancelable(true)
        mDialog.show()

        val numberPicker = mDialog.findViewById<NumberPicker>((R.id.numberPicker))
        var tvCancel = mDialog.findViewById<TextView>(R.id.tvCancel)
        var tvDone = mDialog.findViewById<TextView>(R.id.tvDone)

        numberPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        numberPicker.minValue = 0
        numberPicker.maxValue = firstLineArrays!!.size - 1
        numberPicker.displayedValues = firstLineArrays
        numberPicker.wrapSelectorWheel = false
        tvCancel.setOnClickListener { mDialog.cancel() }
        tvDone.setOnClickListener {
            mDialog.cancel()
            var petsType: PetsType = list[numberPicker.value]
            tvType!!.text = petsType.typeName
            petsTypeId = petsType.petsTypeId
        }
    }

    private fun showBreedDialog(list: ArrayList<Breed>) {
        var firstLineArrays: Array<String> = Array(list.size) { "" }
        for (i in list.indices) {
            firstLineArrays[i] = list[i].breed_name
        }
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val contentView = inflater.inflate(R.layout.single_dial_row, null)
        val mDialog = Dialog(this, R.style.dialogStyle)
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.setContentView(contentView)
        mDialog.setCancelable(true)
        mDialog.show()

        val numberPicker = mDialog.findViewById<NumberPicker>((R.id.numberPicker))
        var tvCancel = mDialog.findViewById<TextView>(R.id.tvCancel)
        var tvDone = mDialog.findViewById<TextView>(R.id.tvDone)

        numberPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        numberPicker.minValue = 0
        numberPicker.maxValue = firstLineArrays!!.size - 1
        numberPicker.displayedValues = firstLineArrays
        numberPicker.wrapSelectorWheel = false
        tvCancel.setOnClickListener { mDialog.cancel() }
        tvDone.setOnClickListener {
            mDialog.cancel()
            var breed: Breed = list[numberPicker.value]
            tvBreed!!.text = breed.breed_name
            breedId = breed.breed_id
        }
    }

    private fun showGenderDialog() {

        val fractionArr = resources.getStringArray(R.array.gender_arr)

        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val contentView = inflater.inflate(R.layout.single_dial_row, null)
        val mDialog = Dialog(this, R.style.dialogStyle)
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.setContentView(contentView)
        mDialog.setCancelable(true)
        mDialog.show()

        val numberPicker = mDialog.findViewById<NumberPicker>((R.id.numberPicker))
        var tvCancel = mDialog.findViewById<TextView>(R.id.tvCancel)
        var tvDone = mDialog.findViewById<TextView>(R.id.tvDone)

        numberPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        numberPicker.minValue = 0
        numberPicker.maxValue = fractionArr!!.size - 1
        numberPicker.displayedValues = fractionArr
        numberPicker.wrapSelectorWheel = false
        tvCancel.setOnClickListener { mDialog.cancel() }
        tvDone.setOnClickListener {
            mDialog.cancel()
            tvGender!!.text = fractionArr[numberPicker.value]
        }
    }


    private fun openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(this)
            startActivityForResult(intent, RC_AUTOCOMPLETE)
        } catch (e: GooglePlayServicesRepairableException) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.connectionStatusCode,
                    0 /* requestCode */).show()
        } catch (e: GooglePlayServicesNotAvailableException) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            val message = "Google Play Services is not available: " + GoogleApiAvailability.getInstance().getErrorString(e.errorCode)

            Log.e("Tag", message)
            Utils.showToast(message)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_AUTOCOMPLETE -> {
                if (resultCode == RESULT_OK) {
                    // Get the user's selected place from the Intent.
                    val place = PlaceAutocomplete.getPlace(this, data)
                    Log.i("TAG", "Place Selected: " + place.name)

                    val latLng = place.latLng
                    latitude = latLng.latitude
                    longitude = latLng.longitude
                    tvLocation?.setText(place.address)

                    Logger.errorLog(place.id + "\n" + place.placeTypes + "\n" + place.address + "\n" + place.locale)
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    val status = PlaceAutocomplete.getStatus(this, data)
                    Log.e("TAG", "Error: Status = " + status.toString())
                } else if (resultCode == RESULT_CANCELED) {
                    // Indicates that the activity closed before a selection was made. For example if
                    // the user pressed the back button.
                }
            }
        }
    }
}
