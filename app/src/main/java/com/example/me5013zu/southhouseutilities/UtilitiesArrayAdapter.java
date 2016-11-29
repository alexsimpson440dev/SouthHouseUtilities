package com.example.me5013zu.southhouseutilities;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by me5013zu on 11/29/16.
 */

public class UtilitiesArrayAdapter extends ArrayAdapter<monthlyUtilityListItem> {

    Context context;

    public UtilitiesArrayAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
    }

    //method is used to create a view for the listItems
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            rowView = inflater.inflate(R.layout.monthly_utility_listview, parent, false);
        }

        //add data to listview
        monthlyUtilityListItem item = getItem(position);

        TextView monthYearItem = (TextView) rowView.findViewById(R.id.month_year_textview);
        TextView dueDateItem = (TextView) rowView.findViewById(R.id.due_date_textview);
        TextView amountDueItem = (TextView) rowView.findViewById(R.id.amount_due_textview);

        //set values
        monthYearItem.setText(item.getMonthYear());
        dueDateItem.setText(item.getDueDate());
        amountDueItem.setText(item.getAmountDue());

        return rowView;

    }

}
