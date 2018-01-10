package com.pets.app.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by admin on 09/01/18.
 */

abstract class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun onBindView(`object`: Any)
}