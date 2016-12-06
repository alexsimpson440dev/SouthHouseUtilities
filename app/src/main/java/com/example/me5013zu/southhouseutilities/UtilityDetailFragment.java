package com.example.me5013zu.southhouseutilities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by me5013zu on 12/6/16.
 */

public class UtilityDetailFragment extends Fragment {


    private static final String DETAIL_ARGS = "detail fragment arguments";
    private static final String TAG = "utility detail fragment";

    //sets arguments for this fragment
    public static UtilityDetailFragment newInstance(monthlyUtilityListItem items) {
        final Bundle args = new Bundle();
        args.putParcelable(DETAIL_ARGS, items);
        final UtilityDetailFragment fragment = new UtilityDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_utility_detail, container, false);

        final monthlyUtilityListItem items = getArguments().getParcelable(DETAIL_ARGS);
        Log.d(TAG, "onCreateView received the following items " + items);

        //set up view
        TextView monthYearDetail = (TextView) view.findViewById(R.id.month_year_detail_textview);
        TextView amountDueDetail = (TextView) view.findViewById(R.id.amount_due_detail_textview);
        TextView dueDateDetail = (TextView) view.findViewById(R.id.due_date_detail_textview);

        monthYearDetail.setText(items.getMonthYear());
        amountDueDetail.setText(items.getAmountDue());
        dueDateDetail.setText(items.getDueDate());


        return view;
    }
}
