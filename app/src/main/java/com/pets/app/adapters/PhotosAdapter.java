package com.pets.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pets.app.R;
import com.pets.app.interfaces.AddPhotoCallback;
import com.pets.app.model.object.PhotosInfo;
import com.pets.app.viewholders.AddPhotoViewHolder;
import com.pets.app.viewholders.PhotosViewHolder;
import com.pets.app.viewholders.RecyclerViewViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BAJIRAO on 11 November 2017.
 */
public class PhotosAdapter extends RecyclerView.Adapter<RecyclerViewViewHolder> {

    private static final int PHOTOS = 1;
    private static final int ADD_PHOTO = 2;
    private final Context mContext;
    private boolean isShow;
    private List<Object> items = new ArrayList<>();
    private AddPhotoCallback addPhotoCallback;

    public PhotosAdapter(Context context, ArrayList<Object> arrayList, boolean isShow) {
        this.mContext = context;
        this.items = arrayList;
        this.isShow = isShow;
    }

    public void setItemClickListener(AddPhotoCallback addPhotoVideoCallback) {
        this.addPhotoCallback = addPhotoVideoCallback;
    }

    @Override
    public RecyclerViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerViewViewHolder viewHolder = null;

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {

            case PHOTOS:
                View participantsLayout = inflater.inflate(R.layout.item_photo_gallery, parent, false);
                viewHolder = new PhotosViewHolder(mContext, participantsLayout, addPhotoCallback, isShow);
                break;

            case ADD_PHOTO:
                View addParticipantsLayout = inflater.inflate(R.layout.item_add_photo, parent, false);
                viewHolder = new AddPhotoViewHolder(mContext, addParticipantsLayout, addPhotoCallback);
                break;

            default:
                View defaultLayout = inflater.inflate(R.layout.item_add_photo, parent, false);
                viewHolder = new AddPhotoViewHolder(mContext, defaultLayout, addPhotoCallback);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewViewHolder recyclerViewViewHolder, int position) {
        recyclerViewViewHolder.onBindView(items.get(position), position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof PhotosInfo) {
            return PHOTOS;
        } else if (items.get(position) instanceof String) {
            return ADD_PHOTO;
        }
        return -1;
    }
}
