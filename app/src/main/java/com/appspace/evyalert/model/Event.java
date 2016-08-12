package com.appspace.evyalert.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by siwaweswongcharoen on 8/10/2016 AD.
 */
public class Event {
    @SerializedName("id") public String eventUid;
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
    @SerializedName("created_at_long") public long createdAtLong;
    @SerializedName("created_at") public String createdAt;
    @SerializedName("updated_at") public String updatedAt;

    public Event() {

    }
}
