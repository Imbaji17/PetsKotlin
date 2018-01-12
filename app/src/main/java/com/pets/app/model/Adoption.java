package com.pets.app.model;

import com.google.gson.annotations.SerializedName;
import com.pets.app.model.object.LoginDetails;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 12/01/18.
 */

public class Adoption implements Serializable {

    @SerializedName("adoption_id")
    private String adoptionId;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("pet_name")
    private String petName;

    @SerializedName("contact_person")
    private String contactPerson;

    @SerializedName("contact_no")
    private String contactNo;

    @SerializedName("address")
    private String address;

    @SerializedName("age")
    private String age;

    @SerializedName("gender")
    private String gender;

    @SerializedName("lat")
    private String lat;

    @SerializedName("lng")
    private String lng;

    @SerializedName("profile_image")
    private String profileImage;

    @SerializedName("certificate_image")
    private String certificateImage;

    @SerializedName("description")
    private String description;

    @SerializedName("created_date")
    private String createdDate;

    @SerializedName("updated_date")
    private String updatedDate;

    @SerializedName("status")
    private String status;

    @SerializedName("pets_type")
    private PetsType petsType;

    @SerializedName("breed")
    private Breed breed;

    @SerializedName("user")
    private LoginDetails user;

    @SerializedName("is_interest")
    private boolean interest;

    @SerializedName("adoption_images")
    private ArrayList<HostelImage> adoptionImages;

    public String getAdoptionId() {
        return adoptionId;
    }

    public String getUserId() {
        return userId;
    }

    public String getPetName() {
        return petName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public String getContactNo() {
        return contactNo;
    }

    public String getAddress() {
        return address;
    }

    public String getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getCertificateImage() {
        return certificateImage;
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

    public PetsType getPetsType() {
        return petsType;
    }

    public Breed getBreed() {
        return breed;
    }

    public LoginDetails getUser() {
        return user;
    }

    public boolean isInterest() {
        return interest;
    }

    public void setInterest(boolean interest) {
        this.interest = interest;
    }

    public ArrayList<HostelImage> getAdoptionImages() {
        return adoptionImages;
    }
}
