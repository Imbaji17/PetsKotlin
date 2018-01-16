package com.pets.app.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pets.app.R
import com.pets.app.model.FunZone
import com.pets.app.viewholders.FunZoneVH
import com.pets.app.viewholders.RecyclerViewHolder
import java.util.*

/**
 * Created by admin on 16/01/18.
 */

class FunZoneAdapter(arrayList: ArrayList<Any>, private val clickListener: View.OnClickListener) : RecyclerView.Adapter<RecyclerViewHolder>() {

    private val FIND_HOSTEL = 1
    private var items = ArrayList<Any>()

    init {
        this.items = arrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        var viewHolder: RecyclerViewHolder?
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            FIND_HOSTEL -> {
                val homeCategoryLayout = inflater.inflate(R.layout.item_fun_zone, parent, false)
                viewHolder = FunZoneVH(homeCategoryLayout, clickListener)
            }
            else -> {
                val layout = inflater.inflate(R.layout.item_fun_zone, parent, false)
                viewHolder = FunZoneVH(layout, clickListener)
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
        return if (items[position] is FunZone) {
            FIND_HOSTEL
        } else -1
    }
}
