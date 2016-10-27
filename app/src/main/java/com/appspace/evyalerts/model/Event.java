package com.appspace.evyalerts.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by siwaweswongcharoen on 8/10/2016 AD.
 */
public class Event implements Parcelable {
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
    @SerializedName("number_of_comments") public int numberOfComments;
    @SerializedName("created_at_long") public long createdAtLong;
    @SerializedName("created_at") public String createdAt;
    @SerializedName("updated_at") public String updatedAt;

    public Event() {

    }

    protected Event(Parcel in) {
        eventUid = in.readString();
        userUid = in.readString();
        userName = in.readString();
        userPhotoUrl = in.readString();
        title = in.readString();
        eventPhotoUrl = in.readString();
        eventTypeIndex = in.readString();
        provinceIndex = in.readString();
        regionIndex = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
        address = in.readString();
        numberOfComments = in.readInt();
        createdAtLong = in.readLong();
        createdAt = in.readString();
        updatedAt = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(eventUid);
        dest.writeString(userUid);
        dest.writeString(userName);
        dest.writeString(userPhotoUrl);
        dest.writeString(title);
        dest.writeString(eventPhotoUrl);
        dest.writeString(eventTypeIndex);
        dest.writeString(provinceIndex);
        dest.writeString(regionIndex);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeString(address);
        dest.writeInt(numberOfComments);
        dest.writeLong(createdAtLong);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}
