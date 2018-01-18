package com.pets.app.model;

import com.google.gson.annotations.SerializedName;
import com.pets.app.model.object.PetDetails;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 12/01/18.
 */
public class PetResponse extends NormalResponse implements Serializable {

    @SerializedName("result")
    private PetDetails result;

    @SerializedName("list")
    private ArrayList<PetDetails> list;

    public PetDetails getResult() {
        return result;
    }

    public ArrayList<PetDetails> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "PetResponse{" +
                "result=" + result +
                ", list=" + list +
                '}';
    }
}
