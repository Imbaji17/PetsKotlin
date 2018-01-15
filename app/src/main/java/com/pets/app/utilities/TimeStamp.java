package com.pets.app.utilities;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TimeStamp {

    public static String getTimeStamp() {
        long currentTimestamp = System.currentTimeMillis();
        return Long.toString(currentTimestamp);
    }

    public static String getMd5(String cmgString) {

        try {
            // Create MD5 Hash
            byte[] bytesOfMessage = null;
            try {
                bytesOfMessage = cmgString.getBytes("UTF-8");
            } catch (Exception e) {
                bytesOfMessage = cmgString.getBytes();
            }
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(bytesOfMessage);
            byte messageDigest[] = digest.digest();

            // Create Hex String

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String hexValue = Integer.toHexString(0xFF & messageDigest[i]);
                if (hexValue.length() == 1) {
                    hexValue = "0" + hexValue;
                }
                hexString.append(hexValue);
            }
            Log.v("", "MD5 for '" + cmgString + "'  is: " + hexString);
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}