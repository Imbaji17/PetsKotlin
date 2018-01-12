package com.pets.app.adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.pets.app.R
import com.pets.app.interfaces.SimpleItemClickListener
import com.pets.app.model.`object`.Country
import java.util.*

/**
 * Created by BAJIRAO on 12 January 2018.
 */
class CountryListAdapter(mActivity: Activity, countryList: ArrayList<Country>) : RecyclerView.Adapter<CountryListAdapter.MyViewHolder>() {

    private var mActivity: Activity
    private var mList: ArrayList<Country>
    private var itemClick: SimpleItemClickListener? = null
    private var itemsCopy = ArrayList<Country>()

    init {
        this.mActivity = mActivity
        this.itemsCopy = ArrayList<Country>()
        this.itemsCopy.addAll(countryList)
        this.mList = countryList
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {

        val v: View = LayoutInflater.from(parent?.context).inflate(R.layout.item_country_list, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {

        val country: Country = mList.get(position)

        holder?.tvCountryName?.text = country.countryName
        holder?.tvCountryCode?.text = "+".plus(country.phoneCode)
        holder?.relMain?.setOnClickListener({
            itemClick?.onItemClick(country)
        })
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        internal var tvCountryName: TextView? = null
        internal var tvCountryCode: TextView? = null
        internal var relMain: RelativeLayout? = null

        init {
            tvCountryName = itemView?.findViewById(R.id.tvCountry)
            tvCountryCode = itemView?.findViewById(R.id.tvCountryCode)
            relMain = itemView?.findViewById(R.id.relMain)
        }
    }

    fun setItemClick(itemClick: SimpleItemClickListener) {
        this.itemClick = itemClick
    }

    fun countryFilter(text: String) {
        var text = text
        mList.clear()
        if (text.isEmpty()) {
            mList.addAll(itemsCopy)
        } else {
            text = text.toLowerCase()
            for (item in itemsCopy) {
                if (item.countryName.toLowerCase().contains(text)) {
                    mList.add(item)
                }
            }
        }
        notifyDataSetChanged()
    }
}