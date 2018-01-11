package com.pets.app.model;

import com.google.gson.annotations.SerializedName;
import com.pets.app.model.object.LoginDetails;

import java.io.Serializable;

/**
 * Created by admin on 11/01/18.
 */

public class Reviews implements Serializable {

    @SerializedName("review_id")
    private String reviewId;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("type")
    private String type;

    @SerializedName("type_id")
    private String typeId;

    @SerializedName("comment")
    private String comment;

    @SerializedName("rating")
    private String rating;

    @SerializedName("created_date")
    private String createdDate;

    @SerializedName("updated_date")
    private String updatedDate;

    @SerializedName("status")
    private String status;

    @SerializedName("user")
    private LoginDetails user;

    public String getReviewId() {
        return reviewId;
    }

    public String getUserId() {
        return userId;
    }

    public String getType() {
        return type;
    }

    public String getTypeId() {
        return typeId;
    }

    public String getComment() {
        return comment;
    }

    public String getRating() {
        return rating;
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

    public LoginDetails getUser() {
        return user;
    }
}
