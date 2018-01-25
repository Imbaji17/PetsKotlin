package com.pets.app.viewholders

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import com.pets.app.R
import com.pets.app.common.ImageSetter
import com.pets.app.model.Product

/**
 * Created by admin on 24/01/18.
 */

class BuyVH(itemView: View, clickListener: View.OnClickListener, from1: Int?) : RecyclerViewHolder(itemView) {

    private val ivBuy: ImageView = itemView.findViewById<View>(R.id.ivBuy) as ImageView
    private val tvName: TextView = itemView.findViewById<View>(R.id.tvName) as TextView
    private val ratingBar: RatingBar = itemView.findViewById<View>(R.id.ratingBar) as RatingBar
    private val tvReview: TextView = itemView.findViewById<View>(R.id.tvReview) as TextView
    private val tvBuy: TextView = itemView.findViewById<View>(R.id.tvBuy) as TextView
    private val tvPrice: TextView = itemView.findViewById<View>(R.id.tvPrice) as TextView
    private val llBuy: LinearLayout = itemView.findViewById<View>(R.id.llBuy) as LinearLayout
    private val context: Context = itemView.context

    private var from: Int? = from1

    init {
        this.from = from1
        llBuy.setOnClickListener(clickListener)
    }

    override fun onBindView(`object`: Any) {
        val product = `object` as Product
        llBuy.tag = product

        tvName.text = product.productName
        ImageSetter.loadImageResize(context, product.productImage, R.drawable.alert_placeholder, ivBuy)

        ratingBar.rating = product.avgRating.toFloat()
        tvReview?.text = context.resources.getQuantityString(R.plurals.reviews_plural, product.reviewsCount, product.reviewsCount)
        tvPrice?.text = String.format(context.getString(R.string.x_price), product.price)
        if (from == 1) {
            tvBuy.text = context.getString(R.string.sell)
        } else {
            tvBuy.text = context.getString(R.string.buy)
        }
    }
}
