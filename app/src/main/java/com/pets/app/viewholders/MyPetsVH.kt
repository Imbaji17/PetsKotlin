package com.pets.app.viewholders

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.pets.app.R
import com.pets.app.common.ImageSetter
import com.pets.app.interfaces.SimpleItemClickListener
import com.pets.app.model.`object`.PetDetails
import com.pets.app.utilities.DateFormatter

/**
 * Created by BAJIRAO on 16 January 2018.
 */
class MyPetsVH(mContext: Context, itemView: View?, itemClick: SimpleItemClickListener) : RecyclerViewViewHolder(itemView) {

    private var mContext: Context
    private var tvName: TextView? = null
    private var tvDate: TextView? = null
    private var imgView: ImageView? = null
    private var itemClick: SimpleItemClickListener
    private var petObj: PetDetails? = null

    init {
        this.mContext = mContext
        this.itemClick = itemClick
        tvName = itemView?.findViewById(R.id.tvName)
        tvDate = itemView?.findViewById(R.id.tvDate)
        imgView = itemView?.findViewById(R.id.imgView)
        itemView?.setOnClickListener { itemClick.onItemClick(petObj) }
    }

    override fun onBindView(`object`: Any?, position: Int) {

        if (`object` is PetDetails) {

            petObj = `object`

            tvName?.text = if (!TextUtils.isEmpty(petObj!!.pet_name)) petObj!!.pet_name else ""

            if (!TextUtils.isEmpty(petObj!!.dob)) {
                tvDate?.text = DateFormatter.getFormattedDate(DateFormatter.yyyy_MM_dd_str, petObj!!.dob, DateFormatter.REVIEW_DATE_FORMAT)
            }

            if (!TextUtils.isEmpty(petObj!!.pet_image)) {
                ImageSetter.loadRoundedImage(mContext, petObj!!.pet_image, R.drawable.dog, imgView)
            } else {
                imgView!!.setImageResource(R.drawable.pet_profile_placeholder)
            }
        }
    }
}