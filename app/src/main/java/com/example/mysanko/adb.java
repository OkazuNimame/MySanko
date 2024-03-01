package com.example.mysanko;

import static com.example.mysanko.time_database.TABLE_NAME;
import static com.example.mysanko.time_database._ID;
import static com.example.mysanko.time_database.break_duration;
import static com.example.mysanko.time_database.date;
import static com.example.mysanko.time_database.date2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.transition.CircularPropagation;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

public class adb extends AppCompatActivity {

    NumberPicker month,day,time;
    Button save,back,delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adb);

        time_database timeDatabase = new time_database(this);
        SQLiteDatabase db = timeDatabase.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,new String[]{date,date2,break_duration},null,null,null,null,null);
        long itemId = getIntent().getLongExtra("time", 0);
        int position = (int) itemId - 1;
        //Toast.makeText(adb.this,String.valueOf(position),Toast.LENGTH_SHORT).show();
        if(cursor != null && cursor.moveToPosition(position)) {
            month = findViewById(R.id.month);
            day = findViewById(R.id.day);
            time = findViewById(R.id.time);

            save = findViewById(R.id.save);
            back = findViewById(R.id.back);
            delete = findViewById(R.id.delete);

            month.setMinValue(1);
            month.setMaxValue(12);
            String monthI = cursor.getString(cursor.getColumnIndexOrThrow(date));
            int newMonthI = Integer.parseInt(monthI);
            month.setValue(newMonthI);


            day.setMinValue(1);
            day.setMaxValue(31);
            String dayI = cursor.getString(cursor.getColumnIndexOrThrow(date2));
            int newDayI = Integer.parseInt(dayI);
            day.setValue(newDayI);

            time.setMinValue(1);
            time.setMaxValue(60);
            int timeI = cursor.getInt(cursor.getColumnIndexOrThrow(break_duration));
            time.setValue(timeI);

            //Toast.makeText(adb.this, "Month: " + monthI + ", Day: " + dayI + ", Time: " + timeI, Toast.LENGTH_SHORT).show();


            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int monthNum = month.getValue();
                    String monthS = String.valueOf(monthNum);
                    int dayNum = day.getValue();
                    String dayS = String.valueOf(dayNum);
                    int timeNum = time.getValue();
                    int newPosition = position + 1;
                    timeDatabase.updateData(newPosition,monthS,dayS,timeNum);
                    setResult(RESULT_OK);

                    finish();

                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int newPosition = position + 1;
                    deleteData(newPosition);
                    Intent intent = new Intent(adb.this, my_timeLimit.class);
                    startActivity(intent);
                    setResult(RESULT_OK);
                    finish();
                }
            });

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(adb.this,my_timeLimit.class);
                    startActivity(intent);
                    finish();
                }
            });

        }
    }
    public void deleteData(int position) {
        time_database database = new time_database(adb.this);
        SQLiteDatabase db = database.getWritableDatabase();

        // データベースから該当のデータを削除
        db.delete(TABLE_NAME, _ID + "=?", new String[]{String.valueOf(position)});
        String updateQuery = "UPDATE " + TABLE_NAME + " SET " + _ID + " = " + _ID + " - 1" +
                " WHERE " + _ID + " > " + position;
        db.execSQL(updateQuery);

        db.close();


    }
}

