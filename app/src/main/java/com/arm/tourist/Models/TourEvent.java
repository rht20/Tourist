package com.arm.tourist.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class TourEvent implements Parcelable {

    private String note;
    private String tourTitle, postId, tourPlace, startDate, endDate;
    private String likeCount, totalCost;
    private String hotelDescription, guideName;
    private String cover, img1, img2, img3;
    private String userName, userImage;
    private String time;

    public TourEvent(){}

    protected TourEvent(Parcel in) {
        note = in.readString();
        tourTitle = in.readString();
        postId = in.readString();
        tourPlace = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        likeCount = in.readString();
        totalCost = in.readString();
        hotelDescription = in.readString();
        guideName = in.readString();
        cover = in.readString();
        img1 = in.readString();
        img2 = in.readString();
        img3 = in.readString();
        userName = in.readString();
        userImage = in.readString();
        time = in.readString();
    }

    public static final Creator<TourEvent> CREATOR = new Creator<TourEvent>() {
        @Override
        public TourEvent createFromParcel(Parcel in) {
            return new TourEvent(in);
        }

        @Override
        public TourEvent[] newArray(int size) {
            return new TourEvent[size];
        }
    };

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTourTitle() {
        return tourTitle;
    }

    public void setTourTitle(String tourTitle) {
        this.tourTitle = tourTitle;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTourPlace() {
        return tourPlace;
    }

    public void setTourPlace(String tourPlace) {
        this.tourPlace = tourPlace;
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

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public String getHotelDescription() {
        return hotelDescription;
    }

    public void setHotelDescription(String hotelDescription) {
        this.hotelDescription = hotelDescription;
    }

    public String getGuideName() {
        return guideName;
    }

    public void setGuideName(String guideName) {
        this.guideName = guideName;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(note);
        parcel.writeString(tourTitle);
        parcel.writeString(postId);
        parcel.writeString(tourPlace);
        parcel.writeString(startDate);
        parcel.writeString(endDate);
        parcel.writeString(likeCount);
        parcel.writeString(totalCost);
        parcel.writeString(hotelDescription);
        parcel.writeString(guideName);
        parcel.writeString(cover);
        parcel.writeString(img1);
        parcel.writeString(img2);
        parcel.writeString(img3);
        parcel.writeString(userName);
        parcel.writeString(userImage);
        parcel.writeString(time);
    }
}
