package com.example.me5013zu.southhouseutilities;

/**
 * Created by me5013zu on 11/29/16.
 */

public class monthlyUtilityListItem {
    String monthYear;
    String dueDate;

    public monthlyUtilityListItem(String text){
        this.monthYear = text;
        this.dueDate = text;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public String getDueDate() {
        return dueDate;
    }
}
