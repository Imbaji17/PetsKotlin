package com.pets.app.utilities;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.util.Hashtable;

/**
 * Created by BAJIRAO on 05 January 2018.
 */
public class Typefaces {

    private static final String TAG = Typefaces.class.getSimpleName();
    public static final String FONT_REGULAR = "fonts/calibri_regular.ttf";
    public static final String FONT_ITALIC = "fonts/calibri_italic.ttf";
    public static final String FONT_BOLD = "fonts/calibri_bold.ttf";
    public static final String FONT_BOLD_ITALIC = "fonts/calibri_bolditalic.ttf";

    private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

    public static Typeface get(Context c, String assetPath) {
        synchronized (cache) {
            if (!cache.containsKey(assetPath)) {
                try {
                    Typeface t = Typeface.createFromAsset(c.getAssets(),
                            assetPath);
                    cache.put(assetPath, t);
                } catch (Exception e) {
                    Log.e(TAG, "Could not get typeface '" + assetPath
                            + "' because " + e.getMessage());
                    return null;
                }
            }
            return cache.get(assetPath);
        }
    }
}
