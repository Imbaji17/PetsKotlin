package com.pets.app.model.object;

/**
 * Created by BAJIRAO on 15 January 2018.
 */
public class LandingDetails {

    private String name;
    private int image;

    public LandingDetails(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "LandingDetails{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
