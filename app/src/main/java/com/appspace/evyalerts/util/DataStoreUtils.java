package com.appspace.evyalerts.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.appspace.appspacelibrary.manager.Contextor;
import com.appspace.appspacelibrary.util.LoggerUtils;
import com.google.gson.Gson;

/**
 * Created by siwaweswongcharoen on 8/10/2016 AD.
 */
public class DataStoreUtils {
    private static DataStoreUtils instance;

    public static DataStoreUtils getInstance() {
        if (instance == null)
            instance = new DataStoreUtils();
        return instance;
    }

    private Context mContext;

    private DataStoreUtils() {
        mContext = Contextor.getInstance().getContext();

        sharedPref = mContext.getSharedPreferences(SETTING_FILE, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        gson = new Gson();
    }

    private static final String tag = "DataStoreUtils";

    private Gson gson;

    // key
    private static final String SETTING_FILE = "SETTING_FILE";
    private static final String KEY_IS_LOGIN = "KEY_IS_LOGIN";
    private static final String KEY_FACEBOOK_ID = "KEY_FACEBOOK_ID";
    private static final String KEY_FACEBOOK_NAME = "KEY_FACEBOOK_NAME";
    private static final String KEY_FACEBOOK_TOKEN = "KEY_FACEBOOK_TOKEN";
    private static final String KEY_APP_USER_ID = "KEY_APP_USER_ID";
    private static final String KEY_APP_FIRST_RUN = "KEY_APP_FIRST_RUN";

    private static final String KEY_SWITCH_ACCIDENT = "KEY_SWITCH_ACCIDENT";
    private static final String KEY_SWITCH_NATURAL_DISASTER = "KEY_SWITCH_NATURAL_DISASTER";
    private static final String KEY_SWITCH_OTHER = "KEY_SWITCH_OTHER";
    private static final String KEY_SWITCH_TRAFFIC_JAM = "KEY_SWITCH_TRAFFIC_JAM";

    // SharedPreferences
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    public boolean isLogin() {
        return sharedPref.getBoolean(KEY_IS_LOGIN, false);
    }

    public void setLogin(boolean val) {
        editor.putBoolean(KEY_IS_LOGIN, val);
        editor.commit();
        LoggerUtils.getInstance().logI(tag, "KEY_IS_LOGIN: " + val);
    }

    public String getFacebookId() {
        return sharedPref.getString(KEY_FACEBOOK_ID, "");
    }

    public void setFacebookId(String val) {
        editor.putString(KEY_FACEBOOK_ID, val);
        editor.commit();
        LoggerUtils.getInstance().logI(tag, "KEY_FACEBOOK_ID: " + val);
    }

    public String getFacebookName() {
        return sharedPref.getString(KEY_FACEBOOK_NAME, "");
    }

    public void setFacebookName(String val) {
        editor.putString(KEY_FACEBOOK_NAME, val);
        editor.commit();
        LoggerUtils.getInstance().logI(tag, "KEY_FACEBOOK_NAME: " + val);
    }

    public String getFacebookToken() {
        return sharedPref.getString(KEY_FACEBOOK_TOKEN, "");
    }

    public void setFacebookToken(String val) {
        editor.putString(KEY_FACEBOOK_TOKEN, val);
        editor.commit();
        LoggerUtils.getInstance().logI(tag, "KEY_FACEBOOK_TOKEN: " + val);
    }

    public String getAppUserId() {
        return sharedPref.getString(KEY_APP_USER_ID, "");
    }

    public void setAppUserId(String val) {
        editor.putString(KEY_APP_USER_ID, val);
        editor.commit();
        LoggerUtils.getInstance().logI(tag, "KEY_APP_USER_ID: " + val);
    }

    public boolean isFirstRun() {
        return sharedPref.getBoolean(KEY_APP_FIRST_RUN, true);
    }

    public void setFirstRun(boolean val) {
        editor.putBoolean(KEY_APP_FIRST_RUN, val);
        editor.commit();
        LoggerUtils.getInstance().logI(tag, "KEY_APP_FIRST_RUN: " + val);
    }

    public boolean isAccidentSwitchOn() {
        return sharedPref.getBoolean(KEY_SWITCH_ACCIDENT, true);
    }

    public void setAccidentSwitch(boolean val) {
        editor.putBoolean(KEY_SWITCH_ACCIDENT, val);
        editor.commit();
        LoggerUtils.getInstance().logI(tag, "KEY_SWITCH_ACCIDENT: " + val);
    }

    public boolean isNaturalDisasterSwitchOn() {
        return sharedPref.getBoolean(KEY_SWITCH_NATURAL_DISASTER, true);
    }

    public void setNaturalDisasterSwitch(boolean val) {
        editor.putBoolean(KEY_SWITCH_NATURAL_DISASTER, val);
        editor.commit();
        LoggerUtils.getInstance().logI(tag, "KEY_SWITCH_NATURAL_DISASTER: " + val);
    }

    public boolean isOtherSwitchOn() {
        return sharedPref.getBoolean(KEY_SWITCH_OTHER, true);
    }

    public void setOtherSwitch(boolean val) {
        editor.putBoolean(KEY_SWITCH_OTHER, val);
        editor.commit();
        LoggerUtils.getInstance().logI(tag, "KEY_SWITCH_OTHER: " + val);
    }

    public boolean isTrafficJamSwitchOn() {
        return sharedPref.getBoolean(KEY_SWITCH_TRAFFIC_JAM, true);
    }

    public void setTrafficJamSwitch(boolean val) {
        editor.putBoolean(KEY_SWITCH_TRAFFIC_JAM, val);
        editor.commit();
        LoggerUtils.getInstance().logI(tag, "KEY_SWITCH_TRAFFIC_JAM: " + val);
    }
}
