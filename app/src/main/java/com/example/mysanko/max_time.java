package com.example.mysanko;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class max_time extends AppCompatActivity {
NumberPicker numberPicker;
Button kettei,deleteButton;
ImageButton gakuen;
    private int[] imageArray = {R.drawable.mirai_ai, R.drawable.buraidaru, R.drawable.kodomo,R.drawable.suportu,R.drawable.wedhingu};
    private int currentImageIndex = 0;
private List<String> savedFileNames = new ArrayList<>();
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

        gakuen = findViewById(R.id.gakuen);
        int maxnumber = numberPicker.getValue();

        SoundPool soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        int sound = soundPool.load(max_time.this,R.raw.kettei,1);

        SoundPool soundPool2 = new SoundPool(2,AudioManager.STREAM_MUSIC,0);
        int sound2 = soundPool2.load(max_time.this,R.raw.kyanseru,0);

        kettei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int maxnumber = numberPicker.getValue();
                Intent intent = new Intent(max_time.this,my_timeLimit.class);
                max_time_database maxTimeDatabase = new max_time_database(max_time.this);
                maxTimeDatabase.saveWord(maxnumber);
                startActivity(intent);
                soundPool.play(sound, 1f, 1f, 0, 0, 1f);
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(max_time.this, my_timeLimit.class);
                deleteBreakDuration();
                startActivity(intent);
                soundPool2.play(sound2, 1f, 1f, 0, 0, 1f);
                finish();

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


