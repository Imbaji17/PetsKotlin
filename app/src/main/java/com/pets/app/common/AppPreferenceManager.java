package com.pets.app.common;

import android.content.Context;
import android.content.SharedPreferences;

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

//    public static void saveUser(UserInfoDetails user) {
//        Gson gson = new Gson();
//        String json = gson.toJson(user);
//        getPreferences().edit().putString(PrefConstants.USER_OBJECT, json).apply();
//    }
//
//    public static UserInfoDetails getUser() {
//        Gson gson = new Gson();
//        String json = getPreferences().getString(PrefConstants.USER_OBJECT, "");
//        return gson.fromJson(json, UserInfoDetails.class);
//    }

//    public static String getUserID() {
//        return getUser().getUser_id();
//    }

//    public static void setSkipCompany(boolean value) {
//        getPreferences().edit().putBoolean(PrefConstants.SKIP_COMPANY, value).apply();
//    }
//
//    public static boolean isSkipCompany() {
//        return getPreferences().getBoolean(PrefConstants.SKIP_COMPANY, false);
//    }

//    public static void setSkipCandidate(boolean value) {
//        getPreferences().edit().putBoolean(PrefConstants.SKIP_CANDIDATE, value).apply();
//    }

//    public static void saveCandidateUsername(String value) {
//        getPreferences().edit().putString(PrefConstants.CANDIDATE_MOBILE, value).apply();
//    }

//    public static String getCandidateUsername() {
//        return getPreferences().getString(PrefConstants.CANDIDATE_MOBILE, "");
//    }

}
