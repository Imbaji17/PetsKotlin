package com.pets.app.model.object;

import com.google.gson.annotations.SerializedName;
import com.pets.app.common.Constants;
import com.pets.app.model.Breed;
import com.pets.app.model.PetsType;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 12/01/18.
 */
public class PetDetails implements Serializable {

    @SerializedName("created_date")
    private String created_date;
    @SerializedName("pet_id")
    private String pet_id;
    @SerializedName("is_ready_match")
    private String is_ready_match;
    @SerializedName("certificate_image")
    private String certificate_image;
    @SerializedName("pet_name")
    private String pet_name;
    @SerializedName("status")
    private String status;
    @SerializedName("qr_code")
    private String qr_code;
    @SerializedName("updated_date")
    private String updated_date;
    @SerializedName("description")
    private String description;
    @SerializedName("dob")
    private String dob;
    @SerializedName("gender")
    private String gender;
    @SerializedName("pet_image")
    private String pet_image;

    @SerializedName("pets_type")
    private PetsType petsType;

    @SerializedName("breed")
    private Breed breed;

    @SerializedName("user")
    private LoginDetails user;

    @SerializedName("pet_images")
    private ArrayList<PetImageInfo> petImages;

    public String getCreated_date() {
        return created_date;
    }

    public String getPet_id() {
        return pet_id;
    }

    public String getIs_ready_match() {
        return is_ready_match;
    }

    public String getCertificate_image() {
        return certificate_image;
    }

    public String getPet_name() {
        return pet_name;
    }

    public String getStatus() {
        return status;
    }

    public String getQr_code() {
        return qr_code;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public String getDescription() {
        return description;
    }

    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public String getPet_image() {
        return pet_image;
    }

    public PetsType getPetsType() {
        return petsType;
    }

    public Breed getBreed() {
        return breed;
    }

    public LoginDetails getUser() {
        return user;
    }

    public ArrayList<PetImageInfo> getPetImages() {
        return petImages;
    }

    public boolean isReadyForMatch() {

        return is_ready_match.equalsIgnoreCase(Constants.YES);
    }

    @Override
    public String toString() {
        return "PetDetails{" +
                "created_date='" + created_date + '\'' +
                ", pet_id='" + pet_id + '\'' +
                ", is_ready_match='" + is_ready_match + '\'' +
                ", certificate_image='" + certificate_image + '\'' +
                ", pet_name='" + pet_name + '\'' +
                ", status='" + status + '\'' +
                ", qr_code='" + qr_code + '\'' +
                ", updated_date='" + updated_date + '\'' +
                ", description='" + description + '\'' +
                ", dob='" + dob + '\'' +
                ", gender='" + gender + '\'' +
                ", pet_image='" + pet_image + '\'' +
                ", petsType=" + petsType +
                ", breed=" + breed +
                ", user=" + user +
                ", petImages=" + petImages +
                '}';
    }
}
