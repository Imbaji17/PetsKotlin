package com.pets.app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 16/01/18.
 */

public class ImageResponse implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("result")
    private HostelImage result;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public HostelImage getResult() {
        return result;
    }
}
