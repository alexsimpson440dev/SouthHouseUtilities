package com.example.me5013zu.southhouseutilities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Alex on 12/13/16.
 */

public class UsersUtilitiesFragment extends Fragment {

    public DatabaseReference dbReference;
    private UtilitiesArrayAdapter utilitiesArrayAdapter;
    private static final String ALL_UTILITIES_KEY = "All_utilities";
    private static final String TAG = "users utilites fragment";

    //listener for main activity
    private UsersUtilitiesSelectedListener usersUtilitiesSelectedListener;

    //interface for listener
    public interface UsersUtilitiesSelectedListener {
        void getUtilityDetails(MonthlyUtilityListItem items);
    }

    //onAttach method for listener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof UsersUtilitiesFragment.UsersUtilitiesSelectedListener) {
            usersUtilitiesSelectedListener = (UsersUtilitiesFragment.UsersUtilitiesSelectedListener) context;
            Log.d(TAG, "On attach configured listener " + usersUtilitiesSelectedListener);

        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_utilities, container, false);


        //variables
        ListView usersListView = (ListView) view.findViewById(R.id.users_monthly_utility_listview);

        //create ArrayAdapter
        utilitiesArrayAdapter = new UtilitiesArrayAdapter(getActivity(), R.layout.monthly_utility_listview);

        //set adapter to listview
        usersListView.setAdapter(utilitiesArrayAdapter);

        //configure Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbReference = database.getReference();

        //gets data from database and sends it to listview
        getSortedUtilities();

        //when a listview Item is clicked, the listener is sent to MainActivity with which item.
        //from main activity the item is then sent to the UtilityDetailFragment
        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                usersUtilitiesSelectedListener.getUtilityDetails(utilitiesArrayAdapter.getItem(position));
            }
        });

        return view;
    }



    public void getSortedUtilities() {

        //creates query for firebase to retrieve data from
        Query getAllUtilities = dbReference.child(ALL_UTILITIES_KEY);

        //sets a listener for a single value. (only updates single information)
        getAllUtilities.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //for each item in the database, add it to the list view. Used for startup
                if (dataSnapshot.getValue() == null)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("No current Utilities")
                            .setTitle("No Utilities");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

                else {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        MonthlyUtilityListItem utility = ds.getValue(MonthlyUtilityListItem.class);
                        //adds data to array
                        utilitiesArrayAdapter.add(utility);
                        //notifies data in the array was changed
                        utilitiesArrayAdapter.notifyDataSetChanged();
                    }
                }
            }

            //if the database fails, error message will show up
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(ALL_UTILITIES_KEY, "Database failed to load");
            }
        });

    }
}
