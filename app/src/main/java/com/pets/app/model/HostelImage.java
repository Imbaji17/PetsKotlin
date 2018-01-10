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

    public String getImage() {
        return image;
    }

    public String getType() {
        return type;
    }

    public String getTypeId() {
        return typeId;
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
}
