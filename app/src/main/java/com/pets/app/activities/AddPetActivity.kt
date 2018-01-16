package com.pets.app.activities

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import com.pets.app.R
import com.pets.app.initialsetup.BaseActivity

class AddPetActivity : BaseActivity(), View.OnClickListener {

    private var imgPet: ImageView? = null
    private var edtName: EditText? = null
    private var edtType: EditText? = null
    private var edtBreed: EditText? = null
    private var edtDOB: EditText? = null
    private var radioGender: RadioGroup? = null
    private var btnUpload: Button? = null
    private var edtDesc: EditText? = null
    private var mRecyclerView: RecyclerView? = null
    private var btnAddPet: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pet)

        initializeToolbar(this.getString(R.string.add_pet))
        initView()
        clickListeners()
    }

    private fun initView() {

        imgPet = findViewById(R.id.imgView)
        edtName = findViewById(R.id.edtName)
        edtType = findViewById(R.id.edtType)
        edtBreed = findViewById(R.id.edtBreed)
        edtDOB = findViewById(R.id.edtDOB)
        radioGender = findViewById(R.id.radioGender)
        btnUpload = findViewById(R.id.btnUpload)
        edtDesc = findViewById(R.id.edtDescription)
        mRecyclerView = findViewById(R.id.recyclerView)
        btnAddPet = findViewById(R.id.btnAddPet)
    }

    private fun clickListeners() {

        imgPet?.setOnClickListener(this)
        edtType?.setOnClickListener(this)
        edtBreed?.setOnClickListener(this)
        edtDOB?.setOnClickListener(this)
        btnUpload?.setOnClickListener(this)
        btnAddPet?.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {


    }
}
