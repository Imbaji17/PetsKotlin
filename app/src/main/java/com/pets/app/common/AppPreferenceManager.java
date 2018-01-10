package com.pets.app.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.pets.app.model.object.LoginDetails;

/**
 * Created by BAJIRAO on 05 January 2018.
 */
public class AppPreferenceManager {

    private final static String PREF_FILE = "PETS";
    private static SharedPreferences appPrefs;

    private static Context context = MyApplication.getInstance();

    private static SharedPreferences getPreferences() {
        if (appPrefs != null)
            return appPrefs;
        else
            return appPrefs = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
    }

    public static void saveUser(LoginDetails user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        getPreferences().edit().putString(PrefConstants.USER_OBJECT, json).apply();
    }

    public static LoginDetails getUser() {
        Gson gson = new Gson();
        String json = getPreferences().getString(PrefConstants.USER_OBJECT, "");
        return gson.fromJson(json, LoginDetails.class);
    }

    public static String getUserID() {
        return getUser().getUser_id();
    }

    public static boolean isRemember() {
        return getPreferences().getBoolean(PrefConstants.LOGIN_REMEMBER, false);
    }

    public static void setRemember(boolean value) {
        getPreferences().edit().putBoolean(PrefConstants.LOGIN_REMEMBER, value).apply();
    }

    public static void saveUserEmail(String value) {
        getPreferences().edit().putString(PrefConstants.USER_EMAIL, value).apply();
    }

    public static String getUserEmail() {
        return getPreferences().getString(PrefConstants.USER_EMAIL, "");
    }

    public static void saveUserPassword(String value) {
        getPreferences().edit().putString(PrefConstants.USER_PASSWORD, value).apply();
    }

    public static String getUserPassword() {
        return getPreferences().getString(PrefConstants.USER_PASSWORD, "");
    }
}
