package com.pets.app.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.pets.app.R;
import com.pets.app.adapters.FullImageAdapter;
import com.pets.app.common.ApplicationsConstants;
import com.pets.app.initialsetup.BaseActivity;
import com.pets.app.model.HostelImage;

import java.util.ArrayList;

/**
 * Created by admin on 11/01/18.
 */

public class FullImageActivity extends BaseActivity {

    private ViewPager viewPager;
    ArrayList<HostelImage> imagePagersList = new ArrayList<>();
    int selectedPosition = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        selectedPosition = getIntent().getExtras().getInt(ApplicationsConstants.ID);
        imagePagersList = (ArrayList<HostelImage>) getIntent().getSerializableExtra(ApplicationsConstants.DATA);
        initializeToolbar("");
        findIds();
        setAdapter();

    }

    private void findIds() {
        viewPager = (ViewPager) findViewById(R.id.vp_deal_images);
    }

    private void setAdapter() {
        FullImageAdapter adapter = new FullImageAdapter(this, imagePagersList);
        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(selectedPosition);
    }

}
