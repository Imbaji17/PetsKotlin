package com.pets.app.initialsetup

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.pets.app.R

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
                this.finish()
            }
        }
    }

}
