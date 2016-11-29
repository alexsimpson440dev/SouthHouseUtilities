package com.example.me5013zu.southhouseutilities;

import android.app.LauncherActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText monthYearEditText;
    EditText dueDateEditText;
    EditText amountDueEditText;

    private static final String ALL_UTILITIES_KEY = "All_utilities";
    private DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {

            Button addUtilityButton = (Button) findViewById(R.id.add_items_button);
            monthYearEditText = (EditText) findViewById(R.id.month_year_edittext);
            dueDateEditText = (EditText) findViewById(R.id.due_date_edittext);
            amountDueEditText = (EditText) findViewById(R.id.amount_due_edittext);
            ListView utilityListView = (ListView) findViewById(R.id.monthly_utility_listview);

            //create ArrayAdapter
            final UtilitiesArrayAdapter utilitiesArrayAdapter = new UtilitiesArrayAdapter(this, R.layout.monthly_utility_listview);

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
                        Toast.makeText(MainActivity.this, "Please enter text into Fields", Toast.LENGTH_LONG).show();
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

        } else {
            
        }

        //configure firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbReference = database.getReference();
    }

    private void saveUtility() {
        String monthYearText = monthYearEditText.getText().toString();
        String dueDateText = dueDateEditText.getText().toString();
        String amountDueText = amountDueEditText.getText().toString();

        monthlyUtilityListItem items = new monthlyUtilityListItem(monthYearText, dueDateText, amountDueText);

        DatabaseReference newBill = dbReference.child(ALL_UTILITIES_KEY).push();
        newBill.setValue(items);

        Toast.makeText(this, "Bill Saved", Toast.LENGTH_SHORT).show();
    }
}
