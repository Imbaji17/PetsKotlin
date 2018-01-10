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
import com.squareup.picasso.Picasso;

/**
 * Created by admin on 09/01/18.
 */

public class FindHostelVH extends RecyclerViewHolder {

    private final ImageView ivFindHostel;
    private final ImageView ivFavourite;
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
        ivFavourite = itemView.findViewById(R.id.ivFavourite);
        llFindHostel.setOnClickListener(clickListener);
        ivFavourite.setOnClickListener(clickListener);
    }

    @Override
    public void onBindView(Object object) {
        final FindHostel findHostel = (FindHostel) object;
        llFindHostel.setTag(findHostel);

        tvName.setText(!TextUtils.isEmpty(findHostel.getHostelName()) ? findHostel.getHostelName() : "");
        tvLocation.setText(!TextUtils.isEmpty(findHostel.getAddress()) ? findHostel.getAddress() : "");

        if (!findHostel.getHostelImages().isEmpty() && !TextUtils.isEmpty(findHostel.getHostelImages().get(0).getImage())) {
            Picasso.with(context)
                    .load(findHostel.getHostelImages().get(0).getImage())
                    .placeholder(R.drawable.alert_placeholder)
                    .into(ivFindHostel);
        }
        ivFavourite.setImageResource(findHostel.isInterest() ? R.drawable.fav1 : R.drawable.fav2);
    }
}