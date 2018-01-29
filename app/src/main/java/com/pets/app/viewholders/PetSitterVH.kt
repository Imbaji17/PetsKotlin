package com.pets.app.viewholders

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.pets.app.R
import com.pets.app.common.ImageSetter
import com.pets.app.interfaces.SimpleItemClickListener
import com.pets.app.model.`object`.PetSitterDetails

/**
 * Created by BAJIRAO on 16 January 2018.
 */
class PetSitterVH(mContext: Context, itemView: View?, itemClick: SimpleItemClickListener) : RecyclerViewViewHolder(itemView) {

    private var mContext: Context
    private var imgPet: ImageView? = null
    private var tvName: TextView? = null
    private var tvLocation: TextView? = null
    private var tvReview: TextView? = null
    private var ratingBar: RatingBar? = null
    private var imgFavourite: ImageView? = null
    private var itemClick: SimpleItemClickListener
    private var petSitterObj: PetSitterDetails? = null

    init {
        this.mContext = mContext
        this.itemClick = itemClick
        imgPet = itemView?.findViewById(R.id.imgPet)
        tvName = itemView?.findViewById(R.id.tvName)
        tvLocation = itemView?.findViewById(R.id.tvLocation)
        tvReview = itemView?.findViewById(R.id.tvReview)
        ratingBar = itemView?.findViewById(R.id.ratingBar)
        imgFavourite = itemView?.findViewById(R.id.imgFavourite)
        itemView?.setOnClickListener { itemClick.onItemClick(petSitterObj) }
    }

    override fun onBindView(`object`: Any?, position: Int) {

        if (`object` is PetSitterDetails) {

            petSitterObj = `object`

            tvName?.text = if (!TextUtils.isEmpty(petSitterObj!!.contact_person)) petSitterObj!!.contact_person else ""

            tvLocation?.text = if (!TextUtils.isEmpty(petSitterObj!!.address)) petSitterObj!!.address else ""

            if (petSitterObj!!.petImages != null && petSitterObj!!.petImages.isNotEmpty()) {
                ImageSetter.loadImage(mContext, petSitterObj!!.petImages[0].pet_image, R.drawable.alert_placeholder, imgPet)
            } else {
                imgPet!!.setImageResource(R.drawable.alert_placeholder)
            }

            imgFavourite!!.setImageResource(if (petSitterObj!!.isIs_interest) R.drawable.fav1 else R.drawable.fav2)

            tvReview?.text = mContext.resources.getQuantityString(R.plurals.reviews_plural, petSitterObj!!.reviews_count, petSitterObj!!.reviews_count)

            ratingBar?.rating = petSitterObj!!.avg_rating.toFloat()
        }
    }
}