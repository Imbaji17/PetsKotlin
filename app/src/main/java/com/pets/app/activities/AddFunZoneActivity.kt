package com.pets.app.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.pets.app.R
import com.pets.app.common.AppPreferenceManager
import com.pets.app.common.Constants
import com.pets.app.common.Enums
import com.pets.app.common.ImageSetter
import com.pets.app.initialsetup.BaseActivity
import com.pets.app.model.AdoptionResponse
import com.pets.app.utilities.ImagePicker
import com.pets.app.utilities.Logger
import com.pets.app.utilities.TimeStamp
import com.pets.app.utilities.Utils
import com.pets.app.webservice.UploadImage
import khandroid.ext.apache.http.entity.mime.HttpMultipartMode
import khandroid.ext.apache.http.entity.mime.MultipartEntity
import khandroid.ext.apache.http.entity.mime.content.FileBody
import khandroid.ext.apache.http.entity.mime.content.StringBody
import java.io.*


class AddFunZoneActivity : ImagePicker(), View.OnClickListener {

    private var ivPost: ImageView? = null
    private var tvAddImage: TextView? = null
    private var etTitle: EditText? = null
    private var etContactPerson: EditText? = null
    private var etPhoneNumber: EditText? = null
    private var etEmail: EditText? = null
    private var etAddress: EditText? = null
    private var etDescription: EditText? = null
    private var btnSave: Button? = null
    private val RC_TAKE_VIDEO = 100
    private val RC_AUTOCOMPLETE: Int = 101
    private var videoFile: File? = null
    private var videoThumbFile: File? = null
    private var selectedType: Int = 0
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    companion object {
        private val TAG = AddFunZoneActivity::class.java.simpleName
        fun startActivity(activity: Activity, requestCode: Int) {
            val intent = Intent(activity, AddFunZoneActivity::class.java)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_fun_zone)
        initView()
    }

    private fun initView() {
        ivPost = findViewById(R.id.ivPost)
        tvAddImage = findViewById(R.id.tvAddImage)
        etTitle = findViewById(R.id.etTitle)
        etContactPerson = findViewById(R.id.etContactPerson)
        etPhoneNumber = findViewById(R.id.etPhoneNumber)
        etEmail = findViewById(R.id.etEmail)
        etAddress = findViewById(R.id.etAddress)
        etDescription = findViewById(R.id.etDescription)
        btnSave = findViewById(R.id.btnSave)

        ivPost!!.setOnClickListener(this)
        btnSave!!.setOnClickListener(this)
        etAddress!!.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.ivPost -> {
                showDialog()
            }

            R.id.etAddress -> {
                openAutocompleteActivity()
            }
            R.id.btnSave -> {
                addFunZone()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_TAKE_VIDEO -> {
                if (data != null) {
//                    mVideoView.setVideoURI(videoUri)
                    val videoUri = data.data
                    videoFile = File(videoUri.path);
                    val filePath = videoUri.path
                    Log.d(TAG, "Video path is: " + filePath)
                    tvAddImage!!.visibility = View.VISIBLE

                    val thumb = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Images.Thumbnails.MINI_KIND)
                    videoThumbFile = bitmapToFile(thumb)
                    if (videoThumbFile!!.exists()) {
                        ImageSetter.loadRoundedImage(this, videoThumbFile, R.drawable.profile, ivPost)
                    }
                }
            }
            RC_CROP_ACTIVITY -> {
                if (data != null) {
                    val result = com.theartofdev.edmodo.cropper.CropImage.getActivityResult(data)
                    val mCurrentPhotoPath = result.uri.path
                    updatedImageFile = File(mCurrentPhotoPath)
                    if (updatedImageFile.exists()) {
                        ImageSetter.loadRoundedImage(this, updatedImageFile, R.drawable.profile, ivPost)
                    }
                    tvAddImage!!.visibility = View.VISIBLE
                }
            }

            RC_AUTOCOMPLETE -> {
                if (resultCode == BaseActivity.RESULT_OK) {
                    // Get the user's selected place from the Intent.
                    val place = PlaceAutocomplete.getPlace(this, data)
                    Log.i("TAG", "Place Selected: " + place.name)
                    val latLng = place.latLng
                    latitude = latLng.latitude
                    longitude = latLng.longitude
                    etAddress?.setText(place.address)
                    Logger.errorLog(place.id + "\n" + place.placeTypes + "\n" + place.address + "\n" + place.locale)
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    val status = PlaceAutocomplete.getStatus(this, data)
                    Log.e("TAG", "Error: Status = " + status.toString())
                } else if (resultCode == BaseActivity.RESULT_CANCELED) {
                    // Indicates that the activity closed before a selection was made. For example if
                    // the user pressed the back button.
                }
            }
        }
    }


    private fun showDialog() {
        val items = arrayOf<CharSequence>(getString(R.string.add_image), getString(R.string.add_video), getString(R.string.cancel))
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.select_photo))
        builder.setItems(items) { dialog, item ->
            when (item) {
                0 -> {
                    selectedType = 1
                    openGallery()
                }
                1 -> {
                    selectedType = 2
                    val takeVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                    startActivityForResult(takeVideoIntent, RC_TAKE_VIDEO)
                }
                3 -> {
                }
            }
        }
        val alert = builder.create()
        alert.show()
    }

    fun bitmapToFile(bmp: Bitmap): File? {
        try {
            val f = File(cacheDir, "temporary_file.jpg")
            f.createNewFile()
            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
            val bitmapdata = bos.toByteArray()
            //write the bytes in file
            val fos = FileOutputStream(f)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
            return f
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return null
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }

    private fun addFunZone() {

        var actionName = "add_edit_fun_zone"
        var userId = AppPreferenceManager.getUserID()
        var timestamp = TimeStamp.getTimeStamp()
        var languageCode = Enums.Language.EN.name
        var key = TimeStamp.getMd5(timestamp + userId + Constants.TIME_STAMP_KEY)
        var user = AppPreferenceManager.getUser()

        var title = etTitle!!.text.toString()
        var contactPerson = etContactPerson!!.text.toString()
        var phoneNumber = etPhoneNumber!!.text.toString()
        var emailAddress = etEmail!!.text.toString()
        var address = etAddress!!.text.toString()
        var description = etDescription!!.text.toString()

        if (TextUtils.isEmpty(title)) {
            Utils.showToast(getString(R.string.please_enter_title))
        } else if (TextUtils.isEmpty(contactPerson)) {
            Utils.showToast(getString(R.string.please_enter_contact_person))
        } else if (TextUtils.isEmpty(phoneNumber)) {
            Utils.showToast(getString(R.string.please_enter_contact_number))
        } else if (TextUtils.isEmpty(emailAddress)) {
            Utils.showToast(getString(R.string.please_enter_email))
        } else if (!Utils.isEmailValid(emailAddress)) {
            Utils.showToast(getString(R.string.please_enter_valid_email))
        } else if (TextUtils.isEmpty(address)) {
            Utils.showToast(getString(R.string.please_select_location))
        } else if (TextUtils.isEmpty(description)) {
            Utils.showToast(getString(R.string.please_enter_description))
        } else if (!Utils.isOnline(this)) {
            Utils.showToast(getString(R.string.please_check_internet_connection))
        } else {

            if (updatedImageFile != null || videoFile != null) {

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
//                        multipartEntity.addPart("fun_zone_id", StringBody(languageCode))
                            multipartEntity.addPart("title", StringBody(title))
                            multipartEntity.addPart("contact_person", StringBody(user.name))
                            multipartEntity.addPart("contact_no", StringBody(user.phone_number))
                            multipartEntity.addPart("email_id", StringBody(emailAddress))

                            if (updatedImageFile != null) {
                                multipartEntity.addPart("fun_zone_image", FileBody(updatedImageFile, "userFile1/jpg"))
                                multipartEntity.addPart("type", StringBody("IMAGE"))
                            } else if (videoFile != null) {
                                multipartEntity.addPart("fun_zone_image", FileBody(videoFile, "userFile1/jpg"))
                                multipartEntity.addPart("type", StringBody("VIDEO"))

                                if (videoThumbFile != null)
                                    multipartEntity.addPart("video_thumb", FileBody(videoThumbFile, "userFile1/jpg"))
                            }

                            multipartEntity.addPart("address", StringBody(address))
                            multipartEntity.addPart("description", StringBody(description))
                            multipartEntity.addPart("lat", StringBody(latitude.toString()))
                            multipartEntity.addPart("lng", StringBody(longitude.toString()))
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
                            val adoptionResponse: AdoptionResponse = Utils.getResponse(result.toString(), AdoptionResponse::class.java)
                            if (adoptionResponse.result != null) {
                                Utils.showToast(adoptionResponse.message)
                                finish()
                            }
                        }
                    }
                }.execute()

            } else {

            }

        }
    }
}
