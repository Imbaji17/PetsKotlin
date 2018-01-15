package com.pets.app.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.gson.GsonBuilder
import com.pets.app.R
import com.pets.app.common.AppPreferenceManager
import com.pets.app.common.ApplicationsConstants
import com.pets.app.common.Constants
import com.pets.app.initialsetup.BaseActivity
import com.pets.app.initialsetup.OtpVerificationActivity
import com.pets.app.initialsetup.SelectCountryCodeActivity
import com.pets.app.model.LoginResponse
import com.pets.app.model.`object`.LoginDetails
import com.pets.app.utilities.TimeStamp
import com.pets.app.utilities.Utils
import com.pets.app.webservice.RestClient
import com.pets.app.webservice.WebServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ChangeNumberActivity : BaseActivity(), View.OnClickListener {

    private var edtCountryCode: EditText? = null
    private var edtContact: EditText? = null
    private var btnUpdate: Button? = null
    private val RC_OTP: Int = 200
    private val RC_COUNTRY_CODE: Int = 300

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_number)

        initializeToolbar(this.getString(R.string.change_number))
        initView()
        clickListeners()
    }

    private fun initView() {

        edtCountryCode = findViewById(R.id.edtCountryCode)
        edtContact = findViewById(R.id.edtContact)
        btnUpdate = findViewById(R.id.btnUpdate)
    }

    private fun clickListeners() {

        edtCountryCode?.setOnClickListener(this)
        btnUpdate?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.edtCountryCode -> {
                val countryCode = Intent(this, SelectCountryCodeActivity::class.java)
                this.startActivityForResult(countryCode, RC_COUNTRY_CODE)
            }
            R.id.btnUpdate -> {
                if (checkValidations()) {
                    if (Utils.isOnline(this)) {
                        changeNumberApiCall()
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

        if (TextUtils.isEmpty(edtContact?.text.toString().trim())) {
            edtContact?.error = this.getString(R.string.please_enter_contact)
            edtContact?.requestFocus()
            return false
        }

        return true
    }

    private fun changeNumberApiCall() {

        val phoneCode = edtCountryCode?.text.toString().trim()
        val contact = edtContact?.text.toString().trim()
        val timeStamp = TimeStamp.getTimeStamp()
        val key = TimeStamp.getMd5(timeStamp + AppPreferenceManager.getUserID() + Constants.TIME_STAMP_KEY)

        showProgressBar()
        val api = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
        val call = api.changeMobile(AppPreferenceManager.getUserID(), phoneCode, contact, timeStamp, key)
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

        val signUp = Intent(this, OtpVerificationActivity::class.java)
        signUp.putExtra(ApplicationsConstants.COUNTRY_CODE, edtCountryCode?.text.toString().trim())
        signUp.putExtra(ApplicationsConstants.MOBILE_NUMBER, edtContact?.text.toString().trim())
        this.startActivityForResult(signUp, RC_OTP)
    }
}
