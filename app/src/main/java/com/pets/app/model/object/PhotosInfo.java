package com.pets.app.model.object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by BAJIRAO on 10 November 2017.
 */
public class PhotosInfo implements Serializable {

    @SerializedName("url")
    private String url;

    @SerializedName("image_id")
    private String imageId;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    @Override
    public String toString() {
        return "PhotosInfo{" +
                "url='" + url + '\'' +
                ", imageId='" + imageId + '\'' +
                '}';
    }
}
