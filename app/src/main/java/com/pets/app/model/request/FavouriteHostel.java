package com.pets.app.model.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 11/01/18.
 */

public class FavouriteHostel implements Serializable {

    @SerializedName("user_id")
    private String userId;

    @SerializedName("timestamp")
    private String timestamp;

    @SerializedName("type")
    private String type;

    @SerializedName("type_id")
    private String typeId;

    @SerializedName("key")
    private String key;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
