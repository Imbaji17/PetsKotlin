package com.pets.app.common;

import android.content.Context;
import android.widget.ImageView;

import com.pets.app.utilities.CircleTransform;
import com.squareup.picasso.Picasso;

/**
 * Created by BAJIRAO on 05 January 2018.
 */
public class ImageSetter {

    public static void loadImage(Context mContext, String mUrl, int placeHolder, ImageView mImageView) {
        Picasso.with(mContext)
                .load(mUrl)
                .placeholder(placeHolder)
                .into(mImageView);
    }

    public static void loadRoundedImage(Context mContext, String mUrl, int placeHolder, ImageView mImageView) {
        Picasso.with(mContext)
                .load(mUrl)
                .placeholder(placeHolder)
                .transform(new CircleTransform())
                .into(mImageView);
    }
}
