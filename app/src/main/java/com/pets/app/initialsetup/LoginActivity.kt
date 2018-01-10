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
import android.widget.*
import com.pets.app.R
import com.pets.app.common.Constants

class LoginActivity : BaseActivity(), View.OnClickListener {

    private var edtEmail: EditText? = null
    private var edtPassword: EditText? = null
    private var checkRemember: CheckBox? = null
    private var tvForgotPassword: TextView? = null
    private var btnLogin: Button? = null
    private var tvSignUp: TextView? = null
    private var imgFacebook: ImageView? = null
    private var imgInstagram: ImageView? = null

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
                val signUp = Intent(this, SignUpActivity::class.java)
                startActivity(signUp)
            }
            R.id.imgFacebook -> {
                val signUp = Intent(this, SignUpActivity::class.java)
                startActivity(signUp)
            }
            R.id.imgInstagram -> {
                val signUp = Intent(this, SignUpActivity::class.java)
                startActivity(signUp)
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
}
