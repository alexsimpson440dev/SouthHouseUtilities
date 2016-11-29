package com.example.me5013zu.southhouseutilities;

/**
 * Created by me5013zu on 11/29/16.
 */

public class monthlyUtilityListItem {
    String monthYear;
    String dueDate;
    String amountDue;

    public monthlyUtilityListItem(String monthYearText, String dueDateText, String amountDueText){
        this.monthYear = monthYearText;
        this.dueDate = dueDateText;
        this.amountDue = amountDueText;
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
}
