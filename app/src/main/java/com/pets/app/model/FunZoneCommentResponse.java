package com.pets.app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 18/01/18.
 */

public class FunZoneCommentResponse extends NormalResponse implements Serializable {

    @SerializedName("list")
    private ArrayList<FunZoneComment> list;

    @SerializedName("result")
    private FunZoneComment result;

    public ArrayList<FunZoneComment> getList() {
        return list;
    }

    public FunZoneComment getResult() {
        return result;
    }
}
