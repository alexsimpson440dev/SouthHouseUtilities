package com.example.me5013zu.southhouseutilities;

import android.*;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.app.LauncherActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddUtilityFragment.UtilitySelectedListener {

    private static final String DETAIL_FRAG_TAG = "detail fragment";
    String userId = getSharedPreferences(SignInFragment.USERS_PREFS, MODE_PRIVATE).getString(SignInFragment.FIREBASE_USER_ID_PREF_KEY, "something has gone wrong here");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //sets the fragment view to the AddFragmentUtility's layout
        addUtilityFragment();
    }

    //method for setting the layout to the AddUtilityFragment
    private void addUtilityFragment() {
        //sets the fragment manager and transaction
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        //sets new instance for AddUtilityFragment
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        AddUtilityFragment addUtilityFragment = new AddUtilityFragment();
        addUtilityFragment.setArguments(bundle);
        //adds the fragment
        ft.add(android.R.id.content, addUtilityFragment);

        //commits the transaction
        ft.commit();

    }



    public void getUtilityDetails(monthlyUtilityListItem selected) {
        //begins fragment transaction
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        //replaces a fragment with this fragment
        UtilityDetailFragment detailFragment = UtilityDetailFragment.newInstance(selected);
        ft.replace(android.R.id.content, detailFragment, DETAIL_FRAG_TAG);

        //addtobackstack allows user to press back button
        ft.addToBackStack(DETAIL_FRAG_TAG);

        //commits transaction
        ft.commit();
    }
}
