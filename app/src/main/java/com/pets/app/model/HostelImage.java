package com.pets.app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 09/01/18.
 */

public class HostelImage implements Serializable {

    @SerializedName("hostel_sitter_vet_images_id")
    private String id;

    @SerializedName("image")
    private String image;

    @SerializedName("type")
    private String type;

    @SerializedName("type_id")
    private String typeId;

    @SerializedName("created_date")
    private String createdDate;

    @SerializedName("updated_date")
    private String updatedDate;

    @SerializedName("status")
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
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
}
