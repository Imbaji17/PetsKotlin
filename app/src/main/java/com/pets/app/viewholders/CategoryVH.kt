package com.pets.app.viewholders

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.pets.app.R
import com.pets.app.interfaces.SimpleItemClickListener
import com.pets.app.model.Breed
import com.pets.app.model.Category

/**
 * Created by admin on 24/01/18.
 */

class CategoryVH(mContext: Context, itemView: View?, itemClick: SimpleItemClickListener) : RecyclerViewViewHolder(itemView) {

    private var mContext: Context
    private var tvName: TextView? = null
    private var imgCheckBox: ImageView? = null
    private var itemClick: SimpleItemClickListener
    private var categoryObj: Category? = null

    init {
        this.mContext = mContext
        this.itemClick = itemClick
        tvName = itemView?.findViewById(R.id.tvName)
        imgCheckBox = itemView?.findViewById(R.id.imgCheckBox)
        itemView?.setOnClickListener { itemClick.onItemClick(categoryObj) }
    }

    override fun onBindView(`object`: Any?, position: Int) {

        if (`object` is Category) {

            categoryObj = `object`

            tvName?.text = categoryObj?.productCategoryName

            if (categoryObj!!.isSelected) {
                imgCheckBox!!.visibility = View.VISIBLE
            } else {
                imgCheckBox!!.visibility = View.INVISIBLE
            }
        }
    }
}
