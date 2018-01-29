package com.pets.app.model.object;

import com.google.gson.annotations.SerializedName;
import com.pets.app.model.PetsType;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 12/01/18.
 */
public class PetSitterDetails implements Serializable {

    @SerializedName("created_date")
    private String created_date;
    @SerializedName("avg_rating")
    private String avg_rating;
    @SerializedName("contact_no")
    private String contact_no;
    @SerializedName("status")
    private String status;
    @SerializedName("contact_person")
    private String contact_person;
    @SerializedName("lng")
    private String lng;
    @SerializedName("distance")
    private String distance;
    @SerializedName("updated_date")
    private String updated_date;
    @SerializedName("address")
    private String address;
    @SerializedName("description")
    private String description;
    @SerializedName("reviews_count")
    private int reviews_count;
    @SerializedName("pet_sitter_id")
    private String pet_sitter_id;
    @SerializedName("is_interest")
    private boolean is_interest;
    @SerializedName("lat")
    private String lat;

    @SerializedName("pets_type")
    private PetsType petsType;

    @SerializedName("user")
    private LoginDetails user;

    @SerializedName("pet_sitter_images")
    private ArrayList<PetImageInfo> petImages;

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(String avg_rating) {
        this.avg_rating = avg_rating;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContact_person() {
        return contact_person;
    }

    public void setContact_person(String contact_person) {
        this.contact_person = contact_person;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getReviews_count() {
        return reviews_count;
    }

    public void setReviews_count(int reviews_count) {
        this.reviews_count = reviews_count;
    }

    public String getPet_sitter_id() {
        return pet_sitter_id;
    }

    public void setPet_sitter_id(String pet_sitter_id) {
        this.pet_sitter_id = pet_sitter_id;
    }

    public boolean isIs_interest() {
        return is_interest;
    }

    public void setIs_interest(boolean is_interest) {
        this.is_interest = is_interest;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public PetsType getPetsType() {
        return petsType;
    }

    public void setPetsType(PetsType petsType) {
        this.petsType = petsType;
    }

    public LoginDetails getUser() {
        return user;
    }

    public void setUser(LoginDetails user) {
        this.user = user;
    }

    public ArrayList<PetImageInfo> getPetImages() {
        return petImages;
    }

    public void setPetImages(ArrayList<PetImageInfo> petImages) {
        this.petImages = petImages;
    }

    @Override
    public String toString() {
        return "PetSitterDetails{" +
                "created_date='" + created_date + '\'' +
                ", avg_rating='" + avg_rating + '\'' +
                ", contact_no='" + contact_no + '\'' +
                ", status='" + status + '\'' +
                ", contact_person='" + contact_person + '\'' +
                ", lng='" + lng + '\'' +
                ", distance='" + distance + '\'' +
                ", updated_date='" + updated_date + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", reviews_count='" + reviews_count + '\'' +
                ", pet_sitter_id='" + pet_sitter_id + '\'' +
                ", is_interest='" + is_interest + '\'' +
                ", lat='" + lat + '\'' +
                ", petsType=" + petsType +
                ", user=" + user +
                ", petImages=" + petImages +
                '}';
    }
}
