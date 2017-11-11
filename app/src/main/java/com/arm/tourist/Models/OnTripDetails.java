package com.arm.tourist.Models;


public class OnTripDetails {

    String dayFirst, daySecond, dayThird, dayFourth, dayFifth;
    String cost;
    String uID;
    String title, tourPlace, tourDate;

    public String gettitle() {
        return title;
    }

    public void settitle(String title) {
        this.title = title;
    }

    public String getTourPlace() {
        return tourPlace;
    }

    public void setTourPlace(String tourPlace) {
        this.tourPlace = tourPlace;
    }

    public String getTourDate() {
        return tourDate;
    }

    public void setTourDate(String tourDate) {
        this.tourDate = tourDate;
    }

    public OnTripDetails(){}

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getDayFirst() {
        return dayFirst;
    }

    public void setDayFirst(String dayFirst) {
        this.dayFirst = dayFirst;
    }

    public String getDaySecond() {
        return daySecond;
    }

    public void setDaySecond(String daySecond) {
        this.daySecond = daySecond;
    }

    public String getDayThird() {
        return dayThird;
    }

    public void setDayThird(String dayThird) {
        this.dayThird = dayThird;
    }

    public String getDayFourth() {
        return dayFourth;
    }

    public void setDayFourth(String dayFourth) {
        this.dayFourth = dayFourth;
    }

    public String getDayFifth() {
        return dayFifth;
    }

    public void setDayFifth(String dayFifth) {
        this.dayFifth = dayFifth;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
