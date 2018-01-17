package com.pets.app.model;

import com.google.gson.annotations.SerializedName;
import com.pets.app.model.object.PetDetails;

import java.io.Serializable;

/**
 * Created by admin on 12/01/18.
 */
public class PetResponse extends NormalResponse implements Serializable {

    @SerializedName("result")
    private PetDetails result;

    public PetDetails getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "PetResponse{" +
                "result=" + result +
                '}';
    }
}
