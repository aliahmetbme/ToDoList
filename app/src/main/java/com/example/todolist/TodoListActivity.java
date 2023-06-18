package com.example.todolist;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;



public class TodoListActivity extends AppCompatActivity {

    Vibrator vibrator = null;
    MediaPlayer mediaPlayer = null;
    DatabaseHelper dbHelper ;
    ContentValues values = new ContentValues();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        Button addList = findViewById(R.id.addList);
        dbHelper = new DatabaseHelper(this);
        getTasks();

        // adding new tasks
        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTask();
            }
        });
    }

    // adding new tasks
    public void addTask(){

        View customView = LayoutInflater.from(TodoListActivity.this).inflate(R.layout.layout_add, null);
        Dialog scaleOptionDialog = new Dialog(TodoListActivity.this);
        scaleOptionDialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corners);
        scaleOptionDialog.setContentView(customView);
        scaleOptionDialog.show();

        Button cancel = customView.findViewById(R.id.cancelButton);
        Button _addButton = customView.findViewById(R.id.addButton);

        LinearLayout taskList = findViewById(R.id.list);

        LayoutInflater inflater = LayoutInflater.from(this);  // LayoutInflater oluşturun
        View view = inflater.inflate(R.layout.task,taskList,false); // XML dosyasını LinearLayout'a ekle

        EditText taskToAddList = customView.findViewById(R.id.organiseTask);
        EditText notesToAddList = customView.findViewById(R.id.organiseNotes);

        EditText minuteAdd = customView.findViewById(R.id.minute);
        EditText secondAdd = customView.findViewById(R.id.second);

        TextView newTask = view.findViewById(R.id.myTask);
        TextView newNote = view.findViewById(R.id.notes);
        ImageButton startButton = view.findViewById(R.id.timerStar);
        ImageButton stopButton = view.findViewById(R.id.timerStop);
        ImageButton resetButton = view.findViewById(R.id.timerReset);
        TextView counter = view.findViewById(R.id.counter);

        if (counter.equals("00:00")) {
            startButton.setEnabled(false);
        }

        // ImageView imageView = view.findViewById(R.id.taskImage);
        Switch x = view.findViewById(R.id.switch1);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButton.setEnabled(false);
                stopButton.setEnabled(true);


                String minute = counter.getText().toString().split(":")[0];
                String second = counter.getText().toString().split(":")[1];
                //Toast.makeText(TodoListActivity.this ,  counter.getText().toString() + " caunt " ,Toast.LENGTH_LONG).show();


                try {
                    if(second.equals("")){
                        second="0";
                    }
                    int value = Integer.parseInt(minute.trim())* 60 * 1000+ Integer.parseInt(second.trim()) * 1000;

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

                        }

                        @Override
                        public void onFinish() {
                            counter.setText("00:00");
                            startButton.setEnabled(true);
                            alertSound();
                            alertVib();
                        }

                    }.start();


                    stopButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startButton.setEnabled(true);
                            resetButton.setEnabled(true);
                            countDownTime.cancel();
                        }
                    });

                    resetButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            countDownTime.cancel();
                            stopButton.setEnabled(false);
                            counter.setText("00:00");
                        }
                    });


                } catch (Exception e){
                    Toast.makeText(TodoListActivity.this,"You didn't give the time correctly",Toast.LENGTH_LONG).show();
                }
            }
        });

        _addButton.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                if (newTask.equals("")) {

                    Toast.makeText(TodoListActivity.this, "Please add your task", Toast.LENGTH_LONG).show();

                } else {
                    int minuteValue = 0;
                    int secondValue = 0;

                    String task = taskToAddList.getText().toString() ;
                    String notes = notesToAddList.getText().toString();

                    newTask.setText(task);
                    newTask.setPadding(16,5,10,10);

                    newNote.setText(notes);
                    newNote.setPadding(16,5,10,10);

                    try {

                        minuteValue = Integer.parseInt(minuteAdd.getText().toString());
                        secondValue = Integer.parseInt(secondAdd.getText().toString());

                        if (secondValue > 60){
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

                    x.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked){
                              //  view.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF3700B3")));
                                view.setAlpha(0.3f);
                                x.setAlpha(1);

                            } else {
                                view.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF03DAC5")));
                                view.setAlpha(1);

                            }
                        }
                    });

                    values.put(DatabaseHelper.COLUMN_TASK, newTask.getText().toString());
                    values.put(DatabaseHelper.COLUMN_NOTES, newNote.getText().toString());
                    values.put(DatabaseHelper.COLUMN_MINUTES, minuteAdd.getText().toString() );
                    values.put(DatabaseHelper.COLUMN_SECONDS, secondAdd.getText().toString());

                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    long id = db.insert(DatabaseHelper.TABLE_NAME, null, values);

                    if (id != -1) {
                        // Başarılı bir şekilde eklendi
                        Toast.makeText(TodoListActivity.this, "Görev eklendi.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Hata oluştu
                        Toast.makeText(TodoListActivity.this, "Görev eklenirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
                    }

                    // Veritabanı bağlantısını kapat
                    db.close();

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            View customView = LayoutInflater.from(TodoListActivity.this).inflate(R.layout.layout_move, null);
                            Dialog move = new Dialog(TodoListActivity.this);
                            move.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corners);
                            move.setContentView(customView);
                            move.show();

                            EditText editTaskToAddList = customView.findViewById(R.id.editTask);
                            EditText editNotesToAddList = customView.findViewById(R.id.editNotes);

                            String previousTask = newTask.getText().toString();
                            String previousNotes = newNote.getText().toString();

                            editTaskToAddList.setText(previousTask);
                            editNotesToAddList.setText(previousNotes);

                            Button editButton = customView.findViewById(R.id.editButton);
                            Button deleteButton = customView.findViewById(R.id.deleteButton);

                            editButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertSound();
                                    alertVib();

                                    String editedTask  = editTaskToAddList.getText().toString();
                                    String editedNotes = editNotesToAddList.getText().toString();

                                    newTask.setText(editedTask);
                                    newTask.setPadding(16,5,10,10);

                                    newNote.setText(editedNotes);
                                    newNote.setPadding(16,5,10,10);
                                    Toast.makeText(TodoListActivity.this, " Your task was edited ", Toast.LENGTH_LONG).show();
                                    move.hide();
                                }
                            });

                            deleteButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(TodoListActivity.this, newTask.getText().toString() + " were remoted", Toast.LENGTH_LONG).show();
                                    taskList.removeView(view);
                                    move.hide();
                                }
                            });
                        }
                    });

                    scaleOptionDialog.hide();
                }

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        scaleOptionDialog.hide();
                    }
                });
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
        vibrator.vibrate(VibrationEffect.createOneShot(10000,VibrationEffect.DEFAULT_AMPLITUDE));
    }

    public  void getTasks(){

        List<Task> taskList = new ArrayList<>();

        // Veritabanından görevleri sorgula
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME, null, null, null, null, null, null);

        LinearLayout taskListLayout = findViewById(R.id.list);
        taskListLayout.removeAllViews();

            // Cursor'da kayıt varsa
        while (cursor.moveToNext()){
                // Kayıttaki sütunları
            @SuppressLint("Range") String task = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK));
            @SuppressLint("Range") String notes = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NOTES));
            @SuppressLint("Range") String minutes = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_MINUTES));
            @SuppressLint("Range") String seconds = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_SECONDS));
            // Task nesnesini oluştur ve listeye ekle

            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.task, taskListLayout, false);

            TextView myTask = view.findViewById(R.id.myTask);
            TextView Notes = view.findViewById(R.id.notes);
            TextView counter = view.findViewById(R.id.counter);

            myTask.setText(task);
            Notes.setText(notes);
            counter.setText(minutes + " : " + seconds);

            myTask.setPadding(16, 5, 10, 10);
            Notes.setPadding(16, 5, 10, 10);

            view.setClickable(true);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View customView = LayoutInflater.from(TodoListActivity.this).inflate(R.layout.layout_move, null);
                    Dialog move = new Dialog(TodoListActivity.this);
                    move.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corners);
                    move.setContentView(customView);
                    move.show();

                    EditText editTaskToAddList = customView.findViewById(R.id.editTask);
                    EditText editNotesToAddList = customView.findViewById(R.id.editNotes);

                    String previousTask = myTask.getText().toString();
                    String previousNotes = Notes.getText().toString();

                    editTaskToAddList.setText(previousTask);
                    editNotesToAddList.setText(previousNotes);

                    Button editButton = customView.findViewById(R.id.editButton);
                    Button deleteButton = customView.findViewById(R.id.deleteButton);

                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String editedTask = editTaskToAddList.getText().toString();
                            String editedNotes = editNotesToAddList.getText().toString();

                            myTask.setText(editedTask);
                            myTask.setPadding(16, 5, 10, 10);

                            Notes.setText(editedNotes);
                            Notes.setPadding(16, 5, 10, 10);

                            Toast.makeText(TodoListActivity.this, "Your task was edited", Toast.LENGTH_LONG).show();
                            move.hide();
                        }
                    });

                    deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(TodoListActivity.this, myTask.getText().toString() + " were removed", Toast.LENGTH_LONG).show();
                            taskListLayout.removeView(view);
                            move.hide();
                            taskList.remove(task);
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
}
