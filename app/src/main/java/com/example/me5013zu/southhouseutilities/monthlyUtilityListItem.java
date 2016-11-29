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

}
