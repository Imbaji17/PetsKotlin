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
import android.view.View
import android.widget.*
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.gson.GsonBuilder
import com.pets.app.R
import com.pets.app.common.AppPreferenceManager
import com.pets.app.common.ApplicationsConstants
import com.pets.app.common.Constants
import com.pets.app.common.Enums
import com.pets.app.interfaces.SimpleItemClickListener
import com.pets.app.mediator.FaceBookIntegrator
import com.pets.app.model.LoginResponse
import com.pets.app.model.`object`.LoginDetails
import com.pets.app.model.request.UpdateUserRequest
import com.pets.app.utilities.TimeStamp
import com.pets.app.utilities.Utils
import com.pets.app.webservice.RestClient
import com.pets.app.webservice.WebServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class LoginActivity : BaseActivity(), View.OnClickListener {

    private val FACEBOOK_REQUEST_CODE = 64206
    private var edtEmail: EditText? = null
    private var edtPassword: EditText? = null
    private var checkRemember: CheckBox? = null
    private var tvForgotPassword: TextView? = null
    private var btnLogin: Button? = null
    private var tvSignUp: TextView? = null
    private var imgFacebook: ImageView? = null
    private var imgInstagram: ImageView? = null
    private lateinit var loginDetails: LoginDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initView()
        clickListeners()
    }

    private fun initView() {

        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        checkRemember = findViewById(R.id.checkRememberMe)
        tvForgotPassword = findViewById(R.id.tvForgotPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvSignUp = findViewById(R.id.tvSignUp)
        imgFacebook = findViewById(R.id.imgFacebook)
        imgInstagram = findViewById(R.id.imgInstagram)

        tvSignUp?.movementMethod = LinkMovementMethod.getInstance()
        tvSignUp?.text = generateSpannableString(this.getString(R.string.dont_have_account), this.getString(R.string.title_activity_sign_up))

        if (AppPreferenceManager.isRemember()) {
            edtEmail?.setText(AppPreferenceManager.getUserEmail())
            edtPassword?.setText(AppPreferenceManager.getUserPassword())
            checkRemember?.isChecked = true
        } else {
            checkRemember?.isChecked = false
        }
    }

    private fun clickListeners() {

        tvForgotPassword?.setOnClickListener(this)
        btnLogin?.setOnClickListener(this)
        imgFacebook?.setOnClickListener(this)
        imgInstagram?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.tvForgotPassword -> {
                val signUp = Intent(this, ForgotPasswordActivity::class.java)
                startActivity(signUp)
            }
            R.id.btnLogin -> {
                if (checkValidations()) {
                    if (Utils.isOnline(this)) {
                        loginApiCall()
                    } else {
                        Utils.showToast(this.getString(R.string.device_is_offline))
                    }
                }
            }
            R.id.imgFacebook -> {
                loginFacebook()
            }
            R.id.imgInstagram -> {
                val signUp = Intent(this, SignUpActivity::class.java)
                startActivity(signUp)
            }
        }
    }

    private fun checkValidations(): Boolean {

        if (TextUtils.isEmpty(edtEmail?.text.toString().trim())) {
            edtEmail?.error = this.getString(R.string.please_enter_email)
            return false
        } else if (!Utils.isEmailValid(edtEmail?.text.toString().trim())) {
            edtEmail?.error = this.getString(R.string.please_enter_valid_email)
            return false
        } else if (TextUtils.isEmpty(edtPassword?.text.toString().trim())) {
            edtPassword?.error = this.getString(R.string.please_enter_password)
            return false
        } else if (edtPassword?.text.toString().trim().length < 6) {
            edtPassword?.error = this.getString(R.string.password_must_be_greater_than_6)
            return false
        }

        return true
    }

    private fun generateSpannableString(string: String?, string1: String?): SpannableString {

        val outString = SpannableString(string
                + Constants.SPACE
                + string1)

        outString.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.black)), 0, string!!.length, 0)
        outString.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorAccent)), string.length + 1, string.length + string1!!.length + 1, 0)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val signUp = Intent(this@LoginActivity, SignUpActivity::class.java)
                startActivity(signUp)
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
        if (requestCode == FACEBOOK_REQUEST_CODE)
            mSocialIntegratorInterface.facebookCallbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun loginFacebook() {

        showProgressBar()
        mSocialIntegratorInterface = FaceBookIntegrator(this, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                hideProgressBar()
                mSocialIntegratorInterface.getProfile(loginResult, object : SimpleItemClickListener {
                    override fun onItemClick(`object`: Any?) {
                        loginDetails = `object` as LoginDetails
                        if (loginDetails != null) {
                            if (Utils.isOnline(this@LoginActivity)) {
                                socialLoginApiCall(loginDetails.social_id, loginDetails.social_type, loginDetails.email_id)
                            } else {
                                Utils.showToast(this@LoginActivity.getString(R.string.device_is_offline))
                            }
                        }
                    }
                })
            }

            override fun onCancel() {
                hideProgressBar()
            }

            override fun onError(error: FacebookException) {
                hideProgressBar()
                Utils.showToast(error.message)
            }
        })
    }

    private fun socialLoginApiCall(socialId: String, socialType: String, email: String) {

        val languageCode = Enums.Language.EN.name
        val deviceType = Constants.DEVICE_TYPE
        val deviceToken = ""
        val timeStamp = TimeStamp.getTimeStamp()
        val key = TimeStamp.getMd5(timeStamp + socialId + socialType + Constants.TIME_STAMP_KEY)

        val request = UpdateUserRequest()
        request.setSocial_id(socialId)
        request.setSocial_type(socialType)
        request.setName(loginDetails.name)
        request.setEmail_id(email)
        request.setLanguage_code(languageCode)
        request.setDevice_type(deviceType)
        request.setDevice_token(deviceToken)
        request.setTimestamp(timeStamp)
        request.setKey(key)

        showProgressBar()
        val api = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
        val call = api.socialLogin(request)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>?, response: Response<LoginResponse>?) {
                hideProgressBar()
                if (response != null) {
                    if (response.body() != null && response.isSuccessful) {
                        checkSocialLoginResponse(response.body().result)
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

    private fun loginApiCall() {

        val email = edtEmail?.text.toString().trim()
        val passsword = edtPassword?.text.toString().trim()
        val languageCode = Enums.Language.EN.name
        val deviceType = Constants.DEVICE_TYPE
        val deviceToken = ""
        val timeStamp = TimeStamp.getTimeStamp()
        val key = TimeStamp.getMd5(timeStamp + email + passsword + Constants.TIME_STAMP_KEY)

        showProgressBar()
        val api = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
        val call = api.login(email, passsword, languageCode, deviceType, deviceToken, timeStamp, key)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>?, response: Response<LoginResponse>?) {
                hideProgressBar()
                if (response != null) {
                    if (response.body() != null && response.isSuccessful) {
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

    private fun checkSocialLoginResponse(response: LoginDetails?) {

        AppPreferenceManager.saveUser(response)

        if (!response?.isNewUser()!!) {

            val mIntent: Intent?
            if (response.isMobileVerified) {
                mIntent = Intent(this, LandingActivity::class.java)
                mIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            } else {
                mIntent = Intent(this, OtpVerificationActivity::class.java)
            }
            this.startActivity(mIntent)
        } else {
            val mIntent = Intent(this, SignUpActivity::class.java)
            mIntent.putExtra(ApplicationsConstants.USER_OBJECT, loginDetails)
            this.startActivity(mIntent)
        }
    }

    private fun checkResponse(details: LoginDetails?) {

        AppPreferenceManager.saveUser(details)

        if (checkRemember!!.isChecked) {
            AppPreferenceManager.setRemember(true)
            AppPreferenceManager.saveUserEmail(edtEmail?.text.toString())
            AppPreferenceManager.saveUserPassword(edtPassword?.text.toString())
        }

        val mIntent = Intent(this, LandingActivity::class.java)
        mIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        this.startActivity(mIntent)
    }
}
