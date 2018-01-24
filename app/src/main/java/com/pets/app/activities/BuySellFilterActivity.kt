package com.pets.app.activities

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.pets.app.R
import com.pets.app.common.ApplicationsConstants
import com.pets.app.common.ImageSetter
import com.pets.app.initialsetup.BaseActivity
import com.pets.app.model.Breed
import com.pets.app.model.PetsType
import com.pets.app.model.`object`.PhotosInfo
import com.pets.app.utilities.ImagePicker
import com.pets.app.utilities.Logger
import java.io.File

class BuySellFilterActivity : BaseActivity(), View.OnClickListener {

    private var rlPetType: RelativeLayout? = null
    private var tvPetType: TextView? = null
    private var rlCategory: RelativeLayout? = null
    private var tvCategory: TextView? = null
    private var rlSortPrice: RelativeLayout? = null
    private var tvSortPrice: TextView? = null

    private var petsTypeId: String? = "";
    private var petsTypeStr: String? = ""
    private var categoryId: String? = "";
    private var categoryStr: String? = ""
    private var sortPrice: String? = ""
    private val RC_TYPE: Int = 102

    companion object {
        private val TAG = BuySellFilterActivity::class.java.simpleName
        fun startActivity(activity: Activity, requestCode: Int, petsTypeId: String, petsTypeStr: String,
                          categoryId: String, categoryStr: String, sortPrice: String) {
            val intent = Intent(activity, BuySellFilterActivity::class.java)
            intent.putExtra(ApplicationsConstants.PETS_TYPE_ID, petsTypeId)
            intent.putExtra(ApplicationsConstants.PETS_TYPE_NAME, petsTypeStr)
            intent.putExtra(ApplicationsConstants.CATEGORY_ID, categoryId)
            intent.putExtra(ApplicationsConstants.CATEGORY, categoryStr)
            intent.putExtra(ApplicationsConstants.PRICE, sortPrice)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_sell_filter)
        initializeToolbar(getString(R.string.buy_and_sell))
        init()
        initView()
        setValues()
    }

    private fun init() {
        petsTypeId = intent.getStringExtra(ApplicationsConstants.PETS_TYPE_ID)
        petsTypeStr = intent.getStringExtra(ApplicationsConstants.PETS_TYPE_NAME)
        categoryId = intent.getStringExtra(ApplicationsConstants.CATEGORY_ID)
        categoryStr = intent.getStringExtra(ApplicationsConstants.CATEGORY)
        sortPrice = intent.getStringExtra(ApplicationsConstants.PRICE)
    }

    private fun initView() {
        rlPetType = findViewById(R.id.rlPetType)
        tvPetType = findViewById(R.id.tvPetType)
        rlCategory = findViewById(R.id.rlCategory)
        tvCategory = findViewById(R.id.tvCategory)
        rlSortPrice = findViewById(R.id.rlSortPrice)
        tvSortPrice = findViewById(R.id.tvSortPrice)

        rlPetType!!.setOnClickListener(this)
        rlCategory!!.setOnClickListener(this)
        rlSortPrice!!.setOnClickListener(this)
    }

    private fun setValues() {
        if (!TextUtils.isEmpty(petsTypeStr))
            tvPetType!!.text = petsTypeStr

        if (!TextUtils.isEmpty(categoryStr))
            tvCategory!!.text = categoryStr

        if (!TextUtils.isEmpty(sortPrice))
            tvSortPrice!!.text = sortPrice
    }


    override fun onClick(p0: View?) {

        when (p0!!.id) {
            R.id.rlPetType -> {
                val mIntent = Intent(this, SelectTypeActivity::class.java)
                mIntent.putExtra(ApplicationsConstants.NAVIGATION_TYPE, true)
                this.startActivityForResult(mIntent, RC_TYPE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {

            RC_TYPE -> {
                if (data != null) {
                    val petsType = data.getSerializableExtra(ApplicationsConstants.DATA) as PetsType
                    if (petsType != null) {
                        petsTypeId = petsType.petsTypeId
                        tvPetType!!.text = petsType.typeName
                    }
                }
            }
//            RC_BREED -> {
//                if (data != null) {
//                    val breed = data.getSerializableExtra(ApplicationsConstants.DATA) as Breed
//                    if (breed != null) {
//                        breedId = breed.breed_id
//                        tvBreed!!.text = breed.breed_name
//                    }
//                }
//            }
        }
    }

}