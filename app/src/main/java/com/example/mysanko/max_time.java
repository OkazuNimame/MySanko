package com.example.mysanko;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

public class max_time extends AppCompatActivity {
NumberPicker numberPicker;
Button kettei,deleteButton;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_max_time);

        numberPicker = findViewById(R.id.maxTime);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(100);
        numberPicker.setTextSize(200f);

        kettei = findViewById(R.id.kettei);

        deleteButton = findViewById(R.id.deletebutton);
        int maxnumber = numberPicker.getValue();
        kettei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int maxnumber = numberPicker.getValue();
                Intent intent = new Intent(max_time.this,my_timeLimit.class);
                max_time_database maxTimeDatabase = new max_time_database(max_time.this);
                maxTimeDatabase.saveWord(maxnumber);
                startActivity(intent);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(max_time.this, my_timeLimit.class);
                deleteBreakDuration();
                startActivity(intent);

            }
        });


    }
    public void deleteBreakDuration() {
        max_time_database maxTimeDatabase = new max_time_database(max_time.this);
        SQLiteDatabase db = maxTimeDatabase.getReadableDatabase();

        db.delete(
                maxTimeDatabase.TABLE_NAME,              // テーブル名
                maxTimeDatabase.maxT + " IS NOT NULL", // 削除する条件
                null                               // WHERE 句の引数
        );

        db.close();
    }
}
