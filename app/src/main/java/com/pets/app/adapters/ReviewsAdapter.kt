package com.pets.app.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pets.app.R
import com.pets.app.model.FindHostel
import com.pets.app.viewholders.FindHostelVH
import com.pets.app.viewholders.RecyclerViewHolder
import com.pets.app.viewholders.ReviewsVH
import java.util.ArrayList

/**
 * Created by admin on 11/01/18.
 */

class ReviewsAdapter(arrayList: ArrayList<Any>, private val clickListener: View.OnClickListener) : RecyclerView.Adapter<RecyclerViewHolder>() {

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
                val homeCategoryLayout = inflater.inflate(R.layout.item_review, parent, false)
                viewHolder = ReviewsVH(homeCategoryLayout, clickListener)
            }
            else -> {
                val layout = inflater.inflate(R.layout.item_review, parent, false)
                viewHolder = ReviewsVH(layout, clickListener)
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
        return if (items[position] is FindHostel) {
            FIND_HOSTEL
        } else -1
    }
}
