package com.example.schoolhourtracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String TABLE_NAME = "student_classes";
    private static final String CLASS_ID = "_id";
    private static final String CLASS_CODE = "classCode";
    private static final String CLASS_NAME = "className";

    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "(" + CLASS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CLASS_CODE + " TEXT NOT NULL, " + CLASS_NAME + " TEXT NOT NULL, UNIQUE(" + CLASS_CODE + ", " + CLASS_NAME + "))";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addClasses(String classId, String className) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CLASS_CODE, classId);
        contentValues.put(CLASS_NAME, className);

        Log.d(TAG, "AddClasses " + classId + " and " + className + " to " + TABLE_NAME);
        long result = db.insert(TABLE_NAME, null, contentValues);

        /* If data is inserted incorrectly, it will return -1 */
        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns all the classes from database
     * @return
     */
    public Cursor getAllClasses() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor rawData = db.rawQuery(selectQuery, null);
        return rawData;
    }
}
