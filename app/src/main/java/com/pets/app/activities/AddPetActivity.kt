package com.pets.app.activities

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.pets.app.R
import com.pets.app.adapters.PhotosAdapter
import com.pets.app.common.AppPreferenceManager
import com.pets.app.common.ApplicationsConstants
import com.pets.app.common.Constants
import com.pets.app.common.ImageSetter
import com.pets.app.interfaces.AddPhotoCallback
import com.pets.app.model.Breed
import com.pets.app.model.ImageResponse
import com.pets.app.model.PetResponse
import com.pets.app.model.PetsType
import com.pets.app.model.`object`.PetDetails
import com.pets.app.model.`object`.PhotosInfo
import com.pets.app.model.request.PetUpdateRequest
import com.pets.app.utilities.*
import com.pets.app.webservice.RestClient
import com.pets.app.webservice.UploadImage
import com.pets.app.webservice.WebServiceBuilder
import khandroid.ext.apache.http.entity.mime.HttpMultipartMode
import khandroid.ext.apache.http.entity.mime.MultipartEntity
import khandroid.ext.apache.http.entity.mime.content.FileBody
import khandroid.ext.apache.http.entity.mime.content.StringBody
import kotlinx.android.synthetic.main.activity_add_pet.*
import kotlinx.android.synthetic.main.app_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException

class AddPetActivity : ImagePicker(), View.OnClickListener {

    private var imgPet: ImageView? = null
    private var edtName: EditText? = null
    private var edtType: EditText? = null
    private var edtBreed: EditText? = null
    private var edtDOB: EditText? = null
    private var radioGender: RadioGroup? = null
    private var btnUpload: Button? = null
    private var edtDesc: EditText? = null
    private var checkMatch: CheckBox? = null
    private var btnAddPet: Button? = null
    private var tvDelete: TextView? = null
    private var adapter: PhotosAdapter? = null
    private var photoList: ArrayList<Any>? = null
    private var selectedType: Int = 0
    private val RC_BREED: Int = 200
    private val RC_TYPE: Int = 100
    private var petsTypeId: String? = ""
    private var breedId: String? = ""
    private var certificateFile: File? = null
    private var petObj: PetDetails? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pet)

        initializeToolbar(this.getString(R.string.add_pet))
        initView()
        clickListeners()
        getIntentData()
    }

    private fun initView() {

        photoList = ArrayList()

        imgPet = findViewById(R.id.imgView)
        edtName = findViewById(R.id.edtName)
        edtType = findViewById(R.id.edtType)
        edtBreed = findViewById(R.id.edtBreed)
        edtDOB = findViewById(R.id.edtDOB)
        radioGender = findViewById(R.id.radioGender)
        btnUpload = findViewById(R.id.btnUpload)
        edtDesc = findViewById(R.id.edtDescription)
        mRecyclerView = findViewById(R.id.recyclerView)
        checkMatch = findViewById(R.id.checkMatch)
        btnAddPet = findViewById(R.id.btnAddPet)
        tvDelete = findViewById(R.id.tvDelete)

        val mGridLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        mGridLayoutManager.orientation = GridLayoutManager.HORIZONTAL
        mRecyclerView?.layoutManager = mGridLayoutManager

        adapter = PhotosAdapter(this, photoList, false)
        mRecyclerView?.adapter = adapter
        adapter?.setItemClickListener(object : AddPhotoCallback {
            override fun onAddPhotoClick(position: Int) {
                selectedType = 2
                showTakeImagePopup()
            }

            override fun onDeleteClick(position: Int) {
                if (position != -1 && position < photoList!!.size) {
                    val photo = photoList!!.get(position) as PhotosInfo
                    if (photoList!!.get(photoList!!.size - 1) is PhotosInfo) {
                        photoList!!.add(getString(R.string.add_photo))
                    }
                    if (photo.url.contains("http")) {
                        if (Utils.isOnline(this@AddPetActivity))
                            deletePetImageApiCall(photo, position)
                    } else {
                        photoList!!.removeAt(position)
                        adapter!!.notifyDataSetChanged()
                    }
                }
            }
        })
    }

    private fun clickListeners() {

        imgPet?.setOnClickListener(this)
        edtType?.setOnClickListener(this)
        edtBreed?.setOnClickListener(this)
        edtDOB?.setOnClickListener(this)
        btnUpload?.setOnClickListener(this)
        btnAddPet?.setOnClickListener(this)
        tvDelete?.setOnClickListener(this)
    }

    private fun getIntentData() {

        tvDelete!!.visibility = View.GONE
        if (intent.hasExtra(ApplicationsConstants.DATA)) {
            tvToolbar.text = this.getText(R.string.edit_pet)
            btnAddPet!!.text = this.getText(R.string.update)
            tvDelete!!.visibility = View.VISIBLE
            petObj = intent.getSerializableExtra(ApplicationsConstants.DATA) as PetDetails

            edtName?.setText(if (!TextUtils.isEmpty(petObj!!.pet_name)) petObj!!.pet_name else "")
            edtType?.setText(if (!TextUtils.isEmpty(petObj!!.petsType!!.typeName)) petObj!!.petsType!!.typeName else "")
            edtBreed?.setText(if (!TextUtils.isEmpty(petObj!!.breed!!.breed_name)) petObj!!.breed!!.breed_name else "")
            edtDesc?.setText(if (!TextUtils.isEmpty(petObj!!.description)) petObj!!.description else "")

            if (!TextUtils.isEmpty(petObj!!.dob)) {
                edtDOB?.setText(DateFormatter.getFormattedDate(DateFormatter.yyyy_MM_dd_str, petObj!!.dob, DateFormatter.dd_MMM_yyyy_str))
            }

            petsTypeId = if (!TextUtils.isEmpty(petObj!!.petsType!!.petsTypeId)) petObj!!.petsType!!.petsTypeId else ""
            breedId = if (!TextUtils.isEmpty(petObj!!.breed!!.breed_id)) petObj!!.breed!!.breed_id else ""

            if (!TextUtils.isEmpty(petObj!!.is_ready_match)) {
                checkMatch!!.isChecked = petObj!!.isReadyForMatch
            }

            if (!TextUtils.isEmpty(petObj!!.pet_image)) {
                imgCamera.visibility = View.GONE
                ImageSetter.loadRoundedImage(this, petObj!!.pet_image, R.drawable.dog, imgPet)
            }

            if (!TextUtils.isEmpty(petObj!!.gender) && petObj!!.gender.equals(Constants.FEMALE, true)) {
                radioGender?.check(R.id.rbFemale)
            }

            if (!TextUtils.isEmpty(petObj!!.certificate_image)) {
                tvUploadUrl?.text = petObj!!.certificate_image
                tvUploadUrl?.visibility = View.VISIBLE
            }
        }

        if (petObj != null && petObj!!.petImages != null && petObj!!.petImages.isNotEmpty()) {

            for (obj in petObj!!.petImages) {
                val photo = PhotosInfo()
                photo.url = obj.pet_image
                photo.imageId = obj.pet_image_id
                photoList!!.add(photo)
            }
        } else {
            photoList!!.add("")
        }
        adapter!!.notifyDataSetChanged()
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.imgView -> {
                selectedType = 1
                showTakeImagePopup()
            }
            R.id.edtType -> {
                SelectTypeActivity.startActivity(this, RC_TYPE, petsTypeId!!, 0, "")
            }
            R.id.edtBreed -> {
                if (petsTypeId!!.isNotEmpty()) {
                    SelectTypeActivity.startActivity(this, RC_BREED, breedId!!, 1, petsTypeId!!)
                } else {
                    Utils.showToast(this.getString(R.string.please_select_pet_type_first))
                }
            }
            R.id.edtDOB -> {
                DatePickerDialogFragment(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val cmgMonth = month + 1
                    val date = "$dayOfMonth-$cmgMonth-$year"
                    val strDate = DateFormatter.getFormattedDate(DateFormatter.dd_MM_yyyy_str, date, DateFormatter.dd_MMM_yyyy_str)
                    edtDOB?.setText(strDate)
                }).show(supportFragmentManager, this.getString(R.string.select_date))
            }
            R.id.btnUpload -> {
                selectedType = 3
                showTakeImagePopup()
            }
            R.id.btnAddPet -> {
                if (checkValidations()) {
                    if (Utils.isOnline(this)) {
                        addUpdatePetApiCall()
                    } else {
                        Utils.showToast(this.getString(R.string.device_is_offline))
                    }
                }
            }
            R.id.tvDelete -> {
                ConfirmationActivity.startActivity(this)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_CROP_ACTIVITY -> {
                if (data != null) {
                    val result = com.theartofdev.edmodo.cropper.CropImage.getActivityResult(data)
                    val mCurrentPhotoPath = result.uri.path
                    val bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath)
                    imgCamera.visibility = View.GONE
                    if (selectedType == 1) {
                        updatedImageFile = File(mCurrentPhotoPath)
                        if (updatedImageFile.exists()) {
                            ImageSetter.loadRoundedImage(this, updatedImageFile, R.drawable.profile, imgPet)
                        }
                    } else if (selectedType == 2) {
                        val photo = PhotosInfo()
                        photo.url = mCurrentPhotoPath
                        photoList?.size?.minus(1)?.let { photoList!!.add(it, photo) }
                        if (photoList!!.size >= 3) {
                            photoList!!.removeAt(photoList!!.size - 1)
                        }
                        adapter!!.notifyDataSetChanged()
                    } else {
                        certificateFile = File(mCurrentPhotoPath)
                        tvUploadUrl.text = mCurrentPhotoPath
                        tvUploadUrl.visibility = View.VISIBLE
                        val path = mCurrentPhotoPath.split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                        tvUploadUrl.text = path[path.size - 1]
                    }
                }
            }
            RC_TYPE -> {
                if (data != null) {
                    val petsType = data.getSerializableExtra(ApplicationsConstants.DATA) as PetsType
                    if (petsType != null) {
                        if (!petsTypeId.equals(petsType.petsTypeId)) {
                            breedId = ""
                            edtBreed!!.setText("")
                        }
                        petsTypeId = petsType.petsTypeId
                        edtType!!.setText(petsType.typeName)
                    }
                }
            }
            RC_BREED -> {
                if (data != null) {
                    val breed = data.getSerializableExtra(ApplicationsConstants.DATA) as Breed
                    if (breed != null) {
                        breedId = breed.breed_id
                        edtBreed!!.setText(breed.breed_name)
                    }
                }
            }
        }
    }

    private fun checkValidations(): Boolean {

        if (TextUtils.isEmpty(edtName?.text.toString().trim())) {
            edtName?.error = this.getString(R.string.please_enter_pet_name)
            edtName?.requestFocus()
            return false
        } else if (TextUtils.isEmpty(edtType?.text.toString().trim())) {
            Utils.showToast(this.getString(R.string.please_select_pet_type))
            return false
        } else if (TextUtils.isEmpty(edtBreed?.text.toString().trim())) {
            Utils.showToast(this.getString(R.string.please_select_breed))
            return false
        } else if (TextUtils.isEmpty(edtDOB?.text.toString().trim())) {
            Utils.showToast(this.getString(R.string.please_select_dob))
            return false
        } else if (TextUtils.isEmpty(edtDesc?.text.toString().trim())) {
            edtDesc?.error = this.getString(R.string.please_enter_pet_description)
            edtDesc?.requestFocus()
            return false
        }

        if (petObj == null) {
            if (updatedImageFile == null) {
                Utils.showToast(this.getString(R.string.please_add_pet_image))
                return false
            }
        }

        return true
    }

    private fun addUpdatePetApiCall() {

        val petName = edtName!!.text.toString().trim()
        val dob = DateFormatter.getFormattedDate(DateFormatter.dd_MMM_yyyy_str, edtDOB!!.text.toString().trim(), DateFormatter.yyyy_MM_dd_str)
        val desc = edtDesc!!.text.toString().trim()

        var gender = Constants.MALE
        if (radioGender?.checkedRadioButtonId == R.id.rbFemale) {
            gender = Constants.FEMALE
        }

        var match = Constants.NO
        if (checkMatch!!.isChecked) {
            match = Constants.YES
        }

        val actionName = "add_pets"
        val userId = AppPreferenceManager.getUserID()
        val timestamp = TimeStamp.getTimeStamp()
        val key = TimeStamp.getMd5(timestamp + userId + Constants.TIME_STAMP_KEY)

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
                        if (petObj != null)
                            multipartEntity.addPart("pet_id", StringBody(petObj!!.pet_id))
                        multipartEntity.addPart("key", StringBody(key!!))
                        multipartEntity.addPart("timestamp", StringBody(timestamp!!))
                        multipartEntity.addPart("pet_name", StringBody(petName))
                        multipartEntity.addPart("pets_type_id", StringBody(petsTypeId))
                        multipartEntity.addPart("breed_id", StringBody(breedId))
                        multipartEntity.addPart("dob", StringBody(dob))
                        multipartEntity.addPart("is_ready_match", StringBody(match))
                        multipartEntity.addPart("gender", StringBody(gender))
                        multipartEntity.addPart("description", StringBody(desc))
                        if (certificateFile != null)
                            multipartEntity.addPart("certificate_image", FileBody(certificateFile, "userFile1/jpg"))
                        if (updatedImageFile != null)
                            multipartEntity.addPart("pet_image", FileBody(updatedImageFile, "userFile1/jpg"))
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
                        val response: PetResponse = Utils.getResponse(result.toString(), PetResponse::class.java)
                        if (response.result != null) {
                            uploadImages(response.result)
                        }
                    }
                }
            }.execute()
        } else {

            val request = PetUpdateRequest()
            request.setUserId(userId)
            if (petObj != null)
                request.setPet_id(petObj!!.pet_id)
            request.setTimestamp(timestamp)
            request.setKey(key)
            request.setPet_name(petName)
            request.setPets_type_id(petsTypeId)
            request.setBreed_id(breedId)
            request.setDob(dob)
            request.setGender(gender)
            request.setDescription(desc)
            request.setIs_ready_match(match)

            showProgressBar()
            val apiClient = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
            val call = apiClient.addUpdatePet(request)
            call.enqueue(object : Callback<PetResponse> {
                override fun onResponse(call: Call<PetResponse>, response: Response<PetResponse>?) {
                    hideProgressBar()
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body() != null && response.body().result != null) {
                                uploadImages(response.body().result)
                            }
                        } else {
                            Utils.showErrorToast(response.errorBody())
                        }
                    }
                }

                override fun onFailure(call: Call<PetResponse>, t: Throwable) {
                    hideProgressBar()
                }
            })
        }
    }

    private fun uploadImages(petDetails: PetDetails?) {

        if (photoList!!.isNotEmpty()) {
            for (i in photoList!!.indices) {
                val obj = photoList!!.get(index = i)
                if (obj is PhotosInfo) {
                    if (!obj.url.contains("http")) {
                        val file = File(obj.url)
                        addImages(petDetails!!.pet_id, file)
                    }
                }
            }
        }

        val mIntent = Intent()
        mIntent.putExtra(ApplicationsConstants.DATA, true)
        setResult(Activity.RESULT_OK, mIntent)
        this.finish()
    }

    private fun addImages(petId: String, file: File) {

        val actionName = "add_pet_images"
        val userId = AppPreferenceManager.getUserID()
        val timestamp = TimeStamp.getTimeStamp()
        val key = TimeStamp.getMd5(timestamp + userId + petId + Constants.TIME_STAMP_KEY)

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
                    multipartEntity.addPart("pet_id", StringBody(petId))
                    if (file != null)
                        multipartEntity.addPart("pet_image", FileBody(file, "userFile1/jpg"))
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
                    }
                }
            }
        }.execute()
    }

    private fun deletePetImageApiCall(photo: PhotosInfo, position: Int) {

        val userId = AppPreferenceManager.getUserID()
        val timeStamp = TimeStamp.getTimeStamp()
        val key = TimeStamp.getMd5(timeStamp + userId + petObj!!.pet_id + photo.imageId + Constants.TIME_STAMP_KEY)

        showProgressBar()
        val apiClient = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
        val call = apiClient.deletePetImage(userId, petObj!!.pet_id, photo.imageId, timeStamp, key)
        call.enqueue(object : Callback<PetResponse> {
            override fun onResponse(call: Call<PetResponse>, response: Response<PetResponse>?) {
                hideProgressBar()
                if (response != null) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            checkResponse(response.body(), position)
                        }
                    } else {
                        Utils.showErrorToast(response.errorBody())
                    }
                }
            }

            override fun onFailure(call: Call<PetResponse>, t: Throwable) {
                hideProgressBar()
            }
        })
    }

    private fun checkResponse(response: PetResponse?, position: Int) {
        Utils.showToast(this.getString(R.string.image_deleted_successfully))
        photoList!!.removeAt(position)
        adapter!!.notifyDataSetChanged()
    }
}
