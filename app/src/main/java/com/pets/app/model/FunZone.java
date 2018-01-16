package com.pets.app.model;

import com.google.gson.annotations.SerializedName;
import com.pets.app.model.object.LoginDetails;

import java.io.Serializable;

/**
 * Created by admin on 16/01/18.
 */

public class FunZone implements Serializable {

    @SerializedName("fun_zone_id")
    private String funZoneId;
    @SerializedName("title")
    private String title;
    @SerializedName("contact_person")
    private String contactPerson;
    @SerializedName("contact_no")
    private String contactNo;
    @SerializedName("email_id")
    private String emailId;
    @SerializedName("fun_zone_image")
    private String funZoneImage;
    @SerializedName("video_thumb")
    private String videoThumb;
    @SerializedName("type")
    private String type;
    @SerializedName("address")
    private String address;
    @SerializedName("lat")
    private String lat;
    @SerializedName("lng")
    private String lng;
    @SerializedName("description")
    private String description;
    @SerializedName("created_date")
    private String createdDate;
    @SerializedName("updated_date")
    private String updatedDate;
    @SerializedName("status")
    private String status;
    @SerializedName("user")
    private LoginDetails user;
    @SerializedName("is_fun_zone_like")
    private boolean funZoneLike;

    public String getFunZoneId() {
        return funZoneId;
    }

    public String getTitle() {
        return title;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public String getContactNo() {
        return contactNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getFunZoneImage() {
        return funZoneImage;
    }

    public String getVideoThumb() {
        return videoThumb;
    }

    public String getType() {
        return type;
    }

    public String getAddress() {
        return address;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getDescription() {
        return description;
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

    public boolean isFunZoneLike() {
        return funZoneLike;
    }

    public void setFunZoneLike(boolean funZoneLike) {
        this.funZoneLike = funZoneLike;
    }
}
