package com.pets.app.activities.adoption

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.gson.GsonBuilder
import com.pets.app.R
import com.pets.app.activities.SelectTypeActivity
import com.pets.app.adapters.PhotosAdapter
import com.pets.app.common.*
import com.pets.app.interfaces.AddPhotoCallback
import com.pets.app.model.*
import com.pets.app.model.`object`.PhotosInfo
import com.pets.app.utilities.ImagePicker
import com.pets.app.utilities.Logger
import com.pets.app.utilities.TimeStamp
import com.pets.app.utilities.Utils
import com.pets.app.webservice.RestClient
import com.pets.app.webservice.UploadImage
import com.pets.app.webservice.WebServiceBuilder
import khandroid.ext.apache.http.entity.mime.HttpMultipartMode
import khandroid.ext.apache.http.entity.mime.MultipartEntity
import khandroid.ext.apache.http.entity.mime.content.FileBody
import khandroid.ext.apache.http.entity.mime.content.StringBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException

class AddAdoptionActivity : ImagePicker(), View.OnClickListener {

    private var ivProfile: ImageView? = null
    private var etPetName: EditText? = null
    private var rlType: RelativeLayout? = null
    private var tvType: TextView? = null
    private var rlBreed: RelativeLayout? = null
    private var tvBreed: TextView? = null
    private var etAge: EditText? = null
    private var rgGender: RadioGroup? = null
    private var btnUpload: Button? = null
    private var etDescription: EditText? = null
    private var btnAddAdoption: Button? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: PhotosAdapter? = null
    private var photoList = ArrayList<Any>()
    private var selectedType: Int = 0
    private var certificateFile: File? = null
    private var rlAddress: RelativeLayout? = null
    private var tvAddress: TextView? = null
    private val RC_AUTOCOMPLETE: Int = 100
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var petsTypeId: String? = ""
    private var breedId: String? = ""
    private val RC_BREED: Int = 101
    private val RC_TYPE: Int = 102

    companion object {
        private val TAG = AddAdoptionActivity::class.java.simpleName
        fun startActivity(activity: Activity, resultCode: Int) {
            val intent = Intent(activity, AddAdoptionActivity::class.java)
//            intent.putExtra(ApplicationsConstants.PETS_TYPE_ID, petsTypeId)
//            intent.putExtra(ApplicationsConstants.PETS_TYPE_NAME, petsTypeStr)
//            intent.putExtra(ApplicationsConstants.BREED_ID, breedId)
//            intent.putExtra(ApplicationsConstants.BREED_NAME, breedStr)
//            intent.putExtra(ApplicationsConstants.GENDER, gender)
//            intent.putExtra(ApplicationsConstants.DISTANCE, distance)
//            intent.putExtra(ApplicationsConstants.LATITUDE, latitude)
//            intent.putExtra(ApplicationsConstants.LONGITUDE, longitude)
//            intent.putExtra(ApplicationsConstants.LOCATION, location)
            activity.startActivityForResult(intent, resultCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_adoption)
        initializeToolbar(getString(R.string.add_adoption))
        initView()

    }

    fun initView() {
        ivProfile = findViewById(R.id.ivProfile)
        etPetName = findViewById(R.id.etPetName)
        rlType = findViewById(R.id.rlType)
        tvType = findViewById(R.id.tvType)
        rlBreed = findViewById(R.id.rlBreed)
        tvBreed = findViewById(R.id.tvBreed)
        etAge = findViewById(R.id.etAge)
        rgGender = findViewById(R.id.rgGender)
        btnUpload = findViewById(R.id.btnUpload)
        etDescription = findViewById(R.id.etDescription)
        btnAddAdoption = findViewById(R.id.btnAddAdoption)
        recyclerView = findViewById(R.id.recyclerView)
        rlAddress = findViewById(R.id.rlAddress)
        tvAddress = findViewById(R.id.tvAddress)

        ivProfile!!.setOnClickListener(this)
        btnUpload!!.setOnClickListener(this)
        btnAddAdoption!!.setOnClickListener(this)
        rlAddress!!.setOnClickListener(this)
        rlType!!.setOnClickListener(this)
        rlBreed!!.setOnClickListener(this)
        setPhotoAdapter()
    }

    private fun setPhotoAdapter() {
        photoList.add("")
        val mGridLayoutManager = GridLayoutManager(this, 3)
        mGridLayoutManager.orientation = GridLayoutManager.VERTICAL
        recyclerView?.layoutManager = mGridLayoutManager

        adapter = PhotosAdapter(this, photoList, false)
        recyclerView?.adapter = adapter
        adapter?.setItemClickListener(object : AddPhotoCallback {
            override fun onAddPhotoClick(position: Int) {
                selectedType = 2
                showTakeImagePopup()
            }

            override fun onDeleteClick(position: Int) {
                if (position != -1 && position < photoList.size) {
                    val photo = photoList.get(position) as PhotosInfo
                    if (photoList.get(photoList.size - 1) is PhotosInfo) {
                        photoList.add(getString(R.string.add_photo))
                    }
                    if (photo.url.contains("http")) {
//                        if (Utils.isOnline(this@AddPetActivity))
//                            deleteImageApiCall(position)
                    } else {
                        photoList.removeAt(position)
                        adapter!!.notifyDataSetChanged()
                    }
                }
            }
        })
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.ivProfile -> {
                selectedType = 0
                showTakeImagePopup()
            }

            R.id.btnUpload -> {
                selectedType = 1
                showTakeImagePopup()
            }

            R.id.rlAddress -> {
                openAutocompleteActivity()
            }
            R.id.btnAddAdoption -> {
                addAdoption()
            }

            R.id.rlType -> {
                SelectTypeActivity.startActivity(this, RC_TYPE, "", 0, "")
//                val mIntent = Intent(this, SelectTypeActivity::class.java)
//                mIntent.putExtra(ApplicationsConstants.NAVIGATION_TYPE, true)
//                this.startActivityForResult(mIntent, RC_TYPE)
            }
            R.id.rlBreed -> {
                if (petsTypeId!!.isNotEmpty()) {
                    SelectTypeActivity.startActivity(this, RC_BREED, "", 1, petsTypeId!!)
//                    val mIntent = Intent(this, SelectTypeActivity::class.java)
//                    mIntent.putExtra(ApplicationsConstants.NAVIGATION_TYPE, false)
//                    mIntent.putExtra(ApplicationsConstants.DATA, petsTypeId)
//                    this.startActivityForResult(mIntent, RC_BREED)
                } else {
                    Utils.showToast(this.getString(R.string.please_select_pet_type_first))
                }
            }
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

    fun isValid(): Boolean {

        return true
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_CROP_ACTIVITY -> {
                if (data != null) {
                    val result = com.theartofdev.edmodo.cropper.CropImage.getActivityResult(data)
                    val mCurrentPhotoPath = result.uri.path
                    val bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath)
                    if (selectedType == 0) {
                        updatedImageFile = File(mCurrentPhotoPath)
                        if (updatedImageFile.exists()) {
                            ImageSetter.loadRoundedImage(this, updatedImageFile, R.drawable.profile, ivProfile)
                        }
                    } else if (selectedType == 1) {
                        certificateFile = File(mCurrentPhotoPath)
                    } else {
                        val photo = PhotosInfo()
                        photo.url = mCurrentPhotoPath
                        photoList.size.minus(1).let { photoList.add(it, photo) }
                        if (photoList.size >= 3) {
                            photoList.removeAt(photoList.size - 1)
                        }
                        adapter!!.notifyDataSetChanged()
                    }
                }
            }

            RC_AUTOCOMPLETE -> {
                if (resultCode == RESULT_OK) {
                    // Get the user's selected place from the Intent.
                    val place = PlaceAutocomplete.getPlace(this, data)
                    Log.i("TAG", "Place Selected: " + place.name)
                    val latLng = place.latLng
                    latitude = latLng.latitude
                    longitude = latLng.longitude
                    tvAddress?.text = place.address
                    Logger.errorLog(place.id + "\n" + place.placeTypes + "\n" + place.address + "\n" + place.locale)
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    val status = PlaceAutocomplete.getStatus(this, data)
                    Log.e("TAG", "Error: Status = " + status.toString())
                } else if (resultCode == RESULT_CANCELED) {
                    // Indicates that the activity closed before a selection was made. For example if
                    // the user pressed the back button.
                }
            }

            RC_TYPE -> {
                if (data != null) {
                    val petsType = data.getSerializableExtra(ApplicationsConstants.DATA) as PetsType
                    if (petsType != null) {
                        petsTypeId = petsType.petsTypeId
                        tvType!!.text = petsType.typeName
                    }
                }
            }
            RC_BREED -> {
                if (data != null) {
                    val breed = data.getSerializableExtra(ApplicationsConstants.DATA) as Breed
                    if (breed != null) {
                        breedId = breed.breed_id
                        tvBreed!!.text = breed.breed_name
                    }
                }
            }
        }
    }

    private fun addAdoption() {

        var actionName = "add_edit_adoption"
        var userId = AppPreferenceManager.getUserID()
        var timestamp = TimeStamp.getTimeStamp()
        var languageCode = Enums.Language.EN.name
        var key = TimeStamp.getMd5(timestamp + userId + Constants.TIME_STAMP_KEY)
        var user = AppPreferenceManager.getUser()

        var petName = etPetName!!.text.toString().trim()
        var type = tvType!!.text.toString().trim()
        var breed = tvBreed!!.text.toString().trim()
        var age = etAge!!.text.toString().trim()
        var address = tvAddress!!.text.toString().trim()
        var description = etDescription!!.text.toString().trim()
        var gender = ""
        if (TextUtils.isEmpty(petName)) {
            Utils.showToast(getString(R.string.please_enter_pet_name))
        } else if (TextUtils.isEmpty(type)) {
            Utils.showToast(getString(R.string.please_select_pet_type))
        } else if (TextUtils.isEmpty(breed)) {
            Utils.showToast(getString(R.string.please_select_breed))
        } else if (rgGender!!.checkedRadioButtonId == -1) {
            Utils.showToast(getString(R.string.please_select_gender))
        } else if (TextUtils.isEmpty(description)) {
            Utils.showToast(getString(R.string.please_enter_pet_description))
        } else {

            gender = if (rgGender!!.checkedRadioButtonId == R.id.rbMale) {
                "M"
            } else {
                "F"
            }

            if (updatedImageFile != null || certificateFile != null) {

                object : AsyncTask<Void, Void, String>() {
                    private var response: String? = null
                    override fun onPreExecute() {
                        super.onPreExecute()
                        showProgressBar()
                    }

                    override fun doInBackground(vararg params: Void): String? {
                        try {
                            val multipartEntity = MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE)
                            multipartEntity.addPart("user_id", StringBody(userId!!))
                            multipartEntity.addPart("key", StringBody(key!!))
                            multipartEntity.addPart("timestamp", StringBody(timestamp!!))
                            multipartEntity.addPart("language_code", StringBody(languageCode))
//                        multipartEntity.addPart("adoption_id", StringBody(languageCode))
                            multipartEntity.addPart("contact_person", StringBody(user.name))
                            multipartEntity.addPart("contact_no", StringBody(user.phone_number))
                            if (certificateFile != null)
                                multipartEntity.addPart("certificate_image", FileBody(certificateFile, "userFile1/jpg"))
                            multipartEntity.addPart("address", StringBody(address))
                            multipartEntity.addPart("description", StringBody(description))
                            multipartEntity.addPart("lat", StringBody(latitude.toString()))
                            multipartEntity.addPart("lng", StringBody(longitude.toString()))
                            multipartEntity.addPart("pets_type_id", StringBody(petsTypeId))
                            multipartEntity.addPart("pet_name", StringBody(petName))
                            multipartEntity.addPart("breed_id", StringBody(breedId))
                            if (updatedImageFile != null)
                                multipartEntity.addPart("profile_image", FileBody(updatedImageFile, "userFile1/jpg"))
                            multipartEntity.addPart("age", StringBody(age))
                            multipartEntity.addPart("gender", StringBody(gender))
                            response = UploadImage.uploadImage(Constants.API_BASE_URL + actionName, multipartEntity)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        return response
                    }

                    override fun onPostExecute(result: String?) {
                        hideProgressBar()
                        Logger.errorLog("Response ### " + result!!)
                        print("Response #### " + result)
                        if (result != null) {
                            val adoptionResponse: AdoptionResponse = Utils.getResponse(result.toString(), AdoptionResponse::class.java)
                            if (adoptionResponse.result != null) {
                                Utils.showToast(adoptionResponse.message)
//                            val user: LoginDetails = AppPreferenceManager.getUser()
//                            user.profile_image = normalResponse.result.profile_image
//                            AppPreferenceManager.saveUser(user)
                                uploadImages(adoptionResponse.result)
//                                finish()
                            }
                        }
                    }
                }.execute()

            } else {
                var adoption = Adoption()
                adoption.userId = userId
                adoption.key = key
                adoption.timestamp = timestamp
//                adoption.adoptionId = ""
                adoption.contactPerson = user.name
                adoption.contactNo = user.phone_number
                adoption.address = address
                adoption.lat = latitude.toString()
                adoption.lng = longitude.toString()
                adoption.description = description
                adoption.petsTypeId = petsTypeId
                adoption.petName = petName
                adoption.breedId = breedId
                adoption.age = age
                adoption.gender = gender

                showProgressBar()
                val api = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
                val call = api.addEditAdoption(adoption)
                call.enqueue(object : Callback<AdoptionResponse> {
                    override fun onResponse(call: Call<AdoptionResponse>?, response: Response<AdoptionResponse>?) {
                        hideProgressBar()
                        if (response != null) {
                            if (response.body() != null && response.isSuccessful) {
                                uploadImages(response.body().result)
                            } else if (response.code() == 403) {
                                val gson = GsonBuilder().create()
                                val mError: NormalResponse
                                try {
                                    mError = gson.fromJson(response.errorBody().string(), NormalResponse::class.java)
                                    Utils.showToast("" + mError.message)
                                } catch (e: IOException) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<AdoptionResponse>?, t: Throwable?) {
                        hideProgressBar()
                    }
                })
            }
        }
    }

    fun uploadImages(adoption: Adoption) {
        if (photoList.size > 0) {
            for (i in photoList.indices) {
                var obj = photoList.get(index = i)
                if (obj is PhotosInfo) {
                    var file: File = File(obj.url)
                    addImages(Enums.Favourite.ADOPTION.name, adoption.adoptionId, file)
                }
            }
        }


        val mIntent = Intent()
//        mIntent.putExtra(ApplicationsConstants.DATA, breed)
        setResult(Activity.RESULT_OK, mIntent)
        finish()

    }


    private fun addImages(type: String, typeId: String, file: File) {

        var actionName = "add_type_wise_images"
        var userId = AppPreferenceManager.getUserID()
        var timestamp = TimeStamp.getTimeStamp()
        var languageCode = Enums.Language.EN.name
        var key = TimeStamp.getMd5(timestamp + userId + type + typeId + Constants.TIME_STAMP_KEY)


        object : AsyncTask<Void, Void, String>() {
            private var response: String? = null
            override fun onPreExecute() {
                super.onPreExecute()
                showProgressBar()
            }

            override fun doInBackground(vararg params: Void): String? {
                try {
                    val multipartEntity = MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE)
                    multipartEntity.addPart("user_id", StringBody(userId!!))
                    multipartEntity.addPart("key", StringBody(key!!))
                    multipartEntity.addPart("timestamp", StringBody(timestamp!!))
                    multipartEntity.addPart("language_code", StringBody(languageCode))
                    multipartEntity.addPart("type", StringBody(type))
                    multipartEntity.addPart("type_id", StringBody(typeId))
                    if (file != null)
                        multipartEntity.addPart("type_image", FileBody(file, "userFile1/jpg"))
                    response = UploadImage.uploadImage(Constants.API_BASE_URL + actionName, multipartEntity)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                return response
            }

            override fun onPostExecute(result: String?) {
                hideProgressBar()
                Logger.errorLog("Response ### " + result!!)
                print("Response #### " + result)
                if (result != null) {
                    val imageResponse: ImageResponse = Utils.getResponse(result.toString(), ImageResponse::class.java)
                    if (imageResponse.result != null) {
                        Utils.showToast(imageResponse.message)
//                            val user: LoginDetails = AppPreferenceManager.getUser()
//                            user.profile_image = normalResponse.result.profile_image
//                            AppPreferenceManager.saveUser(user)
//                        finish()
                    }
                }
            }
        }.execute()

    }

}
