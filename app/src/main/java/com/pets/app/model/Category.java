package com.pets.app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 23/01/18.
 */

public class Category implements Serializable {

    @SerializedName("product_category_id")
    private String productCategoryId;

    @SerializedName("product_category_name")
    private String productCategoryName;

    private boolean isSelected;

    public String getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(String productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public String getProductCategoryName() {
        return productCategoryName;
    }

    public void setProductCategoryName(String productCategoryName) {
        this.productCategoryName = productCategoryName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
