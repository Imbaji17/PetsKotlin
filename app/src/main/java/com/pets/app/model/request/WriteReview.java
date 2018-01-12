package com.pets.app.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 12/01/18.
 */

public class WriteReview {

    @SerializedName("user_id")
    private String userId;

    @SerializedName("key")
    private String key;

    @SerializedName("timestamp")
    private String timestamp;

    @SerializedName("type_id")
    private String typeId;

    @SerializedName("review_id")
    private String reviewId;

    @SerializedName("comment")
    private String comment;

    @SerializedName("rating")
    private String rating;

    @SerializedName("type")
    private String type;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setType(String type) {
        this.type = type;
    }
}
