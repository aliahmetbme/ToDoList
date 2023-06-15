package com.example.todolist;

import static com.example.todolist.R.drawable.madde;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

public class TodoListActivity extends AppCompatActivity {
    static final int[] count = {0};
    static boolean complete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        Button addList = findViewById(R.id.addList);
        TextView textView = findViewById(R.id.textView);


        // adding new tasks
        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTask();
            }
        });



    }

    // adding new tasks
    public void onAddButtonClick(View view) {

        View customView = LayoutInflater.from(TodoListActivity.this).inflate(R.layout.layout_add, null);
        Dialog scaleOptionDialog = new Dialog(TodoListActivity.this);
        scaleOptionDialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corners);
        scaleOptionDialog.setContentView(customView);
        scaleOptionDialog.show();

        Button _addButton = customView.findViewById(R.id.addButton);
        Button cancelButton = customView.findViewById(R.id.cancelButton);


        _addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                TextView _textView = new TextView(TodoListActivity.this);
                EditText editText = customView.findViewById(R.id.organiseTask);

                TextView textView = findViewById(R.id.textView);
                LinearLayout list = findViewById(R.id.list);


                String job = editText.getText().toString();
                _textView.setTextColor(Color.parseColor("#000000"));
                _textView.setBackground(getDrawable(madde));



                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, // genişlik
                        LinearLayout.LayoutParams.WRAP_CONTENT // yükseklik
                );
                layoutParams.setMargins(8, 25, 8, 1);
                layoutParams.gravity = Gravity.CENTER;
                _textView.setPadding(50, 1, 1, 1);
                _textView.setLayoutParams(layoutParams);
                count[0]++;
                if (complete) count[0] = list.getChildCount() + 1;
                complete = false;
                _textView.setTextSize(24);
                String task = String.valueOf(count[0]) + " ---> " + job;
                if (job.equals("")) {
                    Toast.makeText(TodoListActivity.this, "Please add your task", Toast.LENGTH_LONG).show();
                } else {
                    _textView.setText(task);
                    editText.setText(""); // add yapınca sıfırlanması için
                    textView.setVisibility(View.VISIBLE);



                    list.addView(_textView);
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = findViewById(R.id.textView);
                textView.setVisibility(View.VISIBLE);
                scaleOptionDialog.hide();
            }
        });
    }

    public void addTask(){

        View customView = LayoutInflater.from(TodoListActivity.this).inflate(R.layout.layout_add, null);
        Dialog scaleOptionDialog = new Dialog(TodoListActivity.this);
        scaleOptionDialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corners);
        scaleOptionDialog.setContentView(customView);
        scaleOptionDialog.show();

        Button delete = customView.findViewById(R.id.cancelButton);
        Button _addButton = customView.findViewById(R.id.addButton);

        LinearLayout taskList = findViewById(R.id.list);

        LayoutInflater inflater = LayoutInflater.from(this);  // LayoutInflater oluşturun
        View view = inflater.inflate(R.layout.task,taskList,false); // XML dosyasını LinearLayout'a ekle

        EditText taskToAddList = customView.findViewById(R.id.organiseTask);
        EditText notesToAddList = customView.findViewById(R.id.organiseNotes);


        TextView newTask = view.findViewById(R.id.myTask);
        TextView newNote = view.findViewById(R.id.notes);
        ImageView imageView = view.findViewById(R.id.taskImage);
        Switch x = view.findViewById(R.id.switch1);

        _addButton.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                if (newTask.equals("")) {
                    Toast.makeText(TodoListActivity.this, "Please add your task", Toast.LENGTH_LONG).show();
                } else {
                    String task =   taskToAddList.getText().toString() ;
                    String notes = notesToAddList.getText().toString();

                    newTask.setText(task);
                    newTask.setPadding(16,5,10,10);

                    newNote.setText(notes);
                    newNote.setPadding(16,5,10,10);

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

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            View customView = LayoutInflater.from(TodoListActivity.this).inflate(R.layout.layout_move, null);
                            Dialog scaleOptionDialog = new Dialog(TodoListActivity.this);
                            scaleOptionDialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corners);
                            scaleOptionDialog.setContentView(customView);
                            scaleOptionDialog.show();

                        }
                    });

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(TodoListActivity.this,"Hello you clicked me",Toast.LENGTH_LONG).show();
                        }
                    });

                    scaleOptionDialog.hide();

                }
            }


            // taking screenShoot
            public void onCapture() {

                LinearLayout list = findViewById(R.id.list);

                list.setDrawingCacheEnabled(true);
                Bitmap listBitmap = Bitmap.createBitmap(list.getDrawingCache());

                Canvas combinedCanvas = new Canvas(listBitmap);
                combinedCanvas.drawBitmap(listBitmap, 0, 0, null);

                saveImage(listBitmap);
                list.setDrawingCacheEnabled(false);
            }

            protected void saveImage(Bitmap bitmap) {

                SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");

                String folderName = "DCIM/TO-DO-LIST";

                File folder = new File(Environment.getExternalStorageDirectory() + "/" + folderName);

                if (!folder.exists()) {
                    folder.mkdirs();
                }

                // Naming image
                String fileName = String.format("%s_Your_List.png", sdf);

                // saving photo
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
                    values.put(MediaStore.Images.Media.RELATIVE_PATH, folderName);
                    Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    try (OutputStream outputStream = getContentResolver().openOutputStream(uri)) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                        Toast.makeText(TodoListActivity.this, " Your plan is saved ", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(TodoListActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            }


//            // requesting
//            @Override
//            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults) {
//                super.onRequestPermissionsResult(requestCode,permission,grantResults);
//
//                if (requestCode == 1) {
//                    if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                        Intent photo = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                        startActivityForResult(photo,2);
//                    }
//                }
//            }
       });}}
