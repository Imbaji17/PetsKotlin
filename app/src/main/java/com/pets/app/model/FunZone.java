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

    //For add update
    @SerializedName("user_id")
    private String userId;
    @SerializedName("key")
    private String key;
    @SerializedName("timestamp")
    private String timestamp;

    public String getFunZoneId() {
        return funZoneId;
    }

    public void setFunZoneId(String funZoneId) {
        this.funZoneId = funZoneId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getFunZoneImage() {
        return funZoneImage;
    }

    public void setFunZoneImage(String funZoneImage) {
        this.funZoneImage = funZoneImage;
    }

    public String getVideoThumb() {
        return videoThumb;
    }

    public void setVideoThumb(String videoThumb) {
        this.videoThumb = videoThumb;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LoginDetails getUser() {
        return user;
    }

    public void setUser(LoginDetails user) {
        this.user = user;
    }

    public boolean isFunZoneLike() {
        return funZoneLike;
    }

    public void setFunZoneLike(boolean funZoneLike) {
        this.funZoneLike = funZoneLike;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
