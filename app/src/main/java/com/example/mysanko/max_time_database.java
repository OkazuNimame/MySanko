package com.example.mysanko;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class max_time_database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "maxTime_sanko.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "maxTime";
    public static final String maxT = "maxT";
    public static final String _ID = "_id";
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    maxT + " INTEGER);";
    public max_time_database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public void saveWord(int newWord) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(maxT, newWord);

        // 一番目の行のデータを取得
        Cursor cursor = db.query(
                TABLE_NAME,
                new String[]{_ID, maxT},
                null,
                null,
                null,
                null,
                null,
                "1"
        );

        if (cursor != null && cursor.moveToFirst()) {
            // 一番目の行が存在する場合は更新
            long existingRowId = cursor.getLong(cursor.getColumnIndexOrThrow(_ID));
            db.update(
                    TABLE_NAME,
                    values,
                    _ID + " = ?",
                    new String[]{String.valueOf(existingRowId)}
            );
        } else {
            // 一番目の行が存在しない場合は挿入
            db.insert(
                    TABLE_NAME,
                    null,
                    values
            );
        }

        if (cursor != null) {
            cursor.close();
        }

        db.close();
    }

}
