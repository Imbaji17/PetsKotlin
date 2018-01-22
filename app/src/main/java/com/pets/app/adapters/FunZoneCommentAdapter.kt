package com.pets.app.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pets.app.R
import com.pets.app.model.FunZoneComment
import com.pets.app.viewholders.FunZoneCommentVH
import com.pets.app.viewholders.RecyclerViewHolder
import java.util.*

/**
 * Created by admin on 18/01/18.
 */

class FunZoneCommentAdapter(arrayList: ArrayList<Any>, private val clickListener: View.OnClickListener) : RecyclerView.Adapter<RecyclerViewHolder>() {

    private val funZoneComment = 1
    private var items = ArrayList<Any>()

    init {
        this.items = arrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        var viewHolder: RecyclerViewHolder?
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            funZoneComment -> {
                val homeCategoryLayout = inflater.inflate(R.layout.item_fun_zone_comments, parent, false)
                viewHolder = FunZoneCommentVH(homeCategoryLayout, clickListener)
            }
            else -> {
                val layout = inflater.inflate(R.layout.item_fun_zone_comments, parent, false)
                viewHolder = FunZoneCommentVH(layout, clickListener)
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
        return if (items[position] is FunZoneComment) {
            funZoneComment
        } else -1
    }
}
