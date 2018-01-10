package com.pets.app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 09/01/18.
 */
public class FindHostelResponse extends NormalResponse implements Serializable {

    @SerializedName("list")
    private ArrayList<FindHostel> list;

    public ArrayList<FindHostel> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "FindHostelResponse{" +
                "list=" + list +
                '}';
    }
}
