package com.example.todolist;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class TodoListActivity extends AppCompatActivity {


    Vibrator vibrator = null;
    MediaPlayer mediaPlayer = null;
    DatabaseHelper dbHelper ;
    ContentValues values = new ContentValues();
    CalendarView calendarView ;
    String Day, Month, Year;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        Button addList = findViewById(R.id.addList);
        Button clearTasks = findViewById(R.id.trash);
        dbHelper = new DatabaseHelper(this);
        getTasks(); // when app is turned on databased go out to information

        // adding new tasks
        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTask();
            }
        });

        clearTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearTable();
                getTasks();
            }
        });
    }

    // adding new tasks
    public  void addTask(){

        Dialog scaleOptionDialog = new Dialog(this);
        scaleOptionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        scaleOptionDialog.setContentView(R.layout.layout_add);
        scaleOptionDialog.show();

        Button cancel = scaleOptionDialog.findViewById(R.id.cancelButton);
        Button _addButton = scaleOptionDialog.findViewById(R.id.addButton);

        LinearLayout taskList = findViewById(R.id.list);

        LayoutInflater inflater = LayoutInflater.from(this);  // LayoutInflater oluşturun
        View view = inflater.inflate(R.layout.task,taskList,false); // XML dosyasını LinearLayout'a ekle

        EditText taskToAddList = scaleOptionDialog.findViewById(R.id.organiseTask);
        EditText notesToAddList = scaleOptionDialog.findViewById(R.id.organiseNotes);

         // layout add'deki edittext
        Spinner secondSpinner = scaleOptionDialog.findViewById(R.id.second); // layout add'deki edittext
        Spinner minuteSpinner = scaleOptionDialog.findViewById(R.id.spinner);
        Spinner daySpinner = scaleOptionDialog.findViewById(R.id.spinnerDay);
        Spinner monthSpinner = scaleOptionDialog.findViewById(R.id.spinnerMont);

        TextView newTask = view.findViewById(R.id.myTask); // taskview
        TextView newNote = view.findViewById(R.id.notes); // taskview
        ImageButton startButton = view.findViewById(R.id.timerStar); // taskview
        ImageButton stopButton = view.findViewById(R.id.timerStop);  // taskview
        ImageButton resetButton = view.findViewById(R.id.timerReset); // taskview
        TextView counter = view.findViewById(R.id.counter); // taskview
        TextView date = view.findViewById(R.id.date);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch _switch = view.findViewById(R.id.switch1);

        if (counter.equals("00:00")) {
            startButton.setEnabled(false); // zaman 0 sa başlamaz
        }

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButton.setEnabled(false);
                stopButton.setEnabled(true);

                String minute = counter.getText().toString().split(":")[0]; // dakika
                String second = counter.getText().toString().split(":")[1]; // saniye

                try {
                    if(second.equals("")){
                        second="0";
                    }
                    int value = Integer.parseInt(minute.trim())* 60 * 1000+ Integer.parseInt(second.trim()) * 1000; // zaman değeri

                    String finalSecond = second;
                    CountDownTimer countDownTime = new CountDownTimer(value ,1000){
                        @SuppressLint("SetTextI18n")
                        public void onTick(long millisUntilFinished){

                            long time = millisUntilFinished / 1000;

                            long minuteValue =  time / 60;
                            long secondValue =  time % 60;

                            if (minuteValue < 10 && secondValue < 10){
                                counter.setText( "0"+ minuteValue + " : 0" + secondValue);
                            } else if(minuteValue < 10 ){
                                counter.setText( "0"+ minuteValue + " : " + secondValue);
                            } else if ( secondValue < 10){
                                counter.setText(minuteValue + " : 0" + secondValue);
                            } else {
                                counter.setText(minuteValue + " : " + secondValue);
                            }

                            SQLiteDatabase db = dbHelper.getWritableDatabase();

                            ContentValues updatedValues = new ContentValues();
                            updatedValues.put(DatabaseHelper.COLUMN_MINUTES, String.valueOf(minuteValue));
                            updatedValues.put(DatabaseHelper.COLUMN_SECONDS, String.valueOf(secondValue));

                            String whereClause = DatabaseHelper.COLUMN_MINUTES + " = ?";
                            String whereClause1 = DatabaseHelper.COLUMN_SECONDS + " = ?";


                            String[] whereArgs = {minute};
                            String[] whereArgs1 = {finalSecond};

                            int rowsUpdated = db.update(DatabaseHelper.TABLE_NAME, updatedValues, whereClause, whereArgs);
                            int rowsUpdated1 = db.update(DatabaseHelper.TABLE_NAME, updatedValues, whereClause1, whereArgs1);


                            if (rowsUpdated > 0 && rowsUpdated1 >0) {
                                // Başarılı bir şekilde güncellendi
                                Toast.makeText(TodoListActivity.this, "Görev güncellendi.", Toast.LENGTH_SHORT).show();
                            } else {
                                // Hata oluştu
                                Toast.makeText(TodoListActivity.this, "Görev güncellenirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
                            }

                            // Veritabanı bağlantısını kapat
                            db.close();

                        }

                        @Override
                        public void onFinish() {
                            counter.setText("00:00");
                            startButton.setEnabled(true);
                            alertSound(); //  ses
                            alertVib(); // titreşim
                        }

                    }.start();

                    stopButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startButton.setEnabled(true);
                            resetButton.setEnabled(true);
                            countDownTime.cancel();

                            Toast.makeText(TodoListActivity.this, "burda",Toast.LENGTH_LONG).show();

                        }
                    });

                    resetButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            countDownTime.cancel();
                            stopButton.setEnabled(false);


                            SQLiteDatabase db = dbHelper.getWritableDatabase();

                            ContentValues updatedValues = new ContentValues();
                            ContentValues updatedValues1 = new ContentValues();

                            updatedValues.put(DatabaseHelper.COLUMN_MINUTES, String.valueOf(0));
                            updatedValues1.put(DatabaseHelper.COLUMN_SECONDS, String.valueOf(0));

                            String whereClause = DatabaseHelper.COLUMN_MINUTES + " = ?";
                            String whereClause1 = DatabaseHelper.COLUMN_SECONDS + " = ?";

                            String[] whereArgs = {counter.getText().toString().split(":")[0].trim()};
                            String[] whereArgs1 = {counter.getText().toString().split(":")[0].trim()};

                            int rowsUpdated = db.update(DatabaseHelper.TABLE_NAME, updatedValues, whereClause, whereArgs);
                            int rowsUpdated1 = db.update(DatabaseHelper.TABLE_NAME, updatedValues1, whereClause1, whereArgs1);

                            if (rowsUpdated > 0 && rowsUpdated1 > 0) {
                                // Başarılı bir şekilde güncellendi
                                Toast.makeText(TodoListActivity.this, "Görev güncellendi.", Toast.LENGTH_SHORT).show();
                            } else {
                                // Hata oluştu
                                Toast.makeText(TodoListActivity.this, "Görev sikiyim bir hata oluştu.", Toast.LENGTH_SHORT).show();
                            }

                            // Veritabanı bağlantısını kapat
                            db.close();
                            counter.setText("00:00");
                        }
                    });

                } catch (Exception e){
                    Toast.makeText(TodoListActivity.this,"You didn't give the time correctly",Toast.LENGTH_LONG).show();
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopButton.setEnabled(false);


                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues updatedValues = new ContentValues();
                ContentValues updatedValues1 = new ContentValues();

                updatedValues.put(DatabaseHelper.COLUMN_MINUTES, String.valueOf(0));
                updatedValues1.put(DatabaseHelper.COLUMN_SECONDS, String.valueOf(0));

                String whereClause = DatabaseHelper.COLUMN_MINUTES + " = ?";
                String whereClause1 = DatabaseHelper.COLUMN_SECONDS + " = ?";

                String[] whereArgs = {counter.getText().toString().split(":")[0].trim()};
                String[] whereArgs1 = {counter.getText().toString().split(":")[0].trim()};

                int rowsUpdated = db.update(DatabaseHelper.TABLE_NAME, updatedValues, whereClause, whereArgs);
                int rowsUpdated1 = db.update(DatabaseHelper.TABLE_NAME, updatedValues1, whereClause1, whereArgs1);

                if (rowsUpdated > 0 && rowsUpdated1 > 0) {
                    // Başarılı bir şekilde güncellendi
                    Toast.makeText(TodoListActivity.this, "Görev güncellendi.", Toast.LENGTH_SHORT).show();
                } else {
                    // Hata oluştu
                    Toast.makeText(TodoListActivity.this, "Görev sikiyim bir hata oluştu.", Toast.LENGTH_SHORT).show();
                }

                // Veritabanı bağlantısını kapat
                db.close();
                counter.setText("00:00");


            }
        });


        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue = parent.getItemAtPosition(position).toString();
                Day = String.valueOf(selectedValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Day = "01";
            }
        });

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue = parent.getItemAtPosition(position).toString();
                Month = String.valueOf(selectedValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Month = "01";
            }
        });





        final int[] minuteValue = {0};
        final int[] secondValue = {0};

        minuteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue = parent.getItemAtPosition(position).toString();
                minuteValue[0] = Integer.parseInt(selectedValue.trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                minuteValue[0] = 0;
            }
        });

        secondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue = parent.getItemAtPosition(position).toString();
                secondValue[0] = Integer.parseInt(selectedValue.trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                secondValue[0] = 0;
            }
        });


        _addButton.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {



                if (newTask.equals("")) {

                    Toast.makeText(TodoListActivity.this, "Please add your task", Toast.LENGTH_LONG).show();

                } else {

                    date.setText(Day + "  / " + Month);

                    String task = taskToAddList.getText().toString() ; // layout add deki edittext
                    String notes = notesToAddList.getText().toString();

                    newTask.setText(task); // string değeri layoutdaki textview a atanır
                    newTask.setPadding(16,5,10,10);

                    newNote.setText(notes); // string değeri layoutdaki textview a atanır
                    newNote.setPadding(16,5,10,10);

                    try {

                        if (secondValue[0] >= 60){ // saniye 60 dan büyükse dakikaya çevrilir
                            minuteValue[0] += secondValue[0] / 60;
                            secondValue[0] = secondValue[0] % 60;
                        }

                        if (minuteValue[0] < 10 && secondValue[0] < 10){
                            counter.setText( "0"+ minuteValue[0] + " : 0" + secondValue[0]);
                        } else if(minuteValue[0] < 10 ){
                            counter.setText( "0"+ minuteValue[0] + " : " + secondValue[0]);
                        } else if (secondValue[0] < 10){
                            counter.setText(minuteValue[0] + " : 0" + secondValue[0]);
                        } else {
                            counter.setText(minuteValue[0] + " : " + secondValue[0]);
                        }
                    } catch (Exception e ) {
                        counter.setText(minuteValue[0] + " : " + secondValue[0]);
                    }

                    view.setClickable(true);

                    newTask.setTextSize(25);
                    newTask.setTextColor(Color.WHITE);

                    newNote.setTextSize(20);
                    newNote.setTextColor(Color.WHITE);

                    newTask.getAutoSizeMaxTextSize();

                    newTask.setMovementMethod(new ScrollingMovementMethod());
                    newNote.setMovementMethod(new ScrollingMovementMethod());

                    newTask.setVisibility(View.VISIBLE);
                    newNote.setVisibility(View.VISIBLE);

                    taskList.addView(view);

                    _switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked){ // on
                                view.setAlpha(0.3f);
                            } else { // off
                                view.setAlpha(1);
                            }
                        }
                    });
                    // tasklarla ilgilili bilgiler database e gider.
                    values.put(DatabaseHelper.COLUMN_TASK, newTask.getText().toString());
                    values.put(DatabaseHelper.COLUMN_NOTES, newNote.getText().toString());
                    values.put(DatabaseHelper.COLUMN_MINUTES, String.valueOf(minuteValue[0]));
                    values.put(DatabaseHelper.COLUMN_SECONDS, String.valueOf(secondValue[0]));
                    values.put(DatabaseHelper.COLUMN_DATE,date.getText().toString());

                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    long id = db.insert(DatabaseHelper.TABLE_NAME, null, values);

                    if (id != -1) {
                        // Başarılı bir şekilde eklendi
                        Toast.makeText(TodoListActivity.this, " You created your task.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Hata oluştu
                    }

                    // Veritabanı bağlantısını kapat
                    db.close();

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Dialog dialog = new Dialog(TodoListActivity.this);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.setContentView(R.layout.layout_move);
                            dialog.show();

                            EditText editTaskToAddList = dialog.findViewById(R.id.editTask);
                            EditText editNotesToAddList = dialog.findViewById(R.id.editNotes);

                            String previousTask = newTask.getText().toString(); // önceki hazır textview in değerini alıp üstüne yazmak için
                            String previousNotes = newNote.getText().toString();

                            editTaskToAddList.setText(previousTask);  // önceki hazır textview in değerini alıp üstüne yazmak için
                            editNotesToAddList.setText(previousNotes);

                            Button editButton = dialog.findViewById(R.id.editButton);
                            Button cancelEdit = dialog.findViewById(R.id.deleteButton);

                            editButton.setOnClickListener(new View.OnClickListener() { // tasklari editlemek için
                                @Override
                                public void onClick(View v) {

                                    String editedTask  = editTaskToAddList.getText().toString();
                                    String editedNotes = editNotesToAddList.getText().toString();

                                    newTask.setText(editedTask);
                                    newTask.setPadding(16,5,10,10);

                                    newNote.setText(editedNotes);
                                    newNote.setPadding(16,5,10,10);

                                    // Veritabanını güncelle
                                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                                    ContentValues updatedValues = new ContentValues();
                                    updatedValues.put(DatabaseHelper.COLUMN_TASK, editedTask);
                                    updatedValues.put(DatabaseHelper.COLUMN_NOTES, editedNotes);

                                    String whereClause = DatabaseHelper.COLUMN_TASK + " = ?";
                                    String[] whereArgs = {previousTask};

                                    int rowsUpdated = db.update(DatabaseHelper.TABLE_NAME, updatedValues, whereClause, whereArgs);

                                    if (rowsUpdated > 0) {
                                        // Başarılı bir şekilde güncellendi
                                        Toast.makeText(TodoListActivity.this, " Your task was edited ", Toast.LENGTH_LONG).show();
                                    } else {
                                        // Hata oluştu
                                        Toast.makeText(TodoListActivity.this, "Görev güncellenirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
                                    }

                                    // Veritabanı bağlantısını kapat
                                    db.close();
                                    dialog.hide();
                                }
                            });

                            _switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if (isChecked){ // on
                                        view.setAlpha(0.3f);
                                    } else { // off
                                        view.setAlpha(1);
                                    }
                                }
                            });

                            cancelEdit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.hide();
                                }
                            });
                        }
                    });
                    scaleOptionDialog.hide();
                }

                resetButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stopButton.setEnabled(false);
                        counter.setText("00:00");

                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        ContentValues updatedValues = new ContentValues();
                        ContentValues updatedValues1 = new ContentValues();

                        updatedValues.put(DatabaseHelper.COLUMN_MINUTES, String.valueOf(0));
                        updatedValues1.put(DatabaseHelper.COLUMN_SECONDS, String.valueOf(0));

                        String whereClause = DatabaseHelper.COLUMN_MINUTES + " = ?";
                        String whereClause1 = DatabaseHelper.COLUMN_SECONDS + " = ?";
//                        String[] whereArgs = {minutesComesDb};
//                        String[] whereArgs1 = {secondsComesDb};
//
//                        int rowsUpdated = db.update(DatabaseHelper.TABLE_NAME, updatedValues, whereClause, whereArgs);
//                        int rowsUpdated1 = db.update(DatabaseHelper.TABLE_NAME, updatedValues1, whereClause1, whereArgs1);
//
//                        if (rowsUpdated > 0 && rowsUpdated1 > 0) {
//                            // Başarılı bir şekilde güncellendi
//                            Toast.makeText(TodoListActivity.this, "Görev güncellendi.", Toast.LENGTH_SHORT).show();
//                        } else {
//                            // Hata oluştu
//                            Toast.makeText(TodoListActivity.this, "Görev güncellenirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
//                        }

                        // Veritabanı bağlantısını kapat
                        db.close();
                    }
                });
            }
       });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scaleOptionDialog.hide();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() { // baslamadan önce sıfırlamak
            @Override
            public void onClick(View v) {
                stopButton.setEnabled(false);
                counter.setText("00:00");
            }
        });

    }

    public void alertSound() {

        mediaPlayer = MediaPlayer.create(TodoListActivity.this,R.raw.alarm); // ses çıkarmak için

        mediaPlayer.seekTo(0);
        mediaPlayer.start();
    }

    public void alertVib(){

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(VibrationEffect.createOneShot(5000,VibrationEffect.DEFAULT_AMPLITUDE));
    }

    @SuppressLint("SetTextI18n")
    public  void getTasks(){

        // Veritabanından görevleri sorgula
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME, null, null, null, null, null, null);

        LinearLayout taskListLayout = findViewById(R.id.list);
        taskListLayout.removeAllViews();


        // Cursor'da kayıt varsa
        while (cursor.moveToNext()){

            // Kayıttaki sütunlar
            @SuppressLint("Range") String taskComesDb = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK));
            @SuppressLint("Range") String notesComesDb = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NOTES));
            @SuppressLint("Range") String minutesComesDb = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_MINUTES));
            @SuppressLint("Range") String secondsComesDb = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_SECONDS));
            @SuppressLint("Range") String datesComesDb = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE));

            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.task, taskListLayout, false);

            TextView myTask = view.findViewById(R.id.myTask);
            TextView Notes = view.findViewById(R.id.notes);
            TextView counter = view.findViewById(R.id.counter);
            TextView date = view.findViewById(R.id.date);
            @SuppressLint("UseSwitchCompatOrMaterialCode") Switch _switch = view.findViewById(R.id.switch1);

            myTask.setText(taskComesDb);
            Notes.setText(notesComesDb);
            counter.setText(minutesComesDb + " : " + secondsComesDb);
            date.setText(datesComesDb);

            ImageButton startButton = view.findViewById(R.id.timerStar); // taskview
            ImageButton stopButton = view.findViewById(R.id.timerStop);  // taskview
            ImageButton resetButton = view.findViewById(R.id.timerReset); // taskview

            myTask.setPadding(16, 5, 10, 10);
            Notes.setPadding(16, 5, 10, 10);

            int minuteValue = 0;
            int secondValue = 0;

            try {
                minuteValue = Integer.parseInt(minutesComesDb.toString());
                secondValue = Integer.parseInt(secondsComesDb.toString());

                if (secondValue >= 60){ // saniye 60 dan büyükse dakikaya çevrilir
                    minuteValue += secondValue / 60;
                    secondValue = secondValue % 60;
                }

                if (minuteValue < 10 && secondValue < 10){
                    counter.setText( "0"+ minuteValue + " : 0" + secondValue);
                } else if(minuteValue < 10 ){
                    counter.setText( "0"+ minuteValue + " : " + secondValue);
                } else if (secondValue < 10){
                    counter.setText(minuteValue + " : 0" + secondValue);
                } else {
                    counter.setText(minuteValue + " : " + secondValue);
                }
            } catch (Exception e ) {
                counter.setText(minuteValue + " : " + secondValue);
            }

            view.setClickable(true);

            myTask.setTextSize(25);
            myTask.setTextColor(Color.WHITE);

            Notes.setTextSize(20);
            Notes.setTextColor(Color.WHITE);

            myTask.getAutoSizeMaxTextSize();

            myTask.setMovementMethod(new ScrollingMovementMethod());
            Notes.setMovementMethod(new ScrollingMovementMethod());

            myTask.setVisibility(View.VISIBLE);
            Notes.setVisibility(View.VISIBLE);

            view.setClickable(true);

            if (counter.equals("00:00")){
                startButton.setEnabled(false);
            }
            _switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){ // on
                        view.setAlpha(0.3f);
                    } else { // off
                        view.setAlpha(1);
                    }
                }
            });

            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startButton.setEnabled(false);
                    stopButton.setEnabled(true);

                    String minute = counter.getText().toString().split(":")[0]; // dakika
                    String second = counter.getText().toString().split(":")[1]; // saniye

                    try {
                        if(second.equals("")){
                            second="0";
                        }
                        int value = Integer.parseInt(minute.trim())* 60 * 1000+ Integer.parseInt(second.trim()) * 1000; // zaman değeri

                        String finalSecond = second;
                        CountDownTimer countDownTime = new CountDownTimer(value ,1000){
                            @SuppressLint("SetTextI18n")
                            public void onTick(long millisUntilFinished){

                                long time = millisUntilFinished / 1000;

                                long minuteValue =  time / 60;
                                long secondValue =  time % 60;

                                if (minuteValue < 10 && secondValue < 10){
                                    counter.setText( "0"+ minuteValue + " : 0" + secondValue);
                                } else if(minuteValue < 10 ){
                                    counter.setText( "0"+ minuteValue + " : " + secondValue);
                                } else if ( secondValue < 10){
                                    counter.setText(minuteValue + " : 0" + secondValue);
                                } else {
                                    counter.setText(minuteValue + " : " + secondValue);
                                }
//                                                                SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//                                ContentValues updatedValues = new ContentValues();
//                                ContentValues updatedValues1 = new ContentValues();
//
//                                updatedValues.put(DatabaseHelper.COLUMN_MINUTES, 0);
//                                updatedValues1.put(DatabaseHelper.COLUMN_SECONDS, 0);
//
//                                String whereClause = DatabaseHelper.COLUMN_MINUTES + " = ?";
//                                String whereClause1 = DatabaseHelper.COLUMN_SECONDS + " = ?";
//
//                                String[] whereArgs = {minute};
//                                String[] whereArgs1 = {finalSecond};
//
//                                int rowsUpdated = db.update(DatabaseHelper.TABLE_NAME, updatedValues, whereClause, whereArgs);
//                                int rowsUpdated1 = db.update(DatabaseHelper.TABLE_NAME, updatedValues1, whereClause1, whereArgs1);
//
//                                if (rowsUpdated > 0 && rowsUpdated1 > 0) {
//                                    // Başarılı bir şekilde güncellendi
//                                    Toast.makeText(TodoListActivity.this, "Görev güncellendi.", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    // Hata oluştu
//                                    //Toast.makeText(TodoListActivity.this, "Görev güncellenirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
//                                }
//
//                                // Veritabanı bağlantısını kapat
//                                db.close();
                            }

                            @Override
                            public void onFinish() {
                                startButton.setEnabled(false);
                                alertSound(); //  ses
                                alertVib(); // titreşim

                                SQLiteDatabase db = dbHelper.getWritableDatabase();

                                ContentValues updatedValues = new ContentValues();
                                ContentValues updatedValues1 = new ContentValues();

                                updatedValues.put(DatabaseHelper.COLUMN_MINUTES, 0);
                                updatedValues1.put(DatabaseHelper.COLUMN_SECONDS, 0);

                                String whereClause = DatabaseHelper.COLUMN_MINUTES + " = ?";
                                String whereClause1 = DatabaseHelper.COLUMN_SECONDS + " = ?";

                                String[] whereArgs = {minutesComesDb};
                                String[] whereArgs1 = {secondsComesDb};

                                int rowsUpdated = db.update(DatabaseHelper.TABLE_NAME, updatedValues, whereClause, whereArgs);
                                int rowsUpdated1 = db.update(DatabaseHelper.TABLE_NAME, updatedValues1, whereClause1, whereArgs1);

                                if (rowsUpdated > 0 && rowsUpdated1 > 0) {
                                    // Başarılı bir şekilde güncellendi
                                    Toast.makeText(TodoListActivity.this, "Görev güncellendi.", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Hata oluştu
                                    Toast.makeText(TodoListActivity.this, "Görev güncellenirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
                                }

                                // Veritabanı bağlantısını kapat
                                db.close();
                                counter.setText("00:00");

                            }

                        }.start();


                        stopButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                startButton.setEnabled(true);
                                resetButton.setEnabled(true);
                                countDownTime.cancel();

                                SQLiteDatabase db = dbHelper.getWritableDatabase();

                                ContentValues updatedValues = new ContentValues();
                                ContentValues updatedValues1 = new ContentValues();

                                updatedValues.put(DatabaseHelper.COLUMN_MINUTES, String.valueOf(counter.getText().toString().split(":")[0]));
                                updatedValues1.put(DatabaseHelper.COLUMN_SECONDS, String.valueOf(counter.getText().toString().split(":")[1]));

                                String whereClause = DatabaseHelper.COLUMN_MINUTES + " = ?";
                                String whereClause1 = DatabaseHelper.COLUMN_SECONDS + " = ?";

                                String[] whereArgs = {minutesComesDb};
                                String[] whereArgs1 = {secondsComesDb};

                                int rowsUpdated = db.update(DatabaseHelper.TABLE_NAME, updatedValues, whereClause, whereArgs);
                                int rowsUpdated1 = db.update(DatabaseHelper.TABLE_NAME, updatedValues1, whereClause1, whereArgs1);

                                if (rowsUpdated > 0 && rowsUpdated1 > 0) {
                                    // Başarılı bir şekilde güncellendi
                                    Toast.makeText(TodoListActivity.this, "Görev güncellendi.", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Hata oluştu
                                    Toast.makeText(TodoListActivity.this, "Görev güncellenirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
                                }

                                // Veritabanı bağlantısını kapat
                                db.close();
                            }
                        });

                        resetButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                countDownTime.cancel();
                                stopButton.setEnabled(false);
                                startButton.setEnabled(false);

                                SQLiteDatabase db = dbHelper.getWritableDatabase();

                                ContentValues updatedValues = new ContentValues();
                                ContentValues updatedValues1 = new ContentValues();

                                updatedValues.put(DatabaseHelper.COLUMN_MINUTES, 0);
                                updatedValues1.put(DatabaseHelper.COLUMN_SECONDS, 0);

                                String whereClause = DatabaseHelper.COLUMN_MINUTES + " = ?";
                                String whereClause1 = DatabaseHelper.COLUMN_SECONDS + " = ?";

                                String[] whereArgs = {minutesComesDb};
                                String[] whereArgs1 = {secondsComesDb};

                                int rowsUpdated = db.update(DatabaseHelper.TABLE_NAME, updatedValues, whereClause, whereArgs);
                                int rowsUpdated1 = db.update(DatabaseHelper.TABLE_NAME, updatedValues1, whereClause1, whereArgs1);

                                if (rowsUpdated > 0 && rowsUpdated1 > 0) {
                                    // Başarılı bir şekilde güncellendi
                                    Toast.makeText(TodoListActivity.this, "Görev güncellendi.", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Hata oluştu
                                    Toast.makeText(TodoListActivity.this, "Görev ellenirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
                                }

                                // Veritabanı bağlantısını kapat
                                db.close();

                                counter.setText("00:00");
                            }
                        });


                    } catch (Exception e){
                        Toast.makeText(TodoListActivity.this,"You didn't give the time correctly",Toast.LENGTH_LONG).show();
                    }
                }
            });


            resetButton.setOnClickListener(new View.OnClickListener() { // baslamadan önce sıfırlamak
                @Override
                public void onClick(View v) {
                    stopButton.setEnabled(false);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    ContentValues updatedValues = new ContentValues();
                    ContentValues updatedValues1 = new ContentValues();

                    updatedValues.put(DatabaseHelper.COLUMN_MINUTES, 0);
                    updatedValues1.put(DatabaseHelper.COLUMN_SECONDS, 0);

                    String whereClause = DatabaseHelper.COLUMN_MINUTES + " = ?";
                    String whereClause1 = DatabaseHelper.COLUMN_SECONDS + " = ?";

                    String[] whereArgs = {minutesComesDb};
                    String[] whereArgs1 = {secondsComesDb};

                    int rowsUpdated = db.update(DatabaseHelper.TABLE_NAME, updatedValues, whereClause, whereArgs);
                    int rowsUpdated1 = db.update(DatabaseHelper.TABLE_NAME, updatedValues1, whereClause1, whereArgs1);

                    if (rowsUpdated > 0 && rowsUpdated1 > 0) {
                        // Başarılı bir şekilde güncellendi
                        Toast.makeText(TodoListActivity.this, "Görev güncellendi.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Hata oluştu
                        Toast.makeText(TodoListActivity.this, "Görev ellenirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
                    }

                    // Veritabanı bağlantısını kapat
                    db.close();


                    counter.setText("00:00");


                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog = new Dialog(TodoListActivity.this);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setContentView(R.layout.layout_move);
                    dialog.show();

                    EditText editTaskToAddList = dialog.findViewById(R.id.editTask); // layout_movedaki edittextler
                    EditText editNotesToAddList = dialog.findViewById(R.id.editNotes);

                    String previousTask = myTask.getText().toString();
                    String previousNotes = Notes.getText().toString();

                    editTaskToAddList.setText(previousTask); // layout_movedaki edittextlere normal olan notlar atanır sonrasında
                    editNotesToAddList.setText(previousNotes);

                    Button editButton = dialog.findViewById(R.id.editButton);
                    Button deleteButton = dialog.findViewById(R.id.deleteButton);

                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String editedTask = editTaskToAddList.getText().toString(); // layout_movedaki edittextlere normal olan notlar atanır sonrasında string valueları alınır.
                            String editedNotes = editNotesToAddList.getText().toString();

                            myTask.setText(editedTask);
                            myTask.setPadding(16, 5, 10, 10);

                            Notes.setText(editedNotes);
                            Notes.setPadding(16, 5, 10, 10);

                            _switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if (isChecked){ // on
                                        view.setAlpha(0.3f);
                                    } else { // off
                                        view.setAlpha(1);
                                    }
                                }
                            });

                            // Veritabanını güncelle
                            SQLiteDatabase db = dbHelper.getWritableDatabase();

                            ContentValues updatedValues = new ContentValues();
                            updatedValues.put(DatabaseHelper.COLUMN_TASK, editedTask);
                            updatedValues.put(DatabaseHelper.COLUMN_NOTES, editedNotes);

                            String whereClause = DatabaseHelper.COLUMN_TASK + " = ?";
                            String[] whereArgs = {previousTask};

                            int rowsUpdated = db.update(DatabaseHelper.TABLE_NAME, updatedValues, whereClause, whereArgs);

                            if (rowsUpdated > 0) {
                                // Başarılı bir şekilde güncellendi
                                Toast.makeText(TodoListActivity.this, "Görev güncellendi.", Toast.LENGTH_SHORT).show();
                            } else {
                                // Hata oluştu
                                Toast.makeText(TodoListActivity.this, "Görev güncellenirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
                            }

                            // Veritabanı bağlantısını kapat
                            db.close();

                            Toast.makeText(TodoListActivity.this, "Your task was edited", Toast.LENGTH_LONG).show();
                            dialog.hide();
                        }
                    });

                    deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.hide();
                        }
                    });


                }
            });
            taskListLayout.addView(view);

        }

        // Cursor ve veritabanı bağlantısını kapat
        cursor.close();
        db.close();

    }

    public void clearTable() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String tableName = DatabaseHelper.TABLE_NAME; // Temizlenecek tablo adı

        db.delete(tableName, null, null);
        db.close();
    }
}
