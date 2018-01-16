package com.pets.app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 12/01/18.
 */

public class Breed implements Serializable {

    @SerializedName("breed_id")
    private String breed_id;

    @SerializedName("pet_type_id")
    private String petTypeId;

    @SerializedName("breed_name")
    private String breed_name;

    @SerializedName("created_date")
    private String created_date;

    @SerializedName("updated_date")
    private String updated_date;

    @SerializedName("status")
    private String status;

    private boolean isSelected;

    public String getBreed_id() {
        return breed_id;
    }

    public String getPetTypeId() {
        return petTypeId;
    }

    public String getBreed_name() {
        return breed_name;
    }

    public String getCreated_date() {
        return created_date;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public String getStatus() {
        return status;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "Breed{" +
                "breed_id='" + breed_id + '\'' +
                ", petTypeId='" + petTypeId + '\'' +
                ", breed_name='" + breed_name + '\'' +
                ", created_date='" + created_date + '\'' +
                ", updated_date='" + updated_date + '\'' +
                ", status='" + status + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }
}
