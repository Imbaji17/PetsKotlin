package com.pets.app.viewholders;

import android.content.Context;
import android.view.View;

import com.pets.app.interfaces.AddPhotoCallback;

/**
 * Created by BAJIRAO on 11 November 2017.
 */
public class AddPhotoViewHolder extends RecyclerViewViewHolder {

    View itemView;

    public AddPhotoViewHolder(Context mContext, View itemView, final AddPhotoCallback addPhotoCallback) {
        super(itemView);
        this.itemView = itemView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = -1;
                position = (int) v.getTag();
                if (position != -1 && addPhotoCallback != null) {
                    addPhotoCallback.onAddPhotoClick(position);
                }
            }
        });
    }

    @Override
    public void onBindView(Object object, int position) {
        itemView.setTag(position);
    }
}
