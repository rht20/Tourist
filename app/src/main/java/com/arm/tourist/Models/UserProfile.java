package com.arm.tourist.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class UserProfile implements Parcelable {

    private String name, email, phone, userID, profilePicUrl, DOB, addr,inst;

    public UserProfile()
    {
    }

    public UserProfile(String name, String email, String phone, String userID, String profilePicUrl, String DOB, String addr, String inst) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.userID = userID;
        this.profilePicUrl = profilePicUrl;
        this.DOB = DOB;
        this.addr = addr;
        this.inst = inst;
    }

    protected UserProfile(Parcel in) {
        name = in.readString();
        email = in.readString();
        phone = in.readString();
        userID = in.readString();
        profilePicUrl = in.readString();
        DOB = in.readString();
        addr = in.readString();
        inst = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserProfile> CREATOR = new Creator<UserProfile>() {
        @Override
        public UserProfile createFromParcel(Parcel in) {
            return new UserProfile(in);
        }

        @Override
        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getInst() {
        return inst;
    }

    public void setInst(String inst) {
        this.inst = inst;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(phone);
        parcel.writeString(userID);
        parcel.writeString(profilePicUrl);
        parcel.writeString(DOB);
        parcel.writeString(addr);
        parcel.writeString(inst);
    }
}