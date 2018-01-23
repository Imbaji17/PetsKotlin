package com.pets.app.model.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 23/01/18.
 */

public class FunZoneAddComment implements Serializable {

    @SerializedName("user_id")
    private String userId;

    @SerializedName("key")
    private String key;

    @SerializedName("timestamp")
    private String timestamp;

    @SerializedName("fun_zone_id")
    private String funZoneId;

    @SerializedName("fun_zone_comment_id")
    private String funZoneCommentId;

    @SerializedName("comment")
    private String comment;

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

    public void setFunZoneCommentId(String funZoneCommentId) {
        this.funZoneCommentId = funZoneCommentId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
