package com.pets.app.viewholders

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.pets.app.R
import com.pets.app.common.ImageSetter
import com.pets.app.model.Adoption
import com.pets.app.model.FindHostel

/**
 * Created by admin on 12/01/18.
 */

class AdoptionListVH(itemView: View, clickListener: View.OnClickListener) : RecyclerViewHolder(itemView) {

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
        val adoption = `object` as Adoption
        llFindHostel.tag = adoption
        ivFavourite.tag = adoption

        tvName.text = if (!TextUtils.isEmpty(adoption.contactPerson)) adoption.contactPerson else ""
        tvLocation.text = if (!TextUtils.isEmpty(adoption.address)) adoption.address else ""
        ImageSetter.loadImage(context, adoption.profileImage, R.drawable.alert_placeholder, ivFindHostel)
//        if (!adoption.adoptionImages.isEmpty() && !TextUtils.isEmpty(adoption.adoptionImages[0].image)) {
//            ImageSetter.loadImage(context, adoption.adoptionImages[0].image, R.drawable.alert_placeholder, ivFindHostel)
//        } else {
//            ivFindHostel.setImageResource(R.drawable.alert_placeholder)
//        }
        ivFavourite.setImageResource(if (adoption.isInterest) R.drawable.fav1 else R.drawable.fav2)
    }
}
