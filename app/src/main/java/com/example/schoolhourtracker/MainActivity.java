package com.example.schoolhourtracker;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends ListActivity {
    private static final String TAG = "MainActivity";

    DatabaseHelper mDatabaseHelper;
    private Button btnAdd;
    private EditText classId;
    private EditText classText;

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Reference to the EditText of the layout main.xml */
        classId = (EditText) findViewById(R.id.txtClassId);

        /* Reference to the EditText of the layout main.xml */
        classText = (EditText) findViewById(R.id.txtClassName);

        /* Reference to the button of the layout main.xml */
        btnAdd = (Button) findViewById(R.id.btnAdd);

        /* Initialize Database Helper Class */
        mDatabaseHelper = new DatabaseHelper(this);
        populateListView();

        /**
         * Defining a click event listener for the button "Add Class"
         */
        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String classCode = classId.getText().toString();
                String className = classText.getText().toString();
                if((classId.length() != 0) && (classText.length() != 0)) {
                    addNewClass(classCode, className);
                    classId.setText("");
                    classText.setText("");
                    populateListView();
                } else {
                    toastMessage("You must provide course code and course name");
                }
            }
        });
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, Hour_Log.class);
        startActivity(i);
    }

    /**
     *  this populates the ListView fro the database
     */
    public void populateListView() {
        Log.d(TAG, "populateListView: Displaying classes in the ListView");
        //Get data and append to the list
        Cursor classData = mDatabaseHelper.getAllClasses();

        /* Items from database is stored in this ArrayList variable */
        ArrayList<String> classListData = new ArrayList<>();
        /* Loop through the ArrayList and add its values to the Cursor */
        if(classData.getCount() == 0) {
            toastMessage("The database is empty :(.");
        } else {
            while (classData.moveToNext()) {
                classListData.add(classData.getString(2));
            }
        }
        /* ArrayAdapter to set items to ListView */
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, classListData);
        adapter.notifyDataSetChanged();

        /* Setting the adapter to the ListView */
        setListAdapter(adapter);
    }

    /**
     * Insert class to the database
     * @param newClassId classCode
     * @param newClassName className
     */
    public void addNewClass(String newClassId, String newClassName) {
        boolean insertData = mDatabaseHelper.addClasses(newClassId, newClassName);
        if(insertData) {
            toastMessage("Class Successfully Inserted");
        } else {
            toastMessage("Something went wrong");
        }
    }

    /**
     * Customizable Toast
     * @param message Toast Message
     */
    private  void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}