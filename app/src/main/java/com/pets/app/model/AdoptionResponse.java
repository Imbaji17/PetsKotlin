package com.pets.app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 12/01/18.
 */

public class AdoptionResponse implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("next_offset")
    private int nextOffset;

    @SerializedName("list")
    private ArrayList<Adoption> list;

    @SerializedName("result")
    private Adoption result;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public int getNextOffset() {
        return nextOffset;
    }

    public ArrayList<Adoption> getList() {
        return list;
    }
}
