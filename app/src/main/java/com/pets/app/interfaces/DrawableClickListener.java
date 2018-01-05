package com.pets.app.interfaces;

/**
 * Created by BAJIRAO on 05 January 2018.
 */
public interface DrawableClickListener {

    enum DrawablePosition {TOP, BOTTOM, LEFT, RIGHT}

    void onClick(DrawablePosition target);
}
