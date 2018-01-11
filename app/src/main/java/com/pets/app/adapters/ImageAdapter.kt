package com.pets.app.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.pets.app.R
import com.pets.app.activities.FullImageActivity
import com.pets.app.activities.HostelDetailActivity
import com.pets.app.common.ApplicationsConstants
import com.pets.app.common.ImageSetter
import com.pets.app.model.HostelImage
import java.util.*

/**
 * Created by admin on 10/01/18.
 */

class ImageAdapter(context: Context, hostelImages: ArrayList<HostelImage>) : PagerAdapter() {
    private var mContext: Context? = null
    private var mLayoutInflater: LayoutInflater? = null
    private var imagesResourceIdList = ArrayList<HostelImage>()

    init {
        this.imagesResourceIdList = hostelImages;
        this.mContext = context
        mLayoutInflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
//    fun ImageAdapter(context: Context, imagesResourceIdList: ArrayList<HostelImage>) {
//        this.mContext = context
//        this.imagesResourceIdList = imagesResourceIdList
//        mLayoutInflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//    }

    override fun getCount(): Int {
        return imagesResourceIdList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var itemView = mLayoutInflater!!.inflate(R.layout.item_image, container, false)
        val imageView: ImageView = itemView.findViewById<View>(R.id.imageView) as ImageView
        ImageSetter.loadImage(mContext, imagesResourceIdList[position].image, R.drawable.alert_placeholder, imageView)
        container.addView(itemView)

        imageView.setOnClickListener {

            val bundle = Bundle()
            bundle.putSerializable(ApplicationsConstants.DATA, imagesResourceIdList)
            val intent = Intent(mContext, FullImageActivity::class.java)
            intent.putExtra(ApplicationsConstants.ID, position)
            intent.putExtras(bundle)
            mContext?.startActivity(intent)

        }
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}