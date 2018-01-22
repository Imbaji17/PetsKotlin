package com.pets.app.model.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 22/01/18.
 */

public class FunZoneLike implements Serializable {

    @SerializedName("user_id")
    private String userId;

    @SerializedName("key")
    private String key;

    @SerializedName("timestamp")
    private String timestamp;

    @SerializedName("fun_zone_id")
    private String funZoneId;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setFunZoneId(String funZoneId) {
        this.funZoneId = funZoneId;
    }
}
