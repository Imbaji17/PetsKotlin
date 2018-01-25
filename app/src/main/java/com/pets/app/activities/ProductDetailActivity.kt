package com.pets.app.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.pets.app.R
import com.pets.app.common.ApplicationsConstants
import com.pets.app.common.ImageSetter
import com.pets.app.initialsetup.BaseActivity
import com.pets.app.model.Product

class ProductDetailActivity : BaseActivity() {

    private var ivBuy: ImageView? = null
    private var tvName: TextView? = null
    private var ratingBar: RatingBar? = null
    private var tvReview: TextView? = null
    private var tvPrice: TextView? = null
    private var tvDescription: TextView? = null
    private var btnBuy: Button? = null
    private var product: Product? = null

    companion object {
        private val TAG = ProductDetailActivity::class.java.simpleName
        fun startActivity(activity: Activity, product: Product) {
            val intent = Intent(activity, ProductDetailActivity::class.java)
            intent.putExtra(ApplicationsConstants.DATA, product)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        init()
        initView()
        setValues()
    }

    fun init() {
        product = intent.getSerializableExtra(ApplicationsConstants.DATA) as Product?
    }

    fun initView() {
        ivBuy = findViewById(R.id.ivBuy)
        tvName = findViewById(R.id.tvName)
        ratingBar = findViewById(R.id.ratingBar)
        tvReview = findViewById(R.id.tvReview)
        tvPrice = findViewById(R.id.tvPrice)
        tvDescription = findViewById(R.id.tvDescription)
        btnBuy = findViewById(R.id.btnBuy)
    }

    private fun setValues() {
        if (product != null) {
            initializeToolbar(product!!.productName)
            tvName!!.text = product!!.productName
            ImageSetter.loadImageResize(this, product!!.productImage, R.drawable.alert_placeholder, ivBuy)
            ratingBar!!.rating = product!!.avgRating.toFloat()
            tvReview?.text = resources.getQuantityString(R.plurals.reviews_plural, product!!.reviewsCount, product!!.reviewsCount)
            tvPrice?.text = String.format(getString(R.string.x_price), product!!.price)
            tvDescription!!.text = product!!.description
//            if (from == 1) {
//                tvBuy.text = context.getString(R.string.sell)
//            } else {
//                tvBuy.text = context.getString(R.string.buy)
//            }
        }

    }

}
