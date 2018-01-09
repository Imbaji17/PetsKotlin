package com.pets.app.initialsetup

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.pets.app.R

class LoginActivity : AppCompatActivity(), View.OnClickListener {

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
    }

    private fun clickListeners() {

        tvForgotPassword?.setOnClickListener(this)
        btnLogin?.setOnClickListener(this)
        tvSignUp?.setOnClickListener(this)
        imgFacebook?.setOnClickListener(this)
        imgInstagram?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.tvForgotPassword -> {

            }
            R.id.btnLogin -> {

            }
            R.id.tvSignUp -> {

            }
            R.id.imgFacebook -> {

            }
            R.id.imgInstagram -> {

            }
        }
    }
}
