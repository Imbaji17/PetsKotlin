package com.pets.app.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import com.pets.app.R;
import com.pets.app.adapters.FullImageAdapter;
import com.pets.app.common.ApplicationsConstants;
import com.pets.app.initialsetup.BaseActivity;

import java.util.ArrayList;

/**
 * Created by admin on 11/01/18.
 */

public class FullImageActivity extends BaseActivity {

    ArrayList<Object> imagePagersList = new ArrayList<>();
    int selectedPosition = 0;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        selectedPosition = getIntent().getExtras().getInt(ApplicationsConstants.ID);
        imagePagersList = (ArrayList<Object>) getIntent().getSerializableExtra(ApplicationsConstants.DATA);
        initializeToolbar("");
        findIds();
        setAdapter();
    }

    private void findIds() {
        viewPager = findViewById(R.id.vp_deal_images);
    }

    private void setAdapter() {
        FullImageAdapter adapter = new FullImageAdapter(this, imagePagersList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(selectedPosition);
    }
}
