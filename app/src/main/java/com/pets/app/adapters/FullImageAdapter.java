package com.pets.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pets.app.R;
import com.pets.app.common.ImageSetter;
import com.pets.app.model.HostelImage;

import java.util.ArrayList;

/**
 * Created by admin on 11/01/18.
 */

public class FullImageAdapter extends PagerAdapter {

    Context context;
    ArrayList<HostelImage> list;

    public FullImageAdapter(Context context, ArrayList<HostelImage> list) {
        this.context = context;
        this.list = list;

    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View viewItem = inflater.inflate(R.layout.item_full_image, container, false);
        ImageView ivFull = (ImageView) viewItem.findViewById(R.id.ivFull);
        ImageSetter.loadImage(context, list.get(position).getImage(), R.drawable.alert_placeholder, ivFull);
        (container).addView(viewItem);
        return viewItem;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        // TODO Auto-generated method stub

        return view == ((View) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        ((ViewPager) container).removeView((View) object);
    }
}