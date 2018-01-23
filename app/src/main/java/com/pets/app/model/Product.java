package com.pets.app.model;

import com.google.gson.annotations.SerializedName;
import com.pets.app.model.object.LoginDetails;

import java.io.Serializable;

/**
 * Created by admin on 23/01/18.
 */

public class Product implements Serializable {

    @SerializedName("product_id")
    private String productId;

    @SerializedName("product_name")
    private String productName;

    @SerializedName("pets_type_id")
    private String petsTypeId;

    @SerializedName("price")
    private String price;

    @SerializedName("description")
    private String description;

    @SerializedName("product_image")
    private String productImage;

    @SerializedName("product_type")
    private String productType;

    @SerializedName("created_date")
    private String createdDate;

    @SerializedName("updated_date")
    private String updatedDate;

    @SerializedName("status")
    private String status;

    @SerializedName("reviews_count")
    private int reviewsCount;

    @SerializedName("avg_rating")
    private Double avgRating;

    @SerializedName("pets_type")
    private PetsType petsType;

    @SerializedName("user")
    private LoginDetails user;

    @SerializedName("category")
    private Category category;

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getPetsTypeId() {
        return petsTypeId;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getProductType() {
        return productType;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public String getStatus() {
        return status;
    }

    public int getReviewsCount() {
        return reviewsCount;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public PetsType getPetsType() {
        return petsType;
    }

    public LoginDetails getUser() {
        return user;
    }

    public Category getCategory() {
        return category;
    }
}
