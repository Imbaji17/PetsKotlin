package com.pets.app.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import com.pets.app.R
import com.pets.app.adapters.PhotosAdapter
import com.pets.app.common.AppPreferenceManager
import com.pets.app.common.ApplicationsConstants
import com.pets.app.common.Constants
import com.pets.app.common.ImageSetter
import com.pets.app.interfaces.AddPhotoCallback
import com.pets.app.model.Breed
import com.pets.app.model.NormalResponse
import com.pets.app.model.PetsType
import com.pets.app.model.`object`.PhotosInfo
import com.pets.app.utilities.*
import com.pets.app.webservice.UploadImage
import khandroid.ext.apache.http.entity.mime.HttpMultipartMode
import khandroid.ext.apache.http.entity.mime.MultipartEntity
import khandroid.ext.apache.http.entity.mime.content.FileBody
import khandroid.ext.apache.http.entity.mime.content.StringBody
import kotlinx.android.synthetic.main.activity_add_pet.*
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
    private var mRecyclerView: RecyclerView? = null
    private var btnAddPet: Button? = null
    private var adapter: PhotosAdapter? = null
    private var photoList: ArrayList<Any>? = null
    private var selectedType: Int = 0
    private val RC_BREED: Int = 200
    private val RC_TYPE: Int = 100
    private var petsTypeId: String? = ""
    private var breedId: String? = ""
    private var certificateFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pet)

        initializeToolbar(this.getString(R.string.add_pet))
        initView()
        clickListeners()
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
        btnAddPet = findViewById(R.id.btnAddPet)

        val mGridLayoutManager = GridLayoutManager(this, 3)
        mGridLayoutManager.orientation = GridLayoutManager.VERTICAL
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
//                        if (Utils.isOnline(this@AddPetActivity))
//                            deleteImageApiCall(position)
                    } else {
                        photoList!!.removeAt(position)
                        adapter!!.notifyDataSetChanged()
                    }
                }
            }
        })

        photoList!!.add("")
        adapter!!.notifyDataSetChanged()
    }

    private fun clickListeners() {

        imgPet?.setOnClickListener(this)
        edtType?.setOnClickListener(this)
        edtBreed?.setOnClickListener(this)
        edtDOB?.setOnClickListener(this)
        btnUpload?.setOnClickListener(this)
        btnAddPet?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.imgView -> {
                selectedType = 1
                showTakeImagePopup()
            }
            R.id.edtType -> {
                val mIntent = Intent(this, SelectTypeActivity::class.java)
                mIntent.putExtra(ApplicationsConstants.NAVIGATION_TYPE, true)
                this.startActivityForResult(mIntent, RC_TYPE)
            }
            R.id.edtBreed -> {
                if (petsTypeId!!.isNotEmpty()) {
                    val mIntent = Intent(this, SelectTypeActivity::class.java)
                    mIntent.putExtra(ApplicationsConstants.NAVIGATION_TYPE, false)
                    mIntent.putExtra(ApplicationsConstants.DATA, petsTypeId)
                    this.startActivityForResult(mIntent, RC_BREED)
                } else {
                    Utils.showToast(this.getString(R.string.please_select_pet_type_first))
                }
            }
            R.id.edtDOB -> {
                DatePickerDialogFragment(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val date = "$dayOfMonth-$month-$year"
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
                        addPetApiCall()
                    } else {
                        Utils.showToast(this.getString(R.string.device_is_offline))
                    }
                }
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
        } else if (updatedImageFile == null) {
            Utils.showToast(this.getString(R.string.please_add_pet_image))
            return false
        }
        return true
    }

    private fun addPetApiCall() {

        var petName = edtName!!.text.toString().trim()
        var dob = DateFormatter.getFormattedDate(DateFormatter.dd_MMM_yyyy_str, edtDOB!!.text.toString().trim(), DateFormatter.yyyy_MM_dd_str)
        var desc = edtDesc!!.text.toString().trim()
        var gender = Constants.MALE
        if (radioGender?.checkedRadioButtonId == R.id.rbFemale) {
            gender = Constants.FEMALE
        }

        var actionName = "add_pets"
        var userId = AppPreferenceManager.getUserID()
        var timestamp = TimeStamp.getTimeStamp()
        var key = TimeStamp.getMd5(timestamp + userId + Constants.TIME_STAMP_KEY)

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
                        multipartEntity.addPart("pet_name", StringBody(petName))
                        multipartEntity.addPart("pets_type_id", StringBody(petsTypeId))
                        multipartEntity.addPart("breed_id", StringBody(breedId))
                        multipartEntity.addPart("dob", StringBody(dob))
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
                        val adoptionResponse: NormalResponse = Utils.getResponse(result.toString(), NormalResponse::class.java)
                        this@AddPetActivity.finish()
//                        if (adoptionResponse.result != null) {
//                            Utils.showToast(adoptionResponse.message)
//                            uploadImages(adoptionResponse.result)
//                        }
                    }
                }
            }.execute()
        }
    }
}
