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

    //for add update adoption
    @SerializedName("key")
    private String key;

    @SerializedName("timestamp")
    private String timestamp;

    @SerializedName("pets_type_id")
    private String petsTypeId;

    @SerializedName("breed_id")
    private String breedId;

    public String getAdoptionId() {
        return adoptionId;
    }

    public void setAdoptionId(String adoptionId) {
        this.adoptionId = adoptionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getCertificateImage() {
        return certificateImage;
    }

    public void setCertificateImage(String certificateImage) {
        this.certificateImage = certificateImage;
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

    public PetsType getPetsType() {
        return petsType;
    }

    public void setPetsType(PetsType petsType) {
        this.petsType = petsType;
    }

    public Breed getBreed() {
        return breed;
    }

    public void setBreed(Breed breed) {
        this.breed = breed;
    }

    public LoginDetails getUser() {
        return user;
    }

    public void setUser(LoginDetails user) {
        this.user = user;
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

    public void setAdoptionImages(ArrayList<HostelImage> adoptionImages) {
        this.adoptionImages = adoptionImages;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPetsTypeId() {
        return petsTypeId;
    }

    public void setPetsTypeId(String petsTypeId) {
        this.petsTypeId = petsTypeId;
    }

    public String getBreedId() {
        return breedId;
    }

    public void setBreedId(String breedId) {
        this.breedId = breedId;
    }
}
