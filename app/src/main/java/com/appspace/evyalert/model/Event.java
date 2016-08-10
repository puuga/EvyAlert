package com.appspace.evyalert.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by siwaweswongcharoen on 8/10/2016 AD.
 */
@IgnoreExtraProperties
public class Event {
    public String userUid;
    public String userName;
    public String userPhotoUrl;
    public String title;
    public String eventPhotoUrl;
    public String eventTypeIndex;
    public String provinceIndex;
    public String regionIndex;
    public double lat;
    public double lng;
    public String address;
    public String createdAt;
    public long createdAtLong;

    public Event() {

    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userUid", userUid);
        result.put("userName", userName);
        result.put("userPhotoUrl", userPhotoUrl);
        result.put("title", title);
        result.put("eventPhotoUrl", eventPhotoUrl);
        result.put("eventTypeIndex", eventTypeIndex);
        result.put("provinceIndex", provinceIndex);
        result.put("regionIndex", regionIndex);
        result.put("lat", lat);
        result.put("lng", lng);
        result.put("address", address);
        result.put("createdAt", createdAt);
        result.put("createdAtLong", createdAtLong);

        return result;
    }
}
