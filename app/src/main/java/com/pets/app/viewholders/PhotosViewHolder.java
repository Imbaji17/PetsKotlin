package com.pets.app.viewholders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pets.app.R;
import com.pets.app.interfaces.AddPhotoCallback;
import com.pets.app.model.object.PhotosInfo;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;

/**
 * Created by BAJIRAO on 11 November 2017.
 */
public class PhotosViewHolder extends RecyclerViewViewHolder {

    private final ImageView photosIv;
    private final TextView deleteImageView;
    private final AddPhotoCallback addPhotoCallback;
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = -1;
            position = (int) v.getTag();
            if (position != -1)
                if (addPhotoCallback != null) {
                    addPhotoCallback.onDeleteClick(position);
                }
        }
    };
    private boolean isShow;
    private View itemView;
    private Context context;

    public PhotosViewHolder(Context mContext, View itemView, AddPhotoCallback addPhotoCallback, boolean isShow) {
        super(itemView);
        context = mContext;
        this.addPhotoCallback = addPhotoCallback;
        this.isShow = isShow;
        deleteImageView = itemView.findViewById(R.id.remove_photo);
        photosIv = itemView.findViewById(R.id.photo_img);

        if (isShow) {
            deleteImageView.setVisibility(View.GONE);
        }
        deleteImageView.setOnClickListener(onClickListener);
//        this.isDeleteHide = isDelete;
    }

    @Override
    public void onBindView(Object object, int position) {
        deleteImageView.setTag(position);

        if (isShow)
            deleteImageView.setVisibility(View.GONE);
        PhotosInfo photos = (PhotosInfo) object;
        if (photos != null) {
            if (!TextUtils.isEmpty(photos.getUrl())) {
//                resize(70, 70).
                if (!photos.getUrl().contains("http")) {
                    File file = new File(photos.getUrl());
                    Picasso.with(context).load(file).resize(100, 100).placeholder(R.drawable.gallery_placeholder).into(new Target() {

                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            photosIv.setImageDrawable(new BitmapDrawable(context.getResources(), bitmap));
                        }

                        @Override
                        public void onBitmapFailed(final Drawable errorDrawable) {
                            Log.d("TAG", "FAILED");
                        }

                        @Override
                        public void onPrepareLoad(final Drawable placeHolderDrawable) {
                            Log.d("TAG", "Prepare Load");
                        }
                    });
                } else {
                    loadImage(photos.getUrl());
                }
            }
        }
    }

    private void loadImage(String url) {
        Picasso.with(context).load(url).resize(100, 100).placeholder(R.drawable.gallery_placeholder).into(new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                photosIv.setImageDrawable(new BitmapDrawable(context.getResources(), bitmap));
            }

            @Override
            public void onBitmapFailed(final Drawable errorDrawable) {
                Log.d("TAG", "FAILED");
            }

            @Override
            public void onPrepareLoad(final Drawable placeHolderDrawable) {
                Log.d("TAG", "Prepare Load");
            }
        });
    }


}