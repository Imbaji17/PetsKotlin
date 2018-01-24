package com.pets.app.common;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.pets.app.utilities.CircleTransform;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by BAJIRAO on 05 January 2018.
 */
public class ImageSetter {

    public static void loadImage(Context mContext, String mUrl, int placeHolder, ImageView mImageView) {
        if (!TextUtils.isEmpty(mUrl)) {
            Picasso.with(mContext)
                    .load(mUrl)
                    .placeholder(placeHolder)
                    .into(mImageView);
        } else {
            mImageView.setImageResource(placeHolder);
        }
    }

    public static void loadImageResize(Context mContext, String mUrl, int placeHolder, ImageView mImageView) {
        if (!TextUtils.isEmpty(mUrl)) {
            Picasso.with(mContext)
                    .load(mUrl)
                    .placeholder(placeHolder)
                    .resize(200, 200)
                    .into(mImageView);
        } else {
            mImageView.setImageResource(placeHolder);
        }
    }


    public static void loadRoundedImage(Context mContext, String mUrl, int placeHolder, ImageView mImageView) {
        if (!TextUtils.isEmpty(mUrl)) {
            Picasso.with(mContext)
                    .load(mUrl)
                    .placeholder(placeHolder)
                    .transform(new CircleTransform())
                    .into(mImageView);
        } else {
            mImageView.setImageResource(placeHolder);
        }
    }

    public static void loadRoundedImage(Context mContext, File mUrl, int placeHolder, ImageView mImageView) {
        if (mUrl != null) {
            Picasso.with(mContext)
                    .load(mUrl)
                    .placeholder(placeHolder)
                    .transform(new CircleTransform())
                    .into(mImageView);
        } else {
            mImageView.setImageResource(placeHolder);
        }
    }

    public static void loadImage(Context mContext, File mUrl, int placeHolder, ImageView mImageView) {
        if (mUrl != null) {
            Picasso.with(mContext)
                    .load(mUrl)
                    .placeholder(placeHolder)
                    .into(mImageView);
        } else {
            mImageView.setImageResource(placeHolder);
        }
    }
}
