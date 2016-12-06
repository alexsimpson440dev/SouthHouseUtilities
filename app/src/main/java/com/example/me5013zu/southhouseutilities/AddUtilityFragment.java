package com.example.me5013zu.southhouseutilities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.zip.Inflater;

/**
 * Created by me5013zu on 12/6/16.
 */

public class AddUtilityFragment extends Fragment {

    //activity that hosts the fragment
    MainActivity hostingActivity;
    private static final String TAG = "add utility fragment";

    private UtilitySelectedListener utilitySelectedListener;

    public interface UtilitySelectedListener {
        void getUtilityDetails(monthlyUtilityListItem items);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof UtilitySelectedListener) {
            utilitySelectedListener = (UtilitySelectedListener) context;
            Log.d(TAG, "On attach configured listener " + utilitySelectedListener);

        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }


    EditText monthYearEditText;
    EditText dueDateEditText;
    EditText amountDueEditText;

    private static final String ALL_UTILITIES_KEY = "All_utilities";
    private static final String DETAIL_FRAG_TAG = "detail fragment";
    public DatabaseReference dbReference;

    private UtilitiesArrayAdapter utilitiesArrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_utility_fragment, container, false);

        Button addUtilityButton = (Button) view.findViewById(R.id.add_items_button);
        monthYearEditText = (EditText) view.findViewById(R.id.month_year_edittext);
        dueDateEditText = (EditText) view.findViewById(R.id.due_date_edittext);
        amountDueEditText = (EditText) view.findViewById(R.id.amount_due_edittext);
        final ListView utilityListView = (ListView) view.findViewById(R.id.monthly_utility_listview);


        //create ArrayAdapter
        utilitiesArrayAdapter = new UtilitiesArrayAdapter(getActivity(), R.layout.monthly_utility_listview);

        //set adapter to listview
        utilityListView.setAdapter(utilitiesArrayAdapter);

        //add listener to the button to add the items to list
        addUtilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //saves utility to database
                saveUtility();

                //read what user typed into the editTexts
                String monthYearText = monthYearEditText.getText().toString();
                String dueDateText = dueDateEditText.getText().toString();
                String amountDueText = amountDueEditText.getText().toString();

                if (monthYearText.length() == 0 || dueDateText.length() == 0) {
                    Toast.makeText(getActivity(), "Please enter text into Fields", Toast.LENGTH_LONG).show();
                }

                //else create a new item in listview
                monthlyUtilityListItem newItem = new monthlyUtilityListItem(monthYearText, dueDateText, amountDueText);
                utilitiesArrayAdapter.add(new monthlyUtilityListItem(monthYearText, dueDateText, amountDueText));

                //notify data changed
                utilitiesArrayAdapter.notifyDataSetChanged();

                //clear editTexts
                monthYearEditText.getText().clear();
                dueDateEditText.getText().clear();
                amountDueEditText.getText().clear();

                //set focus to first edittext field
                monthYearEditText.requestFocus();
            }


        });

        utilityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                utilitySelectedListener.getUtilityDetails(utilitiesArrayAdapter.getItem(position));
            }
        });

        //configure firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbReference = database.getReference();

        getSortedUtilities();

        return view;
    }

    public void saveUtility() {
        String monthYearText = monthYearEditText.getText().toString();
        String dueDateText = dueDateEditText.getText().toString();
        String amountDueText = amountDueEditText.getText().toString();

        monthlyUtilityListItem items = new monthlyUtilityListItem(monthYearText, dueDateText, amountDueText);

        DatabaseReference newBill = dbReference.child(ALL_UTILITIES_KEY).push();
        newBill.setValue(items);

        Toast.makeText(getActivity(), "Bill Saved", Toast.LENGTH_SHORT).show();
    }

    public void getSortedUtilities() {
        //creates query for firebase to retrieve data from
        Query getAllUtilities = dbReference.child(ALL_UTILITIES_KEY);

        getAllUtilities.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    monthlyUtilityListItem utility = ds.getValue(monthlyUtilityListItem.class);
                    utilitiesArrayAdapter.add(utility);
                    utilitiesArrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(ALL_UTILITIES_KEY, "Database failed to load");
            }
        });

    }

    @Override
    public void onDetach() {
        super.onDetach();
        utilitySelectedListener = null;
    }
}
