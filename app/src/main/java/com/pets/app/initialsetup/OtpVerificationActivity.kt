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
import com.pets.app.R
import com.pets.app.common.ApplicationsConstants
import com.pets.app.common.Constants
import com.pets.app.mediator.AppTextWatcher
import com.pets.app.utilities.Utils

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
            }
        }
    }

    private fun generateSpannableString(string: String?, string1: String?): SpannableString {

        val outString = SpannableString(string
                + Constants.SPACE
                + string1)

        outString.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.black)), 0, string!!.length, 0)
        outString.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorAccent)), string.length + 1, string.length + string1!!.length + 1, 0)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }
        }
        outString.setSpan(clickableSpan, string.length + 1, string.length + string1.length + 1, 0)

        return outString
    }
}
