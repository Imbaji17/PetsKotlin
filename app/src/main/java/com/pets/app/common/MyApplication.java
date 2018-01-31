package com.pets.app.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebView;

import com.facebook.FacebookSdk;
import com.pets.app.utilities.Typefaces;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by BAJIRAO on 05 January 2018.
 */
public class MyApplication extends MultiDexApplication {

    private static final String TAG = MyApplication.class.getSimpleName();
    public static Typeface fontRegular;
    public static Typeface fontItalic;
    public static Typeface fontBold;
    public static Typeface fontLight;
    private static MyApplication mInstance;
    private static SharedPreferences prefs;

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        new WebView(this).destroy();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        ApplicationLifecycleHandler handler = new ApplicationLifecycleHandler(getApplicationContext());
        registerActivityLifecycleCallbacks(handler);
        registerComponentCallbacks(handler);
        preloadTypefaces();
        printHashKey();
        // Facebook Sdk Initiazile
        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    /**
     * Preload Typefaces.
     */
    private void preloadTypefaces() {
        Log.d(TAG, "preloadTypefaces");
        fontRegular = Typefaces.get(getApplicationContext(), Typefaces.FONT_REGULAR);
        fontItalic = Typefaces.get(getApplicationContext(), Typefaces.FONT_ITALIC);
        fontBold = Typefaces.get(getApplicationContext(), Typefaces.FONT_BOLD);
        fontLight = Typefaces.get(getApplicationContext(), Typefaces.FONT_LIGHT);
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
