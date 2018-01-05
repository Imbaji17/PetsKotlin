package com.pets.app.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebView;

import com.pets.app.utilities.Typefaces;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by BAJIRAO on 05 January 2018.
 */
public class MyApplication extends MultiDexApplication {

    private static final String TAG = MyApplication.class.getSimpleName();
    private static MyApplication mInstance;
    private static SharedPreferences prefs;
    public static Typeface fontRegular;
    public static Typeface fontItalic;
    public static Typeface fontBold;
    public static Typeface fontBoldItalic;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        new WebView(this).destroy();
        ApplicationLifecycleHandler handler = new ApplicationLifecycleHandler(getApplicationContext());
        registerActivityLifecycleCallbacks(handler);
        registerComponentCallbacks(handler);
        preloadTypefaces();
        printHashKey();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public static SharedPreferences getSharedPreferences() {
        if (prefs == null) {
            prefs = getInstance().getSharedPreferences(Constants.PREFERENCE_FILE, MODE_PRIVATE);
        }
        return prefs;
    }

    /**
     * Preload Typefaces.
     */
    private void preloadTypefaces() {
        Log.d(TAG, "preloadTypefaces");
        fontRegular = Typefaces.get(getApplicationContext(), Typefaces.FONT_REGULAR);
        fontItalic = Typefaces.get(getApplicationContext(), Typefaces.FONT_ITALIC);
        fontBold = Typefaces.get(getApplicationContext(), Typefaces.FONT_BOLD);
        fontBoldItalic = Typefaces.get(getApplicationContext(), Typefaces.FONT_BOLD_ITALIC);
    }

    private void printHashKey() {
        Log.e("", "Inside Print Hash Key");
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("TEMPHASH KEY:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

                Log.e("", "hash key=" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
