package com.pets.app.model.object;

import com.google.gson.annotations.SerializedName;
import com.pets.app.common.Constants;

import java.io.Serializable;

/**
 * Created by BAJIRAO on 10 January 2018.
 */
public class LoginDetails implements Serializable {

    @SerializedName("user_id")
    private String user_id;
    @SerializedName("name")
    private String name;
    @SerializedName("email_id")
    private String email_id;
    @SerializedName("description")
    private String description;
    @SerializedName("is_mobile_verified")
    private String is_mobile_verified;
    @SerializedName("social_id")
    private String social_id;
    @SerializedName("social_type")
    private String social_type;
    @SerializedName("phone_code")
    private String phone_code;
    @SerializedName("profile_image")
    private String profile_image;
    @SerializedName("location")
    private String location;
    @SerializedName("lat")
    private String lat;
    @SerializedName("lng")
    private String lng;
    @SerializedName("phone_number")
    private String phone_number;
    @SerializedName("isNewUser")
    private String isNewUser;

    public boolean isMobileVerified() {
        return is_mobile_verified.equalsIgnoreCase(Constants.YES);
    }

    public boolean isNewUser() {
        return isNewUser.equalsIgnoreCase("false");
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIs_mobile_verified() {
        return is_mobile_verified;
    }

    public void setIs_mobile_verified(String is_mobile_verified) {
        this.is_mobile_verified = is_mobile_verified;
    }

    public String getSocial_id() {
        return social_id;
    }

    public void setSocial_id(String social_id) {
        this.social_id = social_id;
    }

    public String getSocial_type() {
        return social_type;
    }

    public void setSocial_type(String social_type) {
        this.social_type = social_type;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getIsNewUser() {
        return isNewUser;
    }

    public void setIsNewUser(String isNewUser) {
        this.isNewUser = isNewUser;
    }

    @Override
    public String toString() {
        return "LoginDetails{" +
                "user_id='" + user_id + '\'' +
                ", name='" + name + '\'' +
                ", email_id='" + email_id + '\'' +
                ", description='" + description + '\'' +
                ", is_mobile_verified='" + is_mobile_verified + '\'' +
                ", social_id='" + social_id + '\'' +
                ", social_type='" + social_type + '\'' +
                ", phone_code='" + phone_code + '\'' +
                ", profile_image='" + profile_image + '\'' +
                ", location='" + location + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", isNewUser='" + isNewUser + '\'' +
                '}';
    }
}
