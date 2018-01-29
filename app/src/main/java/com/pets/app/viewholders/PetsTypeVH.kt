package com.pets.app.viewholders

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.pets.app.R
import com.pets.app.interfaces.SimpleItemClickListener
import com.pets.app.model.PetsType

/**
 * Created by BAJIRAO on 16 January 2018.
 */
class PetsTypeVH(mContext: Context, itemView: View?, itemClick: SimpleItemClickListener) : RecyclerViewViewHolder(itemView) {

    private var mContext: Context
    private var tvName: TextView? = null
    private var imgCheckBox: ImageView? = null
    private var itemClick: SimpleItemClickListener
    private var petObj: PetsType? = null

    init {
        this.mContext = mContext
        this.itemClick = itemClick
        tvName = itemView?.findViewById(R.id.tvName)
        imgCheckBox = itemView?.findViewById(R.id.imgCheckBox)
        itemView?.setOnClickListener { itemClick.onItemClick(petObj) }
    }

    override fun onBindView(`object`: Any?, position: Int) {

        if (`object` is PetsType) {

            petObj = `object`

            tvName?.text = if (!TextUtils.isEmpty(petObj!!.typeName)) petObj!!.typeName else ""

            if (petObj!!.isSelected) {
                imgCheckBox!!.visibility = View.VISIBLE
            } else {
                imgCheckBox!!.visibility = View.INVISIBLE
            }
        }
    }
}