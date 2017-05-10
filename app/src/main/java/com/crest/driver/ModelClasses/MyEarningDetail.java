package com.crest.driver.ModelClasses;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by annie on 28/4/17.
 */

public class MyEarningDetail implements Parcelable {
    String date;
    String tripId;
    String type;
    String action;
    String actionAmount;
    String balance;

    public MyEarningDetail(String date, String tripId, String type, String action, String actionAmount, String balance) {
        this.date = date;
        this.tripId = tripId;
        this.type = type;
        this.action = action;
        this.actionAmount = actionAmount;
        this.balance = balance;
    }

    public String getDate() {
        return date;
    }

    public String getTripId() {
        return tripId;
    }

    public String getType() {
        return type;
    }

    public String getAction() {
        return action;
    }

    public String getActionAmount() {
        return actionAmount;
    }

    public String getBalance() {
        return balance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.date);
        dest.writeString(this.tripId);
        dest.writeString(this.type);
        dest.writeString(this.action);
        dest.writeString(this.actionAmount);
        dest.writeString(this.balance);
    }

    protected MyEarningDetail(Parcel in) {
        this.date = in.readString();
        this.tripId = in.readString();
        this.type = in.readString();
        this.action = in.readString();
        this.actionAmount = in.readString();
        this.balance = in.readString();
    }

    public static final Parcelable.Creator<MyEarningDetail> CREATOR = new Parcelable.Creator<MyEarningDetail>() {
        @Override
        public MyEarningDetail createFromParcel(Parcel source) {
            return new MyEarningDetail(source);
        }

        @Override
        public MyEarningDetail[] newArray(int size) {
            return new MyEarningDetail[size];
        }
    };
}
