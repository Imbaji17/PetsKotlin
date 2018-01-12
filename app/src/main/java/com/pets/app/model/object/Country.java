package com.pets.app.model.object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by BAJIRAO on 24-Mar-17.
 */
public class Country implements Serializable {

    private String tbl_countries_id;
    private String country_name;
    private String country_code;
    private String nationality;
    private String Person;

    @SerializedName("Country Name")
    private String countryName;

    @SerializedName("Country Code")
    private String countryCode;

    @SerializedName("Phone Code")
    private String phoneCode;

    public Country() {
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public String toString() {
        return countryName;
    }
}
