package com.arm.tourist.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by hhmoon on 11/1/17.
 */

public class BackPack implements Parcelable {
    ArrayList<String> ItemsToCarryWith;

    public BackPack() {};

    protected BackPack(Parcel in) {
        ItemsToCarryWith = in.createStringArrayList();
    }

    public static final Creator<BackPack> CREATOR = new Creator<BackPack>() {
        @Override
        public BackPack createFromParcel(Parcel in) {
            return new BackPack(in);
        }

        @Override
        public BackPack[] newArray(int size) {
            return new BackPack[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringList(ItemsToCarryWith);
    }

    public ArrayList<String> getItemsToCarryWith() {
        return ItemsToCarryWith;
    }

    public void setItemsToCarryWith(ArrayList<String> itemsToCarryWith) {
        ItemsToCarryWith = itemsToCarryWith;
    }

    public BackPack(ArrayList<String> itemsToCarryWith) {
        ItemsToCarryWith = itemsToCarryWith;
    }

}
