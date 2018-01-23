package com.pets.app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 23/01/18.
 */

public class ProductResponse extends NormalResponse implements Serializable {

    @SerializedName("list")
    ArrayList<Product> list;

    public ArrayList<Product> getList() {
        return list;
    }
}
