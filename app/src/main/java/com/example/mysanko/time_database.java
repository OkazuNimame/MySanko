package com.example.mysanko;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class time_database extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "break_time.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "mySanko";
    public static final String date = "date";
    public static final String date2 = "date2";
    public static final String break_duration = "break_duration";
    public static final String max_duration = "max_duration";
    public static final String _ID = "_id";



    // テーブルの作成クエリ
    private static final String CREATE_TABLE_BREAK_TIME =
            "CREATE TABLE " + TABLE_NAME +  " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    date + " TEXT," + date2 + " TEXT," +
                    break_duration + " INTEGER," +
                    max_duration + " INTEGER)";

    public time_database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BREAK_TIME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void saveData(String month,String day,int time){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(date,month);
        contentValues.put(date2,day);
        contentValues.put(break_duration,time);
        sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        sqLiteDatabase.close();
    }
    public void updateData(int id ,String month,String day,int time){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(date,month);
        contentValues.put(date2,day);
        contentValues.put(break_duration,time);
        db.update(TABLE_NAME, contentValues, _ID + "=?", new String[]{String.valueOf(id)});
        db.close();


    }
}

