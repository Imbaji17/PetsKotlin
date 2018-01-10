package com.pets.app.initialsetup

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import com.pets.app.R
import com.pets.app.activities.WebViewActivity
import com.pets.app.common.Constants

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        initializeToolbar(this.getString(R.string.title_activity_sign_up))
        initView()
        clickListeners()
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

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.edtCountryCode -> {
                val signUp = Intent(this, ForgotPasswordActivity::class.java)
                startActivity(signUp)
            }
            R.id.edtLocation -> {
                val signUp = Intent(this, SignUpActivity::class.java)
                startActivity(signUp)
            }
            R.id.btnRegister -> {
                val signUp = Intent(this, OtpVerificationActivity::class.java)
                startActivity(signUp)
            }
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
                    val signUp = Intent(this@SignUpActivity, WebViewActivity::class.java)
                    startActivity(signUp)
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

}
