package com.pets.app.model.object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by BAJIRAO on 17 January 2018.
 */
public class PetImageInfo implements Serializable {

    @SerializedName("pet_image_id")
    private String pet_image_id;

    @SerializedName("pet_image")
    private String pet_image;

    public String getPet_image_id() {
        return pet_image_id;
    }

    public String getPet_image() {
        return pet_image;
    }

    @Override
    public String toString() {
        return "PetImageInfo{" +
                "pet_image_id='" + pet_image_id + '\'' +
                ", pet_image='" + pet_image + '\'' +
                '}';
    }
}
