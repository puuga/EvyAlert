package com.appspace.evyalerts.util;

import com.google.firebase.crash.FirebaseCrash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by siwaweswongcharoen on 8/10/2016 AD.
 */
public class TimeUtil {
    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        return sdfDate.format(now);
    }

    public static long getCurrentTime() {
        return new Date().getTime();
    }

    public static long getLast2DaysTime() {
        return new Date().getTime() - 17280000;
    }

    public static String timpStampFormater(long time) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);
        return sdfDate.format(date);
    }

    public static String getHashStringFromNow() {
        MessageDigest instance = null;
        byte[] messageDigest = new byte[0];
        try {
            instance = MessageDigest.getInstance("MD5");
            messageDigest = instance.digest(String.valueOf(System.nanoTime()).getBytes());
        } catch (NoSuchAlgorithmException | NullPointerException e) {
            e.printStackTrace();
            FirebaseCrash.report(e);
        }
        StringBuilder hexString = new StringBuilder();
        for (byte aMessageDigest : messageDigest) {
            String hex = Integer.toHexString(0xFF & aMessageDigest);
            if (hex.length() == 1) {
                // could use a for loop, but we're only dealing with a single
                // byte
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
