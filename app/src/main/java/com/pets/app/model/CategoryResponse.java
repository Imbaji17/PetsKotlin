package com.pets.app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 23/01/18.
 */

public class CategoryResponse extends NormalResponse implements Serializable {

    @SerializedName("list")
    ArrayList<Category> list;

    public ArrayList<Category> getList() {
        return list;
    }
}
