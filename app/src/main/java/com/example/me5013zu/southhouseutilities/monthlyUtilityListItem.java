package com.example.me5013zu.southhouseutilities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by me5013zu on 11/29/16.
 */

public class monthlyUtilityListItem implements Parcelable {
    String monthYear;
    String dueDate;
    String amountDue;

    //firebase non-argument constructor
    monthlyUtilityListItem() {
    }

    public monthlyUtilityListItem(String monthYearText, String dueDateText, String amountDueText){
        this.monthYear = monthYearText;
        this.dueDate = dueDateText;
        this.amountDue = amountDueText;
    }

    public monthlyUtilityListItem(Parcel in) {
        monthYear = in.readString();
        dueDate = in.readString();
        amountDue = in.readString();
    }


    public String getMonthYear() {
        return monthYear;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(String amountDue) {
        this.amountDue = amountDue;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    @Override
    public String toString() {
        return "monthlyUtilityListItem{" +
                "monthYear='" + monthYear + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", amountDue='" + amountDue + '\'' +
                '}';
    }

    //Parcelable interface. Parcelable interfaces can send extras to other fragments/activities
    static final Parcelable.Creator<monthlyUtilityListItem> CREATOR = new Parcelable.Creator<monthlyUtilityListItem>() {
        public monthlyUtilityListItem createFromParcel(Parcel in) {
            return new monthlyUtilityListItem(in);
        }

        @Override
        public monthlyUtilityListItem[] newArray(int size) {
            return new monthlyUtilityListItem[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(monthYear);
        dest.writeString(dueDate);
        dest.writeString(amountDue);
    }

}
