package com.pets.app.model;

import com.google.gson.annotations.SerializedName;
import com.pets.app.model.object.LoginDetails;

import java.io.Serializable;

/**
 * Created by admin on 18/01/18.
 */

public class FunZoneComment implements Serializable {

    @SerializedName("fun_zone_comment_id")
    private String funZoneCommentId;
    @SerializedName("fun_zone_id")
    private String funZoneId;
    @SerializedName("comment")
    private String comment;
    @SerializedName("created_date")
    private String createdDate;
    @SerializedName("updated_date")
    private String updatedDate;
    @SerializedName("status")
    private String status;
    @SerializedName("name")
    private String name;
    @SerializedName("profile_image")
    private String profileImage;
    @SerializedName("user")
    private LoginDetails user;

    public String getFunZoneCommentId() {
        return funZoneCommentId;
    }

    public String getFunZoneId() {
        return funZoneId;
    }

    public String getComment() {
        return comment;
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

    public String getName() {
        return name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public LoginDetails getUser() {
        return user;
    }
}
