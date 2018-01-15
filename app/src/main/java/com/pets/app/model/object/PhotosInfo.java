package com.pets.app.model.object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by BAJIRAO on 10 November 2017.
 */
public class PhotosInfo implements Serializable {

    @SerializedName("url")
    private String url;

    @SerializedName("order")
    private String order;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "PhotosInfo{" +
                "url=" + url +
                ", order='" + order + '\'' +
                '}';
    }
}
