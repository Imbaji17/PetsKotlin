package com.pets.app.model.object;

import com.google.gson.annotations.SerializedName;

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

    public String getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getEmail_id() {
        return email_id;
    }

    public String getDescription() {
        return description;
    }

    public String getIs_mobile_verified() {
        return is_mobile_verified;
    }

    public String getSocial_id() {
        return social_id;
    }

    public String getSocial_type() {
        return social_type;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public String getLocation() {
        return location;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getPhone_number() {
        return phone_number;
    }
}
