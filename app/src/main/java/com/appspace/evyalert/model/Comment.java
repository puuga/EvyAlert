package com.appspace.evyalert.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by siwaweswongcharoen on 9/5/2016 AD.
 */
public class Comment implements Parcelable{
    @SerializedName("id") public int id;
    @SerializedName("event_id") public int eventId;
    @SerializedName("comment") public String comment;
    @SerializedName("user_uid") public String userUid;
    @SerializedName("user_name") public String userName;
    @SerializedName("user_photo_url") public String userPhotoUrl;
    @SerializedName("created_at_long") public long createdAtLong;
    @SerializedName("created_at") public String createdAt;
    @SerializedName("updated_at") public String updatedAt;

    public Comment() {

    }

    protected Comment(Parcel in) {
        id = in.readInt();
        eventId = in.readInt();
        comment = in.readString();
        userUid = in.readString();
        userName = in.readString();
        userPhotoUrl = in.readString();
        createdAtLong = in.readLong();
        createdAt = in.readString();
        updatedAt = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(eventId);
        dest.writeString(comment);
        dest.writeString(userUid);
        dest.writeString(userName);
        dest.writeString(userPhotoUrl);
        dest.writeLong(createdAtLong);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}
