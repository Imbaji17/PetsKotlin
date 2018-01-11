package com.pets.app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 09/01/18.
 */

public class FindHostel implements Serializable {

    @SerializedName("hostel_id")
    private String hostelId;

    @SerializedName("hostel_name")
    private String hostelName;

    @SerializedName("contact_person")
    private String contactPerson;

    @SerializedName("contact_number")
    private String contactNumber;

    @SerializedName("address")
    private String address;

    @SerializedName("lat")
    private double lat;

    @SerializedName("lng")
    private double lng;

    @SerializedName("description")
    private String description;

    @SerializedName("created_date")
    private String createdDate;

    @SerializedName("updated_date")
    private String updatedDate;

    @SerializedName("status")
    private String status;

    @SerializedName("is_interest")
    private boolean isInterest;

    @SerializedName("hostel_images")
    private ArrayList<HostelImage> hostelImages;

    @SerializedName("reviews_count")
    private int reviewsCount;

    @SerializedName("avg_rating")
    private double avgRating;

    public String getHostelId() {
        return hostelId;
    }

    public String getHostelName() {
        return hostelName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
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

    public boolean isInterest() {
        return isInterest;
    }

    public void setInterest(boolean interest) {
        isInterest = interest;
    }

    public ArrayList<HostelImage> getHostelImages() {
        return hostelImages;
    }

    public int getReviewsCount() {
        return reviewsCount;
    }

    public double getAvgRating() {
        return avgRating;
    }
}
