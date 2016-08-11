package com.appspace.evyalert.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by siwaweswongcharoen on 8/10/2016 AD.
 */
@IgnoreExtraProperties
public class Event {
    @SerializedName("user_uid") public String userUid;
    @SerializedName("user_name") public String userName;
    @SerializedName("user_photo_url") public String userPhotoUrl;
    @SerializedName("title") public String title;
    @SerializedName("event_photo_url") public String eventPhotoUrl;
    @SerializedName("event_type_index") public String eventTypeIndex;
    @SerializedName("province_index") public String provinceIndex;
    @SerializedName("region_index") public String regionIndex;
    @SerializedName("lat") public double lat;
    @SerializedName("lng") public double lng;
    @SerializedName("address") public String address;
    @SerializedName("created_at") public String createdAt;
    @SerializedName("created_at_long") public long createdAtLong;

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
