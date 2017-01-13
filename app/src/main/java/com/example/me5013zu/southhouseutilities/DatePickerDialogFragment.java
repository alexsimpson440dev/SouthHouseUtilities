package com.example.me5013zu.southhouseutilities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Alex on 1/11/17.
 */

public class DatePickerDialogFragment  extends DialogFragment implements android.app.DatePickerDialog.OnDateSetListener {

    private String dueDate;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //current date as default
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new android.app.DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        month++;
        this.dueDate = month + "/" + day + "/" + year;
        Log.d("Date Picked:", dueDate);

    }

    public String getDueDate() {
        return this.dueDate;
    }

}
