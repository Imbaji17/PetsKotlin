package com.pets.app.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.pets.app.R
import com.pets.app.common.AppPreferenceManager
import com.pets.app.common.Constants
import com.pets.app.common.Enums
import com.pets.app.initialsetup.BaseActivity
import com.pets.app.model.NormalResponse
import com.pets.app.model.request.SentFeedback
import com.pets.app.utilities.TimeStamp
import com.pets.app.utilities.Utils
import com.pets.app.webservice.RestClient
import com.pets.app.webservice.WebServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SendFeedbackActivity : BaseActivity(), View.OnClickListener {

    private var etFeedback: EditText? = null
    private var btnSubmit: Button? = null

    companion object {
        fun startActivity(activity: Activity) {
            val intent = Intent(activity, SendFeedbackActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_feedback)
        initializeToolbar(getString(R.string.sent_feedback))
        initViews()
    }

    private fun initViews() {
        etFeedback = findViewById(R.id.etFeedback)
        btnSubmit = findViewById(R.id.btnSubmit)

        btnSubmit!!.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.btnSubmit -> {
                feedBack()
            }
        }
    }

    private fun feedBack() {
        var feedBack = etFeedback!!.text.toString()
        if (TextUtils.isEmpty(feedBack)) {
            Utils.showToast(getString(R.string.enter_product_description))
        } else if (!Utils.isOnline(this)) {
            Utils.showToast(getString(R.string.please_check_internet_connection))
        } else {
            showProgressBar()
            val timeStamp = TimeStamp.getTimeStamp()
            val userId = AppPreferenceManager.getUserID()
            val key = TimeStamp.getMd5(timeStamp + userId + Constants.TIME_STAMP_KEY)
            val request = SentFeedback()

            request.setUserId(userId)
            request.setTimestamp(timeStamp)
            request.setKey(key)
            request.setMessage(Enums.Favourite.ADOPTION.name)
            val api = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
            val call = api.sentFeedback(request)
            call.enqueue(object : Callback<NormalResponse> {
                override fun onResponse(call: Call<NormalResponse>?, response: Response<NormalResponse>?) {
                    hideProgressBar()
                    if (response != null) {
                        Utils.showToast(response.message())
                        finish()
                    }
                }

                override fun onFailure(call: Call<NormalResponse>?, t: Throwable?) {
                    hideProgressBar()
                }
            })
        }
    }
}
