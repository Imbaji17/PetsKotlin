package com.pets.app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 15/01/18.
 */

public class BreedResponse implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("list")
    private ArrayList<Breed> list;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<Breed> getList() {
        return list;
    }
}
