package com.pets.app.model;

import com.google.gson.annotations.SerializedName;
import com.pets.app.model.object.PetSitterDetails;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by BAJIRAO on 29 January 2018.
 */
public class PetSitterResponse extends NormalResponse implements Serializable {

    @SerializedName("pet_status")
    private String pet_status;

    @SerializedName("result")
    private PetSitterDetails result;

    @SerializedName("list")
    private ArrayList<PetSitterDetails> list;

    public PetSitterDetails getResult() {
        return result;
    }

    public void setResult(PetSitterDetails result) {
        this.result = result;
    }

    public ArrayList<PetSitterDetails> getList() {
        return list;
    }

    public void setList(ArrayList<PetSitterDetails> list) {
        this.list = list;
    }

    public String getPet_status() {
        return pet_status;
    }

    public void setPet_status(String pet_status) {
        this.pet_status = pet_status;
    }

    @Override
    public String toString() {
        return "PetSitterResponse{" +
                "pet_status='" + pet_status + '\'' +
                ", result=" + result +
                ", list=" + list +
                '}';
    }
}
