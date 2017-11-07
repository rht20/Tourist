package com.arm.tourist.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hhmoon on 11/1/17.
 */

public class Guide implements Parcelable {
    String name, email, phoneNo, guidePlace;

    public Guide() {};

    protected Guide(Parcel in) {
        name = in.readString();
        email = in.readString();
        phoneNo = in.readString();
        guidePlace = in.readString();
    }

    public static final Creator<Guide> CREATOR = new Creator<Guide>() {
        @Override
        public Guide createFromParcel(Parcel in) {
            return new Guide(in);
        }

        @Override
        public Guide[] newArray(int size) {
            return new Guide[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(phoneNo);
        parcel.writeString(guidePlace);
    }

    public Guide(String name, String email, String phoneNo, String guidePlace) {
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
        this.guidePlace = guidePlace;
    }

    public Guide(String name, String phoneNo) {
        this.name = name;
        this.phoneNo = phoneNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getGuidePlace() {
        return guidePlace;
    }

    public void setGuidePlace(String guidePlace) {
        this.guidePlace = guidePlace;
    }

    @Override
    public String toString() {
        return "Guide{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", guidePlace='" + guidePlace + '\'' +
                '}';
    }
}
