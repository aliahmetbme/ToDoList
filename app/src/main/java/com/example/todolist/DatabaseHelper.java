package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "tasks";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TASK = "task";
    public static final String COLUMN_NOTES = "notes";
    public static final String COLUMN_MINUTES = "minutes";
    public static final String COLUMN_SECONDS = "seconds";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void onCreate(SQLiteDatabase db) {
        // Tabloları oluşturma işlemleri burada yapılır
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TASK + " TEXT, " +
                COLUMN_NOTES + " TEXT, " +
                COLUMN_MINUTES + " INTEGER, " +
                COLUMN_SECONDS + " INTEGER)";

        db.execSQL(createTableQuery);
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Veritabanı güncelleme durumunda yapılacaklar
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);    }

    public void insertDataForTask(String task ,String notes, String counter) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("task", task);
        values.put("notes", notes);
        values.put("counter", counter);


        long newRowId = db.insert("mytable", null, values);

        db.close();
    }
}
