package com.pets.app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 11/01/18.
 */

public class ReviewsResponse implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("next_offset")
    private int next_offset;

    @SerializedName("list")
    private ArrayList<Reviews> list;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public int getNext_offset() {
        return next_offset;
    }

    public ArrayList<Reviews> getList() {
        return list;
    }
}
