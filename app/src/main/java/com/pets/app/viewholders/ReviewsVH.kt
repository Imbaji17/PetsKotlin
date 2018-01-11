package com.pets.app.viewholders

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.pets.app.R
import com.pets.app.common.ImageSetter
import com.pets.app.model.FindHostel
import com.pets.app.model.Reviews

/**
 * Created by admin on 11/01/18.
 */
class ReviewsVH(itemView: View, clickListener: View.OnClickListener) : RecyclerViewHolder(itemView) {

    private val ivFindHostel: ImageView = itemView.findViewById<View>(R.id.ivFindHostel) as ImageView
    private val ivFavourite: ImageView = itemView.findViewById(R.id.ivFavourite)
    private val tvName: TextView = itemView.findViewById<View>(R.id.tvName) as TextView
    private val tvLocation: TextView = itemView.findViewById<View>(R.id.tvLocation) as TextView
    private val llFindHostel: LinearLayout = itemView.findViewById<View>(R.id.llFindHostel) as LinearLayout
    private val context: Context = itemView.context

    init {
        llFindHostel.setOnClickListener(clickListener)
        ivFavourite.setOnClickListener(clickListener)
    }

    override fun onBindView(`object`: Any) {
//        val revies = `object` as Reviews
//        llFindHostel.tag = findHostel
//        ivFavourite.tag = findHostel
//
//        tvName.text = if (!TextUtils.isEmpty(findHostel.hostelName)) findHostel.hostelName else ""
//        tvLocation.text = if (!TextUtils.isEmpty(findHostel.address)) findHostel.address else ""
//        if (!findHostel.hostelImages.isEmpty() && !TextUtils.isEmpty(findHostel.hostelImages[0].image)) {
//            ImageSetter.loadImage(context, findHostel.hostelImages[0].image, R.drawable.alert_placeholder, ivFindHostel)
//        } else {
//            ivFindHostel.setImageResource(R.drawable.alert_placeholder)
//        }
//        ivFavourite.setImageResource(if (findHostel.isInterest) R.drawable.fav1 else R.drawable.fav2)
    }
}