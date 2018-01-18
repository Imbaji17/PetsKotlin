package com.pets.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pets.app.R;
import com.pets.app.common.ImageSetter;
import com.pets.app.model.HostelImage;
import com.pets.app.model.object.PetDetails;

import java.util.ArrayList;

/**
 * Created by admin on 11/01/18.
 */
public class FullImageAdapter extends PagerAdapter {

    Context context;
    ArrayList<Object> list;

    public FullImageAdapter(Context context, ArrayList<Object> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View viewItem = inflater.inflate(R.layout.item_full_image, container, false);
        ImageView ivFull = viewItem.findViewById(R.id.ivFull);

        if (list.get(position) instanceof HostelImage) {
            ImageSetter.loadImage(context, ((HostelImage) list.get(position)).getImage(), R.drawable.alert_placeholder, ivFull);
        } else if (list.get(position) instanceof PetDetails) {
            ImageSetter.loadImage(context, ((PetDetails) list.get(position)).getPet_image(), R.drawable.alert_placeholder, ivFull);
        }

        (container).addView(viewItem);
        return viewItem;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}