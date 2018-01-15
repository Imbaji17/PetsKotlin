package com.pets.app.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by BAJIRAO on 17-Oct-16.
 */
public abstract class RecyclerViewViewHolder extends RecyclerView.ViewHolder {

    public RecyclerViewViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void onBindView(Object object, int position);
}
