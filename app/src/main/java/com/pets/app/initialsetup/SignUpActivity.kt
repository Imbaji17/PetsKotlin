package com.pets.app.initialsetup

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.gson.GsonBuilder
import com.pets.app.R
import com.pets.app.activities.WebViewActivity
import com.pets.app.common.AppPreferenceManager
import com.pets.app.common.ApplicationsConstants
import com.pets.app.common.Constants
import com.pets.app.common.Enums
import com.pets.app.model.LoginResponse
import com.pets.app.model.`object`.LoginDetails
import com.pets.app.model.request.UpdateUserRequest
import com.pets.app.utilities.Logger
import com.pets.app.utilities.TimeStamp
import com.pets.app.utilities.Utils
import com.pets.app.webservice.RestClient
import com.pets.app.webservice.WebServiceBuilder
import kotlinx.android.synthetic.main.activity_sign_up.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class SignUpActivity : BaseActivity(), View.OnClickListener {

    private var edtName: EditText? = null
    private var edtEmail: EditText? = null
    private var edtPassword: EditText? = null
    private var edtConfirmPassword: EditText? = null
    private var edtCountryCode: EditText? = null
    private var edtContact: EditText? = null
    private var edtLocation: EditText? = null
    private var checkTerms: CheckBox? = null
    private var btnRegister: Button? = null
    private var tvLogin: TextView? = null
    private lateinit var latitude: String
    private lateinit var longitude: String
    private var isBack: Boolean = false
    private val RC_AUTOCOMPLETE: Int = 100
    private val RC_OTP: Int = 200
    private val RC_COUNTRY_CODE: Int = 300
    private var userObj: LoginDetails? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        initializeToolbar(this.getString(R.string.title_activity_sign_up))
        initView()
        clickListeners()
        getIntentData()
    }

    private fun initView() {

        edtName = findViewById(R.id.edtName)
        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword)
        edtCountryCode = findViewById(R.id.edtCountryCode)
        edtContact = findViewById(R.id.edtContact)
        edtLocation = findViewById(R.id.edtLocation)
        checkTerms = findViewById(R.id.checkTerms)
        btnRegister = findViewById(R.id.btnRegister)
        tvLogin = findViewById(R.id.tvLogin)

        checkTerms?.movementMethod = LinkMovementMethod.getInstance()
        checkTerms?.text = generateSpannableString(this.getString(R.string.i_accept), this.getString(R.string.terms_and_conditions), false)

        tvLogin?.movementMethod = LinkMovementMethod.getInstance()
        tvLogin?.text = generateSpannableString(this.getString(R.string.already_account), this.getString(R.string.login), true)
    }

    private fun clickListeners() {

        edtCountryCode?.setOnClickListener(this)
        edtLocation?.setOnClickListener(this)
        btnRegister?.setOnClickListener(this)
    }

    private fun getIntentData() {

        if (intent.hasExtra(ApplicationsConstants.USER_OBJECT)) {

            userObj = intent.getSerializableExtra(ApplicationsConstants.USER_OBJECT) as LoginDetails?

            password.visibility = View.GONE
            confirmPassword.visibility = View.GONE

            if (!TextUtils.isEmpty(userObj?.name)) {
                edtName?.setText(userObj?.name)
            }
            if (!TextUtils.isEmpty(userObj?.email_id)) {
                edtEmail?.setText(userObj?.email_id)
            }
        }
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.edtCountryCode -> {
                val countryCode = Intent(this, SelectCountryCodeActivity::class.java)
                this.startActivityForResult(countryCode, RC_COUNTRY_CODE)
            }
            R.id.edtLocation -> {
                openAutocompleteActivity()
            }
            R.id.btnRegister -> {
                if (checkValidations()) {
                    if (Utils.isOnline(this)) {
                        if (isBack or (userObj != null)) {
                            updateUserApiCall()
                        } else {
                            signUpApiCall()
                        }
                    } else {
                        Utils.showToast(this.getString(R.string.device_is_offline))
                    }
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

    private fun generateSpannableString(string: String?, string1: String?, isTrue: Boolean): SpannableString {

        val outString = SpannableString(string
                + Constants.SPACE
                + string1)

        outString.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.black)), 0, string!!.length, 0)
        outString.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorAccent)), string.length + 1, string.length + string1!!.length + 1, 0)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                if (isTrue) {
                    this@SignUpActivity.finish()
                } else {
                    val web = Intent(this@SignUpActivity, WebViewActivity::class.java)
                    web.putExtra(ApplicationsConstants.NAVIGATION_TYPE, Constants.TERMS_AND_CONDITIONS_URL)
                    this@SignUpActivity.startActivity(web)
                }
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }
        }
        outString.setSpan(clickableSpan, string.length + 1, string.length + string1.length + 1, 0)

        return outString
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
            RC_OTP -> {
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        isBack = true
                    }
                }
            }
            RC_COUNTRY_CODE -> {
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        val countryCode: String = data.getStringExtra(ApplicationsConstants.DATA)
                        edtCountryCode?.setText("+".plus(countryCode))
                    }
                }
            }
        }
    }

    private fun checkValidations(): Boolean {

        if (TextUtils.isEmpty(edtName?.text.toString().trim())) {
            edtName?.error = this.getString(R.string.please_enter_name)
            edtName?.requestFocus()
            return false
        } else if (TextUtils.isEmpty(edtEmail?.text.toString().trim())) {
            edtEmail?.error = this.getString(R.string.please_enter_email)
            edtEmail?.requestFocus()
            return false
        } else if (!Utils.isEmailValid(edtEmail?.text.toString().trim())) {
            edtEmail?.error = this.getString(R.string.please_enter_valid_email)
            edtEmail?.requestFocus()
            return false
        }

        if (userObj == null) {
            if (TextUtils.isEmpty(edtPassword?.text.toString().trim())) {
                edtPassword?.error = this.getString(R.string.please_enter_password)
                edtPassword?.requestFocus()
                return false
            } else if (edtPassword?.text.toString().trim().length < 6) {
                edtPassword?.error = this.getString(R.string.password_must_be_greater_than_6)
                edtPassword?.requestFocus()
                return false
            } else if (TextUtils.isEmpty(edtConfirmPassword?.text.toString().trim())) {
                edtConfirmPassword?.error = this.getString(R.string.please_enter_confirm_password)
                edtConfirmPassword?.requestFocus()
                return false
            } else if (!edtConfirmPassword?.text.toString().trim().equals(edtPassword?.text.toString().trim(), ignoreCase = true)) {
                edtConfirmPassword?.error = this.getString(R.string.please_enter_confirm_password_match)
                edtConfirmPassword?.requestFocus()
                return false
            }
        }

        if (TextUtils.isEmpty(edtContact?.text.toString().trim())) {
            edtContact?.error = this.getString(R.string.please_enter_contact)
            edtContact?.requestFocus()
            return false
        } else if (TextUtils.isEmpty(edtLocation?.text.toString().trim())) {
            Utils.showToast(this.getString(R.string.please_select_location))
            return false
        } else if (!checkTerms?.isChecked!!) {
            Utils.showToast(this.getString(R.string.please_accept_terms))
            return false
        }

        return true
    }

    private fun signUpApiCall() {

        val name = edtName?.text.toString().trim()
        val email = edtEmail?.text.toString().trim()
        val passsword = edtPassword?.text.toString().trim()
        val phoneCode = edtCountryCode?.text.toString().trim()
        val contact = edtContact?.text.toString().trim()
        val location = edtLocation?.text.toString().trim()
        val languageCode = Enums.Language.EN.name
        val deviceType = Constants.DEVICE_TYPE
        val deviceToken = ""
        val timeStamp = TimeStamp.getTimeStamp()
        val key = TimeStamp.getMd5(timeStamp + passsword + email + Constants.TIME_STAMP_KEY)

        showProgressBar()
        val api = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
        val call = api.signUp(name, email, passsword, phoneCode, contact, location, latitude, longitude, languageCode, deviceType, deviceToken, timeStamp, key)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>?, response: Response<LoginResponse>?) {
                hideProgressBar()
                if (response != null) {
                    if (response.body() != null && response.isSuccessful) {
                        Utils.showToast(response.body().message)
                        checkResponse(response.body().result)
                    } else if (response.code() == 403) {
                        val gson = GsonBuilder().create()
                        val mError: LoginResponse
                        try {
                            mError = gson.fromJson(response.errorBody().string(), LoginResponse::class.java)
                            Utils.showToast("" + mError.message)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                hideProgressBar()
            }
        })
    }

    private fun updateUserApiCall() {

        val name = edtName?.text.toString().trim()
        val email = edtEmail?.text.toString().trim()
        val phoneCode = edtCountryCode?.text.toString().trim()
        val contact = edtContact?.text.toString().trim()
        val location = edtLocation?.text.toString().trim()
        val languageCode = Enums.Language.EN.name
        val timeStamp = TimeStamp.getTimeStamp()
        val key = TimeStamp.getMd5(timeStamp + AppPreferenceManager.getUserID() + email + Constants.TIME_STAMP_KEY)

        val request = UpdateUserRequest()
        if (userObj != null) {
            request.setSocial_id(userObj?.social_id)
            request.setSocial_type(userObj?.social_type)
        }
        request.setUser_id(AppPreferenceManager.getUserID())
        request.setName(name)
        request.setEmail_id(email)
        request.setPhone_code(phoneCode)
        request.setPhone_number(contact)
        request.setLocation(location)
        request.setLat(latitude)
        request.setLng(longitude)
        request.setLanguage_code(languageCode)
        request.setTimestamp(timeStamp)
        request.setKey(key)

        showProgressBar()
        val api = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
        val call = api.updateUser(request)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>?, response: Response<LoginResponse>?) {
                hideProgressBar()
                if (response != null) {
                    if (response.body() != null && response.isSuccessful) {
                        Utils.showToast(response.body().message)
                        checkResponse(response.body().result)
                    } else if (response.code() == 403) {
                        val gson = GsonBuilder().create()
                        val mError: LoginResponse
                        try {
                            mError = gson.fromJson(response.errorBody().string(), LoginResponse::class.java)
                            Utils.showToast("" + mError.message)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                hideProgressBar()
            }
        })
    }

    private fun checkResponse(details: LoginDetails?) {

        AppPreferenceManager.saveUser(details)

        val signUp = Intent(this, OtpVerificationActivity::class.java)
        this.startActivityForResult(signUp, RC_OTP)
    }
}
