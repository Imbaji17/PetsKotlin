package com.pets.app.activities

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.gson.GsonBuilder
import com.pets.app.R
import com.pets.app.common.AppPreferenceManager
import com.pets.app.common.Constants
import com.pets.app.common.Enums
import com.pets.app.initialsetup.BaseActivity
import com.pets.app.model.NormalResponse
import com.pets.app.utilities.TimeStamp
import com.pets.app.utilities.Utils
import com.pets.app.webservice.RestClient
import com.pets.app.webservice.WebServiceBuilder
import kotlinx.android.synthetic.main.activity_change_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ChangePasswordActivity : BaseActivity(), View.OnClickListener {

    private var edtOldPassword: EditText? = null
    private var edtPassword: EditText? = null
    private var edtConfirmPassword: EditText? = null
    private var btnUpdate: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        initializeToolbar(this.getString(R.string.change_password))
        initView()
        clickListeners()
    }

    private fun initView() {

        edtOldPassword = findViewById(R.id.edtOldPassword)
        edtPassword = findViewById(R.id.edtNewPassword)
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword)
        btnUpdate = findViewById(R.id.btnUpdate)
    }

    private fun clickListeners() {

        btnUpdate?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.btnUpdate -> {
                if (checkValidations()) {
                    if (Utils.isOnline(this)) {
                        changePasswordApiCall()
                    } else {
                        Utils.showToast(this.getString(R.string.device_is_offline))
                    }
                }
            }
        }
    }

    private fun checkValidations(): Boolean {

        if (TextUtils.isEmpty(edtOldPassword?.text.toString().trim())) {
            edtOldPassword?.error = this.getString(R.string.enter_old_password)
            edtOldPassword?.requestFocus()
            return false
        } else if (edtOldPassword?.text.toString().trim().length < 6) {
            edtOldPassword?.error = this.getString(R.string.password_must_be_greater_than_6)
            edtOldPassword?.requestFocus()
            return false
        } else if (TextUtils.isEmpty(edtPassword?.text.toString().trim())) {
            edtPassword?.error = this.getString(R.string.enter_new_password)
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
            edtConfirmPassword?.error = this.getString(R.string.please_enter_confirm_password_match_new)
            edtConfirmPassword?.requestFocus()
            return false
        }

        return true
    }

    private fun changePasswordApiCall() {

        val oldPassword = edtOldPassword?.text.toString().trim()
        val newPassword = edtNewPassword?.text.toString().trim()
        val language = Enums.Language.EN.name.toUpperCase()
        val timeStamp = TimeStamp.getTimeStamp()
        val key = TimeStamp.getMd5(timeStamp + oldPassword + newPassword + Constants.TIME_STAMP_KEY)

        showProgressBar()
        val api = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
        val call = api.changePassword(AppPreferenceManager.getUserID(), oldPassword, newPassword, language, timeStamp, key)
        call.enqueue(object : Callback<NormalResponse> {
            override fun onResponse(call: Call<NormalResponse>?, response: Response<NormalResponse>?) {
                hideProgressBar()
                if (response != null) {
                    if (response.body() != null && response.isSuccessful) {
                        Utils.showToast(response.body().message)
                        this@ChangePasswordActivity.finish()
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
}
