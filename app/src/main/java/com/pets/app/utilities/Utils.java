package com.pets.app.utilities;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.pets.app.R;
import com.pets.app.common.AppPreferenceManager;
import com.pets.app.common.MyApplication;
import com.pets.app.model.object.Country;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static void showToast(String text) {
        Toast.makeText(MyApplication.getInstance(), text, Toast.LENGTH_SHORT).show();
    }

    public static void showToastLong(String text) {
        Toast.makeText(MyApplication.getInstance(), text, Toast.LENGTH_LONG).show();
    }

    public static boolean isOnline(Context context) {
        boolean result = false;
        if (context != null) {
            final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null) {
                    result = networkInfo.isConnected();
                }
            }
        }
        return result;
    }

    public static boolean isNetworkAvailable(Context context) {
        boolean value = false;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            value = true;
        }
        return value;
    }

    public static void requestFocus(View view, Activity mActivity) {
        if (view.requestFocus()) {
            mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public static boolean isEmailValid(String email) {
        String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@" + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|" + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
    }

    public static boolean isEmailValidDefault(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean getConnectionStatus(Context context) {

        ConnectivityManager mConnectivityManager;
        NetworkInfo mNetworkInfoMobile, mNetworkInfoWifi;

        mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        mNetworkInfoMobile = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        mNetworkInfoWifi = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        try {
            if (mNetworkInfoMobile.isConnected()) {
                return true;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return mNetworkInfoWifi.isConnected();
    }

    public static void hideKeyboardFromView(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyboard(Activity activity) {

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

    public static <T> T getResponse(String result, Class<?> cls) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        T response = (T) gson.fromJson(result, cls);
        return response;
    }

    public static String responseToString(ArrayList<String> friendLists) {

        Type listType = new TypeToken<List<String>>() {
        }.getType();
        String list = null;
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            list = gson.toJson(friendLists, listType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<String> stringToResponse(String jsonString) {
        Type listType = new TypeToken<List<String>>() {
        }.getType();

        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            ArrayList<String> response = gson.fromJson(jsonString, listType);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getDeviceWidth(Activity mActivity) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        return width;
    }

    public static int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.ic_launcher : R.mipmap.ic_launcher;
    }

    public static String getDistance(Activity mActivity, String latitude, String longitude) {

        double distance;
        Location locationA = new Location("My Location");
        locationA.setLatitude(Double.parseDouble(AppPreferenceManager.getUser().getLat()));
        locationA.setLongitude(Double.parseDouble(AppPreferenceManager.getUser().getLng()));
        Location locationB = new Location("Club Location");
        locationB.setLatitude(Double.parseDouble(latitude));
        locationB.setLongitude(Double.parseDouble(longitude));

        distance = locationA.distanceTo(locationB) / 1000;

        Locale locale = new Locale("en", "UK");
        String pattern = "#.##";
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(locale);
        decimalFormat.applyPattern(pattern);

        DecimalFormatSymbols local = DecimalFormatSymbols.getInstance(Locale.ENGLISH);

        return "" + new DecimalFormat("#.#", local).format(distance);
    }

    public static ArrayList<Country> getCountryList(Activity mActivity) {
        InputStream is = mActivity.getResources().openRawResource(R.raw.country_code);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String countriesJsonString = writer.toString();
        Type listType = new TypeToken<ArrayList<Country>>() {
        }.getType();
        return new Gson().fromJson(countriesJsonString, listType);
    }
}
