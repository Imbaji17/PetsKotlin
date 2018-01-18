package com.pets.app.model.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 11/01/18.
 */
public class PetUpdateRequest implements Serializable {

    @SerializedName("user_id")
    private String userId;
    @SerializedName("key")
    private String key;
    @SerializedName("timestamp")
    private String timestamp;
    @SerializedName("pet_id")
    private String pet_id;
    @SerializedName("pet_name")
    private String pet_name;
    @SerializedName("pets_type_id")
    private String pets_type_id;
    @SerializedName("breed_id")
    private String breed_id;
    @SerializedName("dob")
    private String dob;
    @SerializedName("gender")
    private String gender;
    @SerializedName("description")
    private String description;
    @SerializedName("is_ready_match")
    private String is_ready_match;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setPet_id(String pet_id) {
        this.pet_id = pet_id;
    }

    public void setPet_name(String pet_name) {
        this.pet_name = pet_name;
    }

    public void setPets_type_id(String pets_type_id) {
        this.pets_type_id = pets_type_id;
    }

    public void setBreed_id(String breed_id) {
        this.breed_id = breed_id;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIs_ready_match(String is_ready_match) {
        this.is_ready_match = is_ready_match;
    }
}
