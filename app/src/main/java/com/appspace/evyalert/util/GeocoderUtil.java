package com.appspace.evyalert.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.appspace.appspacelibrary.util.LoggerUtils;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.crash.FirebaseCrash;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by siwaweswongcharoen on 8/11/2016 AD.
 */
public class GeocoderUtil {
    public static String getAddress(Context context, double lat, double lng) {
        String addressText = "";
        Geocoder coder = new Geocoder(context);
        try {
            ArrayList<Address> adresses = (ArrayList<Address>) coder.getFromLocation(lat, lng, 1);
            for (Address address : adresses) {
                for (int i = 0; address.getAddressLine(i) != null; i++) {
                    addressText += address.getAddressLine(i) + ",";
                }

//                LoggerUtils.log2I("address", "getThoroughfare(): " + address.getThoroughfare());
//                LoggerUtils.log2I("address", "getSubThoroughfare(): " + address.getSubThoroughfare());
//                LoggerUtils.log2I("address", "getAdminArea(): " + address.getAdminArea());
//                LoggerUtils.log2I("address", "getSubAdminArea(): " + address.getSubAdminArea());
//                LoggerUtils.log2I("address", "getLocality(): " + address.getLocality());
//                LoggerUtils.log2I("address", "getSubAdminArea(): " + address.getSubAdminArea());
//                LoggerUtils.log2I("address", "getPremises(): " + address.getPremises());
//                LoggerUtils.log2I("address", "getPostalCode(): " + address.getPostalCode());
//                LoggerUtils.log2I("address", "getCountryName(): " + address.getCountryName());
            }
        } catch (IOException | NullPointerException e) {
            FirebaseCrash.report(e);
            Crashlytics.logException(e);
        }
        return addressText;
    }

    public static String getDistrict(Context context, double lat, double lng) {
        String addressText = "";
        Geocoder coder = new Geocoder(context);
        try {
            ArrayList<Address> adresses = (ArrayList<Address>) coder.getFromLocation(lat, lng, 1);
            for (Address address : adresses) {
                if (address.getLocality() != null) {
                    addressText += address.getLocality();
                    LoggerUtils.log2I("address", "getLocality(): " + address.getLocality());
                }

//                LoggerUtils.log2I("address", "getThoroughfare(): " + address.getThoroughfare());
//                LoggerUtils.log2I("address", "getSubThoroughfare(): " + address.getSubThoroughfare());
//                LoggerUtils.log2I("address", "getAdminArea(): " + address.getAdminArea());
//                LoggerUtils.log2I("address", "getSubAdminArea(): " + address.getSubAdminArea());
//                LoggerUtils.log2I("address", "getSubAdminArea(): " + address.getSubAdminArea());
//                LoggerUtils.log2I("address", "getPremises(): " + address.getPremises());
//                LoggerUtils.log2I("address", "getPostalCode(): " + address.getPostalCode());
//                LoggerUtils.log2I("address", "getCountryName(): " + address.getCountryName());
            }
        } catch (IOException | NullPointerException e) {
            FirebaseCrash.report(e);
            Crashlytics.logException(e);
        }
        return addressText;
    }

    public static String getProvince(Context context, double lat, double lng) {
        String addressText = "";
        Geocoder coder = new Geocoder(context);
        try {
            ArrayList<Address> adresses = (ArrayList<Address>) coder.getFromLocation(lat, lng, 1);
            for (Address address : adresses) {
                if (address.getLocality() != null) {
                    addressText += address.getAdminArea();
                    LoggerUtils.log2I("address", "getAdminArea(): " + address.getAdminArea());
                }

//                LoggerUtils.log2I("address", "getThoroughfare(): " + address.getThoroughfare());
//                LoggerUtils.log2I("address", "getSubThoroughfare(): " + address.getSubThoroughfare());
//                LoggerUtils.log2I("address", "getAdminArea(): " + address.getAdminArea());
//                LoggerUtils.log2I("address", "getSubAdminArea(): " + address.getSubAdminArea());
//                LoggerUtils.log2I("address", "getSubAdminArea(): " + address.getSubAdminArea());
//                LoggerUtils.log2I("address", "getPremises(): " + address.getPremises());
//                LoggerUtils.log2I("address", "getPostalCode(): " + address.getPostalCode());
//                LoggerUtils.log2I("address", "getCountryName(): " + address.getCountryName());
            }
        } catch (IOException | NullPointerException e) {
            FirebaseCrash.report(e);
            Crashlytics.logException(e);
        }
        return addressText;
    }
}
