package com.example.mysanko;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
TextView my,sanko,kanri;
Button tap;
int sound;
SoundPool soundPool;
Context context = MainActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        my = findViewById(R.id.my);
        sanko = findViewById(R.id.sanko);
        kanri = findViewById(R.id.kanri);
        tap = findViewById(R.id.tap);
        soundPool = new SoundPool(2,AudioManager.STREAM_MUSIC,0);
        sound = soundPool.load(MainActivity.this,R.raw.tap,1);
        tap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                soundPool.play(sound, 1f, 1f, 0, 0, 1f);

                Intent intent = new Intent(MainActivity.this, my_timeLimit.class);
                startActivity(intent);

            }
        });



    }
}