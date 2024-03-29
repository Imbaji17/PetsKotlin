package com.pets.app.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pets.app.R
import com.pets.app.model.Product
import com.pets.app.viewholders.BuyVH
import com.pets.app.viewholders.RecyclerViewHolder
import java.util.*

/**
 * Created by admin on 24/01/18.
 */

class BuyAdapter(arrayList: ArrayList<Any>, private val clickListener: View.OnClickListener, from1: Int) : RecyclerView.Adapter<RecyclerViewHolder>() {

    private val BUY = 1
    private var items = ArrayList<Any>()
    private var from: Int? = from1

    init {
        this.items = arrayList
        this.from = from1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        var viewHolder: RecyclerViewHolder?
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            BUY -> {
                val homeCategoryLayout = inflater.inflate(R.layout.item_buy, parent, false)
                viewHolder = BuyVH(homeCategoryLayout, clickListener, from)
            }
            else -> {
                val layout = inflater.inflate(R.layout.item_buy, parent, false)
                viewHolder = BuyVH(layout, clickListener, from)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(recyclerViewViewHolder: RecyclerViewHolder, i: Int) {
        recyclerViewViewHolder.onBindView(items[i])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] is Product) {
            BUY
        } else -1
    }

    fun setFrom() {
        this.from = 1
    }

}
