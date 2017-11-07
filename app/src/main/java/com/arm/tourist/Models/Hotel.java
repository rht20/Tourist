package com.arm.tourist.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hhmoon on 11/1/17.
 */

public class Hotel implements Parcelable {
    String name, address, contactNo, contactEmail, websiteAddress;

    public Hotel() {};

    protected Hotel(Parcel in) {
        name = in.readString();
        address = in.readString();
        contactNo = in.readString();
        contactEmail = in.readString();
        websiteAddress = in.readString();
    }

    public static final Creator<Hotel> CREATOR = new Creator<Hotel>() {
        @Override
        public Hotel createFromParcel(Parcel in) {
            return new Hotel(in);
        }

        @Override
        public Hotel[] newArray(int size) {
            return new Hotel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeString(contactNo);
        parcel.writeString(contactEmail);
        parcel.writeString(websiteAddress);
    }

    public Hotel(String name, String address, String contactNo, String contactEmail, String websiteAddress) {
        this.name = name;
        this.address = address;
        this.contactNo = contactNo;
        this.contactEmail = contactEmail;
        this.websiteAddress = websiteAddress;
    }

    public Hotel(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getWebsiteAddress() {
        return websiteAddress;
    }

    public void setWebsiteAddress(String websiteAddress) {
        this.websiteAddress = websiteAddress;
    }


    @Override
    public String toString() {
        return "Hotel{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", contactNo='" + contactNo + '\'' +
                ", contactEmail='" + contactEmail + '\'' +
                ", websiteAddress='" + websiteAddress + '\'' +
                '}';
    }

}
