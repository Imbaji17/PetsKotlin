package com.pets.app.activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.pets.app.R
import com.pets.app.initialsetup.BaseActivity
import com.pets.app.utilities.Utils

class ProfileActivity : BaseActivity(), View.OnClickListener {

    private var imgProfile: ImageView? = null
    private var tvChangePhoto: TextView? = null
    private var edtName: EditText? = null
    private var edtEmail: EditText? = null
    private var edtCountryCode: EditText? = null
    private var edtContact: EditText? = null
    private var edtLocation: EditText? = null
    private var edtDescription: EditText? = null
    private var tvChangePassword: TextView? = null
    private var btnUpdate: Button? = null
    private lateinit var latitude: String
    private lateinit var longitude: String
    private val RC_AUTOCOMPLETE: Int = 100
    private val RC_COUNTRY_CODE: Int = 300

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initializeToolbar(this.getString(R.string.profile))
        initView()
        clickListeners()
    }

    private fun initView() {

        imgProfile = findViewById(R.id.imgProfile)
        tvChangePhoto = findViewById(R.id.tvChangePhoto)
        edtName = findViewById(R.id.edtName)
        edtEmail = findViewById(R.id.edtEmail)
        edtCountryCode = findViewById(R.id.edtCountryCode)
        edtContact = findViewById(R.id.edtContact)
        edtLocation = findViewById(R.id.edtLocation)
        edtDescription = findViewById(R.id.edtDescription)
        tvChangePassword = findViewById(R.id.tvChangePassword)
        btnUpdate = findViewById(R.id.btnUpdate)

        tvChangePhoto?.text = Utils.getUnderlineString(this.getString(R.string.change_photo))
        tvChangePassword?.text = Utils.getUnderlineString(this.getString(R.string.change_password))
    }

    private fun clickListeners() {

        tvChangePhoto?.setOnClickListener(this)
        edtLocation?.setOnClickListener(this)
        btnUpdate?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {


    }
}
