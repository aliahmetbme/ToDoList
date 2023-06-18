package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import kotlinx.coroutines.scheduling.Task;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void onCreate(SQLiteDatabase db) {
        // Tabloları oluşturma işlemleri burada yapılır
        String createTableQuery = "CREATE TABLE mytable (id INTEGER PRIMARY KEY, name TEXT, age INTEGER);";
        db.execSQL(createTableQuery);
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Veritabanı sürümü değiştiğinde gerekli güncelleme işlemleri burada yapılır
    }

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
