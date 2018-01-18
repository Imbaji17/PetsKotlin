package com.pets.app.initialsetup

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.pets.app.R
import com.pets.app.common.Constants
import com.pets.app.model.NormalResponse
import com.pets.app.utilities.TimeStamp
import com.pets.app.utilities.Utils
import com.pets.app.webservice.RestClient
import com.pets.app.webservice.WebServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordActivity : BaseActivity(), View.OnClickListener {

    private var edtEmail: EditText? = null
    private var btnSubmit: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        initializeToolbar(this.getString(R.string.title_activity_forgot_password))
        initView()
        clickListeners()
    }

    private fun initView() {

        edtEmail = findViewById(R.id.edtEmail)
        btnSubmit = findViewById(R.id.btnSubmit)
    }

    private fun clickListeners() {

        btnSubmit?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.btnSubmit -> {
                if (checkValidations()) {
                    if (Utils.isOnline(this)) {
                        forgotPasswordApiCall()
                    } else {
                        Utils.showToast(this.getString(R.string.device_is_offline))
                    }
                }
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
        }

        return true
    }

    private fun forgotPasswordApiCall() {

        val email = edtEmail?.text.toString().trim()
        val timeStamp = TimeStamp.getTimeStamp()
        val key = TimeStamp.getMd5(timeStamp + email + Constants.TIME_STAMP_KEY)

        showProgressBar()
        val api = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
        val call = api.forgetPassword(email, timeStamp, key)
        call.enqueue(object : Callback<NormalResponse> {
            override fun onResponse(call: Call<NormalResponse>?, response: Response<NormalResponse>?) {
                hideProgressBar()
                if (response != null) {
                    if (response.body() != null && response.isSuccessful) {
                        Utils.showToast(response.body().message)
                        this@ForgotPasswordActivity.finish()
                    } else {
                        Utils.showErrorToast(response.errorBody())
                    }
                }
            }

            override fun onFailure(call: Call<NormalResponse>?, t: Throwable?) {
                hideProgressBar()
            }
        })
    }
}
