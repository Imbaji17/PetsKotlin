package com.pets.app.activities.adoption

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.pets.app.R
import com.pets.app.adapters.PhotosAdapter
import com.pets.app.common.AppPreferenceManager
import com.pets.app.common.Constants
import com.pets.app.common.Enums
import com.pets.app.common.ImageSetter
import com.pets.app.initialsetup.BaseActivity
import com.pets.app.interfaces.AddPhotoCallback
import com.pets.app.model.LoginResponse
import com.pets.app.model.`object`.LoginDetails
import com.pets.app.model.`object`.PhotosInfo
import com.pets.app.utilities.ImagePicker
import com.pets.app.utilities.Logger
import com.pets.app.utilities.TimeStamp
import com.pets.app.utilities.Utils
import com.pets.app.webservice.UploadImage
import khandroid.ext.apache.http.entity.mime.HttpMultipartMode
import khandroid.ext.apache.http.entity.mime.MultipartEntity
import khandroid.ext.apache.http.entity.mime.content.FileBody
import khandroid.ext.apache.http.entity.mime.content.StringBody
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
    private var photoList: ArrayList<Any>? = null
    private var selectedType: Int = 0
    private var certificateFile: File? = null

    companion object {
        private val TAG = AddAdoptionActivity::class.java.simpleName
        fun startActivity(activity: Activity) {
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
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_adoption)
        initializeToolbar(getString(R.string.add_adoption))
        initView()
        setPhotoAdapter()
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

        ivProfile!!.setOnClickListener(this)
        btnUpload!!.setOnClickListener(this)
        btnAddAdoption!!.setOnClickListener(this)
    }

    private fun setPhotoAdapter() {
        photoList!!.add("")
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

            R.id.btnAddAdoption -> {
//                addAdoption()
            }
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
                        photoList?.size?.minus(1)?.let { photoList!!.add(it, photo) }
                        if (photoList!!.size >= 3) {
                            photoList!!.removeAt(photoList!!.size - 1)
                        }
                        adapter!!.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun addAdoption() {

        object : AsyncTask<Void, Void, String>() {

            private var actionName: String? = null
            private var userId: String? = null
            private var languageCode: String? = null
            private var timestamp: String? = null
            private var key: String? = null
            private var response: String? = null

            override fun onPreExecute() {
                super.onPreExecute()
                showProgressBar()
                actionName = "add_edit_adoption"
                userId = AppPreferenceManager.getUserID()
                timestamp = TimeStamp.getTimeStamp()
                languageCode = Enums.Language.EN.name
                key = TimeStamp.getMd5(timestamp + userId + Constants.TIME_STAMP_KEY)
            }

            override fun doInBackground(vararg params: Void): String? {
                try {
                    val multipartEntity = MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE)
                    multipartEntity.addPart("user_id", StringBody(userId!!))
                    multipartEntity.addPart("profile_image", FileBody(updatedImageFile, "userFile1/jpg"))
                    multipartEntity.addPart("language_code", StringBody(languageCode))
                    multipartEntity.addPart("timestamp", StringBody(timestamp!!))
                    multipartEntity.addPart("key", StringBody(key!!))

                    response = UploadImage.uploadImage(Constants.API_BASE_URL + actionName!!, multipartEntity)
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
                    val normalResponse: LoginResponse = Utils.getResponse(result.toString(), LoginResponse::class.java)
                    if (normalResponse.result != null) {
                        Utils.showToast(normalResponse.message)
                        val user: LoginDetails = AppPreferenceManager.getUser()
                        user.profile_image = normalResponse.result.profile_image
                        AppPreferenceManager.saveUser(user)
                    }
                }
            }
        }.execute()
    }
}
