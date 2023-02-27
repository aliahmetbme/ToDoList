package com.example.todolist;

import static com.example.todolist.R.drawable.btn_background;
import static com.example.todolist.R.drawable.madde;
import static com.example.todolist.R.drawable.rounded;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TodoListActivity extends AppCompatActivity {
    static final int[] count = {0};
    static boolean complete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        Button addList = findViewById(R.id.addList);
        TextView textView = findViewById(R.id.textView);
        Button completed = findViewById(R.id.completed);
        Button save = findViewById(R.id.button3);

        // completing tasks
        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRemoveClick();
            }
        });

        // adding new tasks
        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setVisibility(View.INVISIBLE);
                onAddButtonClick(view);
            }
        });

        // saving to gallery
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCapture();
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

        // to give colors to tasks
        Button redButton = customView.findViewById(R.id.red);
        Button yesilButton = customView.findViewById(R.id.yesil);
        Button orangeButton = customView.findViewById(R.id.orange);
        Button yellowButton = customView.findViewById(R.id.yellow);
        Button blueButton = customView.findViewById(R.id.blue);
        Button sonrenkButton = customView.findViewById(R.id.sonrenk);

        final String[] taskColor = new String[1];
        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskColor[0] = "#F81212";
            }
        });

        yesilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskColor[0] = "#8BC34A";
            }
        });

        orangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskColor[0] = "#FF5722";
            }
        });

        yellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskColor[0] = "#FFEB3B";
            }
        });

        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskColor[0] = "#00BCD4";
            }
        });

        sonrenkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskColor[0] = "#FF5722";
            }
        });

        _addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                TextView _textView = new TextView(TodoListActivity.this);
                EditText editText = customView.findViewById(R.id.addTask);

                TextView textView = findViewById(R.id.textView);
                LinearLayout list = findViewById(R.id.list);


                String job = editText.getText().toString();
                _textView.setTextColor(Color.parseColor("#000000"));
                _textView.setBackground(getDrawable(madde));

                if (taskColor[0] == null) {
                    taskColor[0] = "#c6e2ff";
                }

                _textView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(taskColor[0])));

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


    // deleting tasks
    public void onRemoveClick() {

        LinearLayout list = findViewById(R.id.list);

        View customView = LayoutInflater.from(TodoListActivity.this).inflate(R.layout.layout_move, null);
        Dialog scaleOptionDialog = new Dialog(TodoListActivity.this);
        scaleOptionDialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corners);
        scaleOptionDialog.setContentView(customView);
        scaleOptionDialog.show();

        Button removeButton = customView.findViewById(R.id.button2);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = customView.findViewById(R.id.numberCompleted);
                int count;
                try {
                    count = Integer.parseInt(editText.getText().toString().trim());
                    View child = list.getChildAt((count - 1));
                    list.removeView(child);
                    if (list.getChildCount() != 0) {
                        Toast.makeText(TodoListActivity.this, "You completed the task congratulations \n you have " + String.valueOf(list.getChildCount()) + " tasks good luck", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(TodoListActivity.this, "You completed all tasks congratulations \n you can add new tasks", Toast.LENGTH_LONG).show();
                    }

                    int newCount = 1;
                    String newTask;
                    TextView task;

                    // after deleting order tasks again
                    for (int i = 0; i < list.getChildCount(); i++) {
                        task = (TextView) list.getChildAt(i);
                        String[] taskParts = task.getText().toString().split(" ");
                        newTask = String.valueOf(newCount) + " " + taskParts[1] + " " + taskParts[2];
                        task.setText(newTask);
                        newCount++;
                        complete = true;
                    }
                    scaleOptionDialog.hide();

                } catch (java.lang.NumberFormatException e) {
                    Toast.makeText(TodoListActivity.this, "Pleas Enter the task number", Toast.LENGTH_LONG).show();
                }


            }

        });

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
                Toast.makeText(this, " Your plan is saved ", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // requesting
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permission,grantResults);

        if (requestCode == 1) {
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent photo = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(photo,2);
            }
        }
    }
}