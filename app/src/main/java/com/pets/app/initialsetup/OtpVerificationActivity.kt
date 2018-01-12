package com.pets.app.initialsetup

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.gson.GsonBuilder
import com.pets.app.R
import com.pets.app.common.AppPreferenceManager
import com.pets.app.common.ApplicationsConstants
import com.pets.app.common.Constants
import com.pets.app.mediator.AppTextWatcher
import com.pets.app.model.NormalResponse
import com.pets.app.utilities.TimeStamp
import com.pets.app.utilities.Utils
import com.pets.app.webservice.RestClient
import com.pets.app.webservice.WebServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class OtpVerificationActivity : BaseActivity(), View.OnClickListener {

    private var edtText1: EditText? = null
    private var edtText2: EditText? = null
    private var edtText3: EditText? = null
    private var edtText4: EditText? = null
    private var tvNumber: TextView? = null
    private var tvResent: TextView? = null
    private var btnSubmit: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verification)

        initializeToolbar(this.getString(R.string.otp))
        initView()
        clickListeners()
    }

    private fun initView() {

        edtText1 = findViewById(R.id.edtText1)
        edtText2 = findViewById(R.id.edtText2)
        edtText3 = findViewById(R.id.edtText3)
        edtText4 = findViewById(R.id.edtText4)
        tvNumber = findViewById(R.id.tvNumber)
        tvResent = findViewById(R.id.tvResent)
        btnSubmit = findViewById(R.id.btnSubmit)

        tvResent?.movementMethod = LinkMovementMethod.getInstance()
        tvResent?.text = generateSpannableString(this.getString(R.string.dint_receive_code), this.getString(R.string.resent))

        tvNumber?.text = this.getString(R.string.enter_verification_code_sent_on)
                .plus(Constants.SPACE)
                .plus(AppPreferenceManager.getUser().phone_code)
                .plus(AppPreferenceManager.getUser().phone_number)

        edtText1!!.addTextChangedListener(object : AppTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                super.afterTextChanged(s)
                if (s?.length!! > 0) {
                    edtText2?.requestFocus()
                }
            }
        })

        edtText2!!.addTextChangedListener(object : AppTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                super.afterTextChanged(s)
                if (s?.length!! > 0) {
                    edtText3?.requestFocus()
                }
            }
        })

        edtText3!!.addTextChangedListener(object : AppTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                super.afterTextChanged(s)
                if (s?.length!! > 0) {
                    edtText4?.requestFocus()
                }
            }
        })

        edtText4!!.addTextChangedListener(object : AppTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                super.afterTextChanged(s)
                if (s?.length!! > 0) {
                    Utils.hideKeyboard(this@OtpVerificationActivity)
                }
            }
        })
    }

    private fun clickListeners() {

        btnSubmit?.setOnClickListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val mIntent = Intent()
                mIntent.putExtra(ApplicationsConstants.NORMAL, true)
                setResult(RESULT_OK, mIntent)
                this.finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.btnSubmit -> {
                if (checkValidations()) {
                    if (Utils.isOnline(this)) {
                        verifyOTPApiCall()
                    } else {
                        Utils.showToast(this.getString(R.string.device_is_offline))
                    }
                }
            }
        }
    }

    private fun checkValidations(): Boolean {

        if (edtText1?.text.toString().trim().isEmpty()) {
            Utils.showToast(this.getString(R.string.enter_all_fields))
            return false
        } else if (edtText2?.text.toString().trim().isEmpty()) {
            Utils.showToast(this.getString(R.string.enter_all_fields))
            return false
        } else if (edtText3?.text.toString().trim().isEmpty()) {
            Utils.showToast(this.getString(R.string.enter_all_fields))
            return false
        } else if (edtText4?.text.toString().trim().isEmpty()) {
            Utils.showToast(this.getString(R.string.enter_all_fields))
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
                if (Utils.isOnline(this@OtpVerificationActivity)) {
                    resentOTPApiCall()
                } else {
                    Utils.showToast(this@OtpVerificationActivity.getString(R.string.device_is_offline))
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

    private fun verifyOTPApiCall() {

        val text1 = edtText1?.text.toString().trim()
        val text2 = edtText2?.text.toString().trim()
        val text3 = edtText3?.text.toString().trim()
        val text4 = edtText4?.text.toString().trim()
        val otp = text1 + text2 + text3 + text4
        val timeStamp = TimeStamp.getTimeStamp()
        val key = TimeStamp.getMd5(timeStamp + AppPreferenceManager.getUserID() + otp + Constants.TIME_STAMP_KEY)

        showProgressBar()
        val api = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
        val call = api.verifyOTP(AppPreferenceManager.getUserID(), otp, timeStamp, key)
        call.enqueue(object : Callback<NormalResponse> {
            override fun onResponse(call: Call<NormalResponse>?, response: Response<NormalResponse>?) {
                hideProgressBar()
                if (response != null) {
                    if (response.body() != null && response.isSuccessful) {
                        checkResponse(response.body(), true)
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

            override fun onFailure(call: Call<NormalResponse>?, t: Throwable?) {
                hideProgressBar()
            }
        })
    }

    private fun resentOTPApiCall() {

        val timeStamp = TimeStamp.getTimeStamp()
        val key = TimeStamp.getMd5(timeStamp + AppPreferenceManager.getUserID() + Constants.TIME_STAMP_KEY)

        showProgressBar()
        val api = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
        val call = api.resendOTP(AppPreferenceManager.getUserID(), timeStamp, key)
        call.enqueue(object : Callback<NormalResponse> {
            override fun onResponse(call: Call<NormalResponse>?, response: Response<NormalResponse>?) {
                hideProgressBar()
                if (response != null) {
                    if (response.body() != null && response.isSuccessful) {
                        checkResponse(response.body(), false)
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

            override fun onFailure(call: Call<NormalResponse>?, t: Throwable?) {
                hideProgressBar()
            }
        })
    }

    private fun checkResponse(response: NormalResponse?, isVerifyOTP: Boolean) {

        if (isVerifyOTP) {
            AppPreferenceManager.setSignIn(true)
            Utils.showToast(this.getString(R.string.otp_verified))
            val mIntent = Intent(this, LandingActivity::class.java)
            mIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            this.startActivity(mIntent)
        } else {
            Utils.showToast(this.getString(R.string.otp_resent))
        }
    }
}
