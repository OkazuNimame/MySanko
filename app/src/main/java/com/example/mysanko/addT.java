package com.example.mysanko;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

public class addT extends AppCompatActivity {
NumberPicker month,day,time;
Button ok;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_t);

        month = findViewById(R.id.month);
        day = findViewById(R.id.day);
        time = findViewById(R.id.time);
        ok = findViewById(R.id.ok);


        month.setTextSize(100);
        month.setMinValue(1);
        month.setMaxValue(12);

        day.setTextSize(100);
        day.setMinValue(1);
        day.setMaxValue(31);

        time.setTextSize(100);
        time.setMinValue(1);
        time.setMaxValue(50);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int timeNum = time.getValue();
                int monthNum = month.getValue();
                String monthS = String.valueOf(monthNum);
                int dayNum = day.getValue();
                String dayS = String.valueOf(dayNum);



                time_database timeDatabase = new time_database(addT.this);
                timeDatabase.saveData(monthS,dayS,timeNum);

                Intent intent = new Intent(addT.this, my_timeLimit.class);
                startActivity(intent);


            }
        });


    }
}