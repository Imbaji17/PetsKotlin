package com.pets.app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by BAJIRAO on 05 January 2018.
 */
public class NormalResponse implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("next_offset")
    private int nextOffset;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public int getNextOffset() {
        return nextOffset;
    }

    @Override
    public String toString() {
        return "NormalResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", nextOffset='" + nextOffset + '\'' +
                '}';
    }
}
