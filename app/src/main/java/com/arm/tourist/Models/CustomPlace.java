package com.arm.tourist.Models;


import android.os.Parcel;
import android.os.Parcelable;

public class CustomPlace implements Parcelable {

    String name;
    double lat, longt;

    public CustomPlace(){}

    protected CustomPlace(Parcel in) {
        name = in.readString();
        lat = in.readDouble();
        longt = in.readDouble();
    }

    public static final Creator<CustomPlace> CREATOR = new Creator<CustomPlace>() {
        @Override
        public CustomPlace createFromParcel(Parcel in) {
            return new CustomPlace(in);
        }

        @Override
        public CustomPlace[] newArray(int size) {
            return new CustomPlace[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongt() {
        return longt;
    }

    public void setLongt(double longt) {
        this.longt = longt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeDouble(lat);
        parcel.writeDouble(longt);
    }
}
