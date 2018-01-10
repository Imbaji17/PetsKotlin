package com.pets.app.adapters

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.pets.app.R
import java.util.*

/**
 * Created by admin on 10/01/18.
 */

class ImageAdapter : PagerAdapter() {
    private var mContext: Context? = null
    private var mLayoutInflater: LayoutInflater? = null
    private var imagesResourceIdList = ArrayList<Int>()

    fun ImageAdapter(context: Context, imagesResourceIdList: ArrayList<Int>) {
        this.mContext = context
        this.imagesResourceIdList = imagesResourceIdList
        mLayoutInflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        return imagesResourceIdList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var itemView = mLayoutInflater!!.inflate(R.layout.item_image, container, false)
        val imageView: ImageView = itemView.findViewById<View>(R.id.tvName) as ImageView
        val id = imagesResourceIdList[position]
        imageView.setImageResource(id)
        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}