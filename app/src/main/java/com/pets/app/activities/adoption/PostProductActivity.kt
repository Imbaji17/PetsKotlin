package com.pets.app.activities.adoption

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.pets.app.R
import com.pets.app.activities.SelectTypeActivity
import com.pets.app.common.*
import com.pets.app.model.Category
import com.pets.app.model.PetsType
import com.pets.app.model.Product
import com.pets.app.model.ProductResponse
import com.pets.app.utilities.ImagePicker
import com.pets.app.utilities.Logger
import com.pets.app.utilities.TimeStamp
import com.pets.app.utilities.Utils
import com.pets.app.webservice.UploadImage
import khandroid.ext.apache.http.entity.mime.HttpMultipartMode
import khandroid.ext.apache.http.entity.mime.MultipartEntity
import khandroid.ext.apache.http.entity.mime.content.FileBody
import khandroid.ext.apache.http.entity.mime.content.StringBody
import java.io.File
import java.io.IOException

class PostProductActivity : ImagePicker(), View.OnClickListener {

    private var ivPost: ImageView? = null
    private var tvAddImage: TextView? = null
    private var etProductName: EditText? = null
    private var etSelectCategory: EditText? = null
    private var etPetType: EditText? = null
    private var etPrice: EditText? = null
    private var etDescription: EditText? = null
    private var btnPost: Button? = null
    private val RC_TAKE_VIDEO = 100
    private val RC_PET_TYPE: Int = 102
    private val RC_CATEGORY: Int = 103

    private var from: Int = 0
    private var type: String = ""
    private var categoryId: String = ""
    private var petTypeId: String = ""


    companion object {
        private val TAG = PostProductActivity::class.java.simpleName
        fun startActivity(activity: Activity, requestCode: Int, type: String) {
            val intent = Intent(activity, PostProductActivity::class.java)
            intent.putExtra(ApplicationsConstants.TYPE, type)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_product)
        initializeToolbar(getString(R.string.post_your_product))
        init()
        initView()
    }

    private fun init() {
        type = intent.getStringExtra(ApplicationsConstants.TYPE)
    }

    private fun initView() {
        ivPost = findViewById(R.id.ivPost)
        tvAddImage = findViewById(R.id.tvAddImage)
        etProductName = findViewById(R.id.etProductName)
        etSelectCategory = findViewById(R.id.etSelectCategory)
        etPetType = findViewById(R.id.etPetType)
        etPrice = findViewById(R.id.etPrice)
        etDescription = findViewById(R.id.etDescription)
        btnPost = findViewById(R.id.btnPost)
        ivPost!!.setOnClickListener(this)
        etSelectCategory!!.setOnClickListener(this)
        btnPost!!.setOnClickListener(this)
        etPetType!!.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {

        when (p0!!.id) {
            R.id.ivPost -> {
                showTakeImagePopup()
            }
            R.id.etSelectCategory -> {
                SelectTypeActivity.startActivity(this, RC_CATEGORY, categoryId!!, 2, "")
            }
            R.id.etPetType -> {
                SelectTypeActivity.startActivity(this, RC_PET_TYPE, petTypeId!!, 0, "")
            }
            R.id.btnPost -> {
                addProduct()
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {

            RC_CROP_ACTIVITY -> {
                if (data != null) {
                    val result = com.theartofdev.edmodo.cropper.CropImage.getActivityResult(data)
                    val mCurrentPhotoPath = result.uri.path
                    updatedImageFile = File(mCurrentPhotoPath)
                    if (updatedImageFile.exists()) {
                        ImageSetter.loadImage(this, updatedImageFile, R.drawable.profile, ivPost)
                    }
                }
            }

            RC_CATEGORY -> {
                if (data != null) {
                    val category = data.getSerializableExtra(ApplicationsConstants.DATA) as Category
                    if (category != null) {
                        categoryId = category.productCategoryId
                        etSelectCategory!!.setText(category.productCategoryName)
                    }
                }
            }

            RC_PET_TYPE -> {
                if (data != null) {
                    val petsType = data.getSerializableExtra(ApplicationsConstants.DATA) as PetsType
                    if (petsType != null) {
                        petTypeId = petsType.petsTypeId
                        etPetType!!.setText(petsType.typeName)
                    }
                }
            }

        }
    }


    private fun addProduct() {

        var actionName = "add_edit_product"
        var userId = AppPreferenceManager.getUserID()
        var timestamp = TimeStamp.getTimeStamp()
        var languageCode = Enums.Language.EN.name
        var key = TimeStamp.getMd5(timestamp + userId + Constants.TIME_STAMP_KEY)

        var productName = etProductName!!.text.toString()
        var categoryName = etSelectCategory!!.text.toString()
        var petType = etPetType!!.text.toString()
        var price = etPrice!!.text.toString()
        var description = etDescription!!.text.toString()


        if (updatedImageFile == null) {
            Utils.showToast(getString(R.string.please_add_product_image))
        } else if (TextUtils.isEmpty(productName)) {
            Utils.showToast(getString(R.string.enter_product_name))
        } else if (TextUtils.isEmpty(categoryName)) {
            Utils.showToast(getString(R.string.select_product_category))
        } else if (TextUtils.isEmpty(petType)) {
            Utils.showToast(getString(R.string.please_select_pet_type))
        } else if (TextUtils.isEmpty(price)) {
            Utils.showToast(getString(R.string.enter_product_price))
        } else if (TextUtils.isEmpty(description)) {
            Utils.showToast(getString(R.string.enter_product_description))
        } else if (!Utils.isOnline(this)) {
            Utils.showToast(getString(R.string.please_check_internet_connection))
        } else {

            object : AsyncTask<Void, Void, String>() {
                private var response: String? = null
                override fun onPreExecute() {
                    super.onPreExecute()
                    showProgressBar()
                }

                override fun doInBackground(vararg params: Void): String? {
                    try {
                        val multipartEntity = MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE)
                        multipartEntity.addPart("user_id", StringBody(userId!!))
                        multipartEntity.addPart("key", StringBody(key!!))
                        multipartEntity.addPart("timestamp", StringBody(timestamp!!))
                        multipartEntity.addPart("language_code", StringBody(languageCode))
//                            if (from == 1) {
//                                multipartEntity.addPart("product_id", StringBody(funZone!!.funZoneId))
//                            }
                        multipartEntity.addPart("product_name", StringBody(productName))
                        multipartEntity.addPart("price", StringBody(price))
                        multipartEntity.addPart("description", StringBody(description))
                        multipartEntity.addPart("product_type", StringBody(type))
                        multipartEntity.addPart("product_image", FileBody(updatedImageFile, "userFile1/jpg"))
                        multipartEntity.addPart("product_category_id", StringBody(categoryId))
                        multipartEntity.addPart("pets_type_id", StringBody(petTypeId))

                        response = UploadImage.uploadImage(Constants.API_BASE_URL + actionName, multipartEntity)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    return response
                }

                override fun onPostExecute(result: String?) {
                    hideProgressBar()
                    Logger.errorLog("Response ### " + result!!)
                    print("Response #### " + result)
                    if (result != null) {
                        val productResponse: ProductResponse = Utils.getResponse(result.toString(), ProductResponse::class.java)
                        if (productResponse.result != null) {
                            Utils.showToast(productResponse.message)
                            setResultOk(productResponse.result)
                        }
                    }
                }
            }.execute()
        }
    }

    fun setResultOk(product: Product?) {
//        val selectedAddIntent = Intent()
//        selectedAddIntent.putExtra(ApplicationsConstants.FROM, from)
//        selectedAddIntent.putExtra(ApplicationsConstants.DATA, funZone)
//        selectedAddIntent.putExtra(ApplicationsConstants.POSITION, position)
//        setResult(Activity.RESULT_OK, selectedAddIntent)
        finish()
    }
}
