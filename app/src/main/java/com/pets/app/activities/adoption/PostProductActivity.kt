package com.pets.app.activities.adoption

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.pets.app.R
import com.pets.app.utilities.ImagePicker

class PostProductActivity : ImagePicker(), View.OnClickListener {

    private var ivPost: ImageView? = null
    private var tvAddImage: TextView? = null
    private var etProductName: EditText? = null
    private var etSelectCategory: EditText? = null
    private var etPrice: EditText? = null
    private var etDescription: EditText? = null
    private var btnPost: Button? = null

    companion object {
        private val TAG = PostProductActivity::class.java.simpleName
        fun startActivity(activity: Activity, requestCode: Int) {
            val intent = Intent(activity, PostProductActivity::class.java)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_product)
        initializeToolbar(getString(R.string.post_your_product))
        initView()
    }

    private fun initView() {
        ivPost = findViewById(R.id.ivPost)
        tvAddImage = findViewById(R.id.tvAddImage)
        etProductName = findViewById(R.id.etProductName)
        etSelectCategory = findViewById(R.id.etSelectCategory)
        etPrice = findViewById(R.id.etPrice)
        etDescription = findViewById(R.id.etDescription)
        btnPost = findViewById(R.id.btnPost)
        ivPost!!.setOnClickListener(this)
        btnPost!!.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {

        when (p0!!.id) {
            R.id.ivPost -> {
                showTakeImagePopup()
            }

            R.id.btnPost -> {
//                addFunZone()
            }
        }
    }


}
