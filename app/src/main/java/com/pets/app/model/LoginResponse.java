package com.pets.app.model;

import com.google.gson.annotations.SerializedName;
import com.pets.app.model.object.LoginDetails;

import java.io.Serializable;

/**
 * Created by BAJIRAO on 10 January 2018.
 */
public class LoginResponse extends NormalResponse implements Serializable {

    @SerializedName("result")
    private LoginDetails result;

    public LoginDetails getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "result=" + result +
                '}';
    }
}
