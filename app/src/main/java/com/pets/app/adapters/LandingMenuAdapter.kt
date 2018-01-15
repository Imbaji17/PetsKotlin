package com.pets.app.adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.pets.app.R
import com.pets.app.interfaces.SimpleItemClickListener
import com.pets.app.model.`object`.LandingDetails
import java.util.*

/**
 * Created by BAJIRAO on 12 January 2018.
 */
class LandingMenuAdapter(mActivity: Activity, countryList: ArrayList<LandingDetails>) : RecyclerView.Adapter<LandingMenuAdapter.MyViewHolder>() {

    private var mActivity: Activity
    private var mList: ArrayList<LandingDetails>
    private var itemClick: SimpleItemClickListener? = null

    init {
        this.mActivity = mActivity
        this.mList = countryList
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {

        val v: View = LayoutInflater.from(parent?.context).inflate(R.layout.item_landing_menu, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {

        val info: LandingDetails = mList.get(position)

        holder?.tvName?.text = info.name

        holder?.imgView?.setImageResource(info.image)

        holder?.linMain?.setOnClickListener({
            itemClick?.onItemClick(position)
        })
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        internal var imgView: ImageView? = null
        internal var tvName: TextView? = null
        internal var linMain: LinearLayout? = null

        init {
            imgView = itemView?.findViewById(R.id.imgView)
            tvName = itemView?.findViewById(R.id.tvName)
            linMain = itemView?.findViewById(R.id.linMain)
        }
    }

    fun setItemClick(itemClick: SimpleItemClickListener) {
        this.itemClick = itemClick
    }
}