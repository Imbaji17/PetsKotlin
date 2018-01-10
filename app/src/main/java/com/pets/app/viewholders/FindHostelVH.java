package com.pets.app.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pets.app.R;
import com.pets.app.model.FindHostel;

/**
 * Created by admin on 09/01/18.
 */

public class FindHostelVH extends RecyclerViewHolder {

    private final ImageView ivFindHostel;
    private final TextView tvName;
    private final TextView tvLocation;
    private final LinearLayout llFindHostel;
    private Context context;

    public FindHostelVH(View itemView, View.OnClickListener clickListener) {
        super(itemView);
        this.context = itemView.getContext();
        ivFindHostel = (ImageView) itemView.findViewById(R.id.ivFindHostel);
        tvName = (TextView) itemView.findViewById(R.id.tvName);
        tvLocation = (TextView) itemView.findViewById(R.id.tvLocation);
        llFindHostel = (LinearLayout) itemView.findViewById(R.id.llFindHostel);
        llFindHostel.setOnClickListener(clickListener);
    }

    @Override
    public void onBindView(Object object) {
        final FindHostel findHostel = (FindHostel) object;
        llFindHostel.setTag(findHostel);

        tvName.setText(!TextUtils.isEmpty(findHostel.getHostelName()) ? findHostel.getHostelName() : "");
        tvLocation.setText(!TextUtils.isEmpty(findHostel.getAddress()) ? findHostel.getAddress() : "");


    }
}