package com.pets.app.model.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by BAJIRAO on 10 January 2018.
 */
public class UpdateUserRequest implements Serializable {

    @SerializedName("timestamp")
    private String timestamp;
    @SerializedName("language_code")
    private String language_code;
    @SerializedName("phone_number")
    private String phone_number;
    @SerializedName("location")
    private String location;
    @SerializedName("description")
    private String description;
    @SerializedName("name")
    private String name;
    @SerializedName("phone_code")
    private String phone_code;
    @SerializedName("lng")
    private String lng;
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("email_id")
    private String email_id;
    @SerializedName("lat")
    private String lat;
    @SerializedName("key")
    private String key;

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setLanguage_code(String language_code) {
        this.language_code = language_code;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
