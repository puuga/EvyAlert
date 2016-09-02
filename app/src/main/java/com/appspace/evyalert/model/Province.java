package com.appspace.evyalert.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by siwaweswongcharoen on 9/2/2016 AD.
 */
public class Province implements Parcelable {
    @SerializedName("id") public int provinceId;
    @SerializedName("name_th") public String nameTh;
    @SerializedName("name_en") public String nameEn;
    @SerializedName("province_index") public int provinceIndex;
    @SerializedName("region_code") public int regionCode;
    @SerializedName("province_code") public int provinceCode;
    @SerializedName("bound_lat_min") public double boundLatMin;
    @SerializedName("bound_lat_max") public double boundLatMax;
    @SerializedName("bound_lng_min") public double boundLngMin;
    @SerializedName("bound_lng_max") public double boundLngMax;
    @SerializedName("shape_area") public double shapeArea;
    @SerializedName("shape_len") public double shapeLen;

    public Province() {

    }

    protected Province(Parcel in) {
        provinceId = in.readInt();
        nameTh = in.readString();
        nameEn = in.readString();
        provinceIndex = in.readInt();
        regionCode = in.readInt();
        provinceCode = in.readInt();
        boundLatMin = in.readDouble();
        boundLatMax = in.readDouble();
        boundLngMin = in.readDouble();
        boundLngMax = in.readDouble();
        shapeArea = in.readDouble();
        shapeLen = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(provinceId);
        dest.writeString(nameTh);
        dest.writeString(nameEn);
        dest.writeInt(provinceIndex);
        dest.writeInt(regionCode);
        dest.writeInt(provinceCode);
        dest.writeDouble(boundLatMin);
        dest.writeDouble(boundLatMax);
        dest.writeDouble(boundLngMin);
        dest.writeDouble(boundLngMax);
        dest.writeDouble(shapeArea);
        dest.writeDouble(shapeLen);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Province> CREATOR = new Creator<Province>() {
        @Override
        public Province createFromParcel(Parcel in) {
            return new Province(in);
        }

        @Override
        public Province[] newArray(int size) {
            return new Province[size];
        }
    };
}
