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

    /*PET SITTER OBJECT*/
    @SerializedName("hostel_sitter_vet_images_id")
    private String hostel_sitter_vet_images_id;
    @SerializedName("image")
    private String image;
    @SerializedName("type")
    private String type;
    @SerializedName("type_id")
    private String type_id;
    @SerializedName("created_date")
    private String created_date;
    @SerializedName("updated_date")
    private String updated_date;
    @SerializedName("status")
    private String status;

    public String getPet_image_id() {
        return pet_image_id;
    }

    public void setPet_image_id(String pet_image_id) {
        this.pet_image_id = pet_image_id;
    }

    public String getPet_image() {
        return pet_image;
    }

    public void setPet_image(String pet_image) {
        this.pet_image = pet_image;
    }

    public String getHostel_sitter_vet_images_id() {
        return hostel_sitter_vet_images_id;
    }

    public void setHostel_sitter_vet_images_id(String hostel_sitter_vet_images_id) {
        this.hostel_sitter_vet_images_id = hostel_sitter_vet_images_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PetImageInfo{" +
                "pet_image_id='" + pet_image_id + '\'' +
                ", pet_image='" + pet_image + '\'' +
                ", hostel_sitter_vet_images_id='" + hostel_sitter_vet_images_id + '\'' +
                ", image='" + image + '\'' +
                ", type='" + type + '\'' +
                ", type_id='" + type_id + '\'' +
                ", created_date='" + created_date + '\'' +
                ", updated_date='" + updated_date + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
