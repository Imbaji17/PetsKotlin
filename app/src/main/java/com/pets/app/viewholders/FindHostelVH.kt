package com.pets.app.viewholders

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.pets.app.R
import com.pets.app.common.ImageSetter
import com.pets.app.model.FindHostel
import com.squareup.picasso.Picasso

/**
 * Created by admin on 09/01/18.
 */

class FindHostelVH(itemView: View, clickListener: View.OnClickListener) : RecyclerViewHolder(itemView) {

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
        val findHostel = `object` as FindHostel
        llFindHostel.tag = findHostel
        ivFavourite.tag = findHostel

        tvName.text = if (!TextUtils.isEmpty(findHostel.hostelName)) findHostel.hostelName else ""
        tvLocation.text = if (!TextUtils.isEmpty(findHostel.address)) findHostel.address else ""

        if (!findHostel.hostelImages.isEmpty() && !TextUtils.isEmpty(findHostel.hostelImages[0].image)) {
            ImageSetter.loadImage(context, findHostel.hostelImages[0].image, R.drawable.alert_placeholder, ivFindHostel)
//            Picasso.with(context)
//                    .load(findHostel.hostelImages[0].image)
//                    .placeholder(R.drawable.alert_placeholder)
//                    .into(ivFindHostel)
        }
        ivFavourite.setImageResource(if (findHostel.isInterest) R.drawable.fav1 else R.drawable.fav2)
    }
}