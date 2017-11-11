package com.arm.tourist.Models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class TourPlan implements Parcelable, Comparable<TourPlan> {

    String title, placeName, startDate, endDate, unixTimeSt, unixTimeEn;
    String planDetails; // long description
    BackPack backPack;
    Guide guide;
    Hotel hotel;
    Double placeLat, placeLongt;
    String postID;

    public TourPlan() {}

    protected TourPlan(Parcel in) {
        title = in.readString();
        placeName = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        unixTimeSt = in.readString();
        unixTimeEn = in.readString();
        planDetails = in.readString();
        backPack = in.readParcelable(BackPack.class.getClassLoader());
        guide = in.readParcelable(Guide.class.getClassLoader());
        hotel = in.readParcelable(Hotel.class.getClassLoader());
        if (in.readByte() == 0) {
            placeLat = null;
        } else {
            placeLat = in.readDouble();
        }
        if (in.readByte() == 0) {
            placeLongt = null;
        } else {
            placeLongt = in.readDouble();
        }
        postID = in.readString();
    }

    public static final Creator<TourPlan> CREATOR = new Creator<TourPlan>() {
        @Override
        public TourPlan createFromParcel(Parcel in) {
            return new TourPlan(in);
        }

        @Override
        public TourPlan[] newArray(int size) {
            return new TourPlan[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getUnixTimeSt() {
        return unixTimeSt;
    }

    public void setUnixTimeSt(String unixTimeSt) {
        this.unixTimeSt = unixTimeSt;
    }

    public String getUnixTimeEn() {
        return unixTimeEn;
    }

    public void setUnixTimeEn(String unixTimeEn) {
        this.unixTimeEn = unixTimeEn;
    }

    public String getPlanDetails() {
        return planDetails;
    }

    public void setPlanDetails(String planDetails) {
        this.planDetails = planDetails;
    }

    public BackPack getBackPack() {
        return backPack;
    }

    public void setBackPack(BackPack backPack) {
        this.backPack = backPack;
    }

    public Guide getGuide() {
        return guide;
    }

    public void setGuide(Guide guide) {
        this.guide = guide;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public Double getPlaceLat() {
        return placeLat;
    }

    public void setPlaceLat(Double placeLat) {
        this.placeLat = placeLat;
    }

    public Double getPlaceLongt() {
        return placeLongt;
    }

    public void setPlaceLongt(Double placeLongt) {
        this.placeLongt = placeLongt;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(placeName);
        parcel.writeString(startDate);
        parcel.writeString(endDate);
        parcel.writeString(unixTimeSt);
        parcel.writeString(unixTimeEn);
        parcel.writeString(planDetails);
        parcel.writeParcelable(backPack, i);
        parcel.writeParcelable(guide, i);
        parcel.writeParcelable(hotel, i);
        if (placeLat == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(placeLat);
        }
        if (placeLongt == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(placeLongt);
        }
        parcel.writeString(postID);
    }

    @Override
    public int compareTo(@NonNull TourPlan tourPlan) {
        return 0;
    }
}