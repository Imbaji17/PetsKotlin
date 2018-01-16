package com.pets.app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 12/01/18.
 */

public class PetsType implements Serializable {

    @SerializedName("pets_type_id")
    private String petsTypeId;
    @SerializedName("type_name")
    private String typeName;
    @SerializedName("created_date")
    private String createdDate;
    @SerializedName("updated_date")
    private String updatedDate;
    @SerializedName("status")
    private String status;

    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getPetsTypeId() {
        return petsTypeId;
    }

    public String getTypeName() {
        return typeName;
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

    @Override
    public String toString() {
        return "PetsType{" +
                "petsTypeId='" + petsTypeId + '\'' +
                ", typeName='" + typeName + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", updatedDate='" + updatedDate + '\'' +
                ", status='" + status + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }
}
