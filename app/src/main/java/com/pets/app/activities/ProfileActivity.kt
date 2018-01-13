package com.pets.app.activities

import android.content.Intent
import android.os.Bundle
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
import com.pets.app.common.ImageSetter
import com.pets.app.initialsetup.BaseActivity
import com.pets.app.utilities.Logger
import com.pets.app.utilities.Utils

class ProfileActivity : BaseActivity(), View.OnClickListener {

    private var imgProfile: ImageView? = null
    private var tvChangePhoto: TextView? = null
    private var edtName: EditText? = null
    private var edtEmail: EditText? = null
    private var edtCountryCode: EditText? = null
    private var edtContact: EditText? = null
    private var tvChangeNumber: TextView? = null
    private var edtLocation: EditText? = null
    private var edtDescription: EditText? = null
    private var tvChangePassword: TextView? = null
    private var btnUpdate: Button? = null
    private lateinit var latitude: String
    private lateinit var longitude: String
    private val RC_AUTOCOMPLETE: Int = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initializeToolbar(this.getString(R.string.profile))
        initView()
        clickListeners()
        setUserDataToView()
    }

    private fun initView() {

        imgProfile = findViewById(R.id.imgProfile)
        tvChangePhoto = findViewById(R.id.tvChangePhoto)
        edtName = findViewById(R.id.edtName)
        edtEmail = findViewById(R.id.edtEmail)
        edtCountryCode = findViewById(R.id.edtCountryCode)
        edtContact = findViewById(R.id.edtContact)
        tvChangeNumber = findViewById(R.id.tvChangeNumber)
        edtLocation = findViewById(R.id.edtLocation)
        edtDescription = findViewById(R.id.edtDescription)
        tvChangePassword = findViewById(R.id.tvChangePassword)
        btnUpdate = findViewById(R.id.btnUpdate)

        tvChangePhoto?.text = Utils.getUnderlineString(this.getString(R.string.change_photo))
        tvChangeNumber?.text = Utils.getUnderlineString(this.getString(R.string.change_number))
        tvChangePassword?.text = Utils.getUnderlineString(this.getString(R.string.change_password))
    }

    private fun setUserDataToView() {

        try {

            val user = AppPreferenceManager.getUser()

            if (!TextUtils.isEmpty(user.profile_image)) {
                ImageSetter.loadRoundedImage(this, user.profile_image, R.drawable.profile, imgProfile)
            }
            if (!TextUtils.isEmpty(user.name)) {
                edtName?.setText(user.name)
            }
            if (!TextUtils.isEmpty(user.email_id)) {
                edtEmail?.setText(user.email_id)
            }
            if (!TextUtils.isEmpty(user.phone_code)) {
                edtCountryCode?.setText(user.phone_code)
            }
            if (!TextUtils.isEmpty(user.phone_number)) {
                edtContact?.setText(user.phone_number)
            }
            if (!TextUtils.isEmpty(user.location)) {
                edtLocation?.setText(user.location)
                latitude = user.lat
                longitude = user.lng
            }
            if (!TextUtils.isEmpty(user.description)) {
                edtDescription?.setText(user.description)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun clickListeners() {

        tvChangePhoto?.setOnClickListener(this)
        tvChangeNumber?.setOnClickListener(this)
        edtLocation?.setOnClickListener(this)
        tvChangePassword?.setOnClickListener(this)
        btnUpdate?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.tvChangePhoto -> {

            }
            R.id.tvChangeNumber -> {

            }
            R.id.edtLocation -> {
                openAutocompleteActivity()
            }
            R.id.tvChangePassword -> {

            }
            R.id.btnUpdate -> {

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
            RC_AUTOCOMPLETE -> {
                if (resultCode == RESULT_OK) {
                    // Get the user's selected place from the Intent.
                    val place = PlaceAutocomplete.getPlace(this, data)
                    Log.i("TAG", "Place Selected: " + place.name)

                    val latLng = place.latLng
                    latitude = latLng.latitude.toString()
                    longitude = latLng.longitude.toString()
                    edtLocation?.setText(place.address)

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
