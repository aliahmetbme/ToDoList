package com.example.todolist;

import static com.example.todolist.R.drawable.btn_background;
import static com.example.todolist.R.drawable.madde;
import static com.example.todolist.R.drawable.rounded;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.os.Bundle;
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

import androidx.appcompat.app.AppCompatActivity;

public class TodoListActivity extends AppCompatActivity {
    static final int[] count = {0};
    @Override
    protected void onCreate(Bundle savedInstanceState ){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        Button addList = findViewById(R.id.addList);
        TextView textView = findViewById(R.id.textView);
        Button completed = findViewById(R.id.completed);

        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRemoveClick();
            }
        });


        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setVisibility(View.INVISIBLE);
                onAddButtonClick(view);
            }
});}
    public void onAddButtonClick(View view) {

        View customView = LayoutInflater.from(TodoListActivity.this).inflate(R.layout.layout_add,null);
        Dialog scaleOptionDialog  = new Dialog(TodoListActivity.this);
        scaleOptionDialog.setContentView(customView);
        scaleOptionDialog.show();

        Button _addButton = customView.findViewById(R.id.addButton);
        Button cancelButton = customView.findViewById(R.id.cancelButton);

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
                taskColor[0] =  "#8BC34A";
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
                _textView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(taskColor[0])));
                int width;

                if (LinearLayout.LayoutParams.WRAP_CONTENT >= 900) {
                    width = LinearLayout.LayoutParams.WRAP_CONTENT;
                } else {
                    width = 900;
                }

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        width, // genişlik
                        LinearLayout.LayoutParams.WRAP_CONTENT // yükseklik
                );
                layoutParams.setMargins(8,8,8,10);
                _textView.setLayoutParams(layoutParams);
                count[0]++;
                _textView.setTextSize(28);
                _textView.setText(String.valueOf(count[0])+" ----> "+job);

                editText.setText(""); // add yapınca sıfırlanması için


                textView.setVisibility(View.VISIBLE);
                list.addView(_textView);

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scaleOptionDialog.hide();
            }
        });
    }

    public void onRemoveClick() {

        LinearLayout list = findViewById(R.id.list);

        View customView = LayoutInflater.from(TodoListActivity.this).inflate(R.layout.layout_move,null);
        Dialog scaleOptionDialog  = new Dialog(TodoListActivity.this);
        scaleOptionDialog.setContentView(customView);
        scaleOptionDialog.show();

        Button removeButton = customView.findViewById(R.id.button2);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = customView.findViewById(R.id.numberCompleted);
                int count = Integer.parseInt(editText.getText().toString().trim());
                View child = list.getChildAt((count - 1));
                list.removeView(child);
                Toast.makeText(TodoListActivity.this,"You completed the task congratulations", Toast.LENGTH_LONG).show();
                scaleOptionDialog.hide();
            }
        });




    }


}
