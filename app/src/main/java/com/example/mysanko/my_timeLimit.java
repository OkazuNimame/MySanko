package com.example.mysanko;

import static android.graphics.Color.*;
import static com.example.mysanko.time_database.DATABASE_NAME;
import static com.example.mysanko.time_database.TABLE_NAME;
import static com.example.mysanko.time_database._ID;
import static com.example.mysanko.time_database.break_duration;
import static com.example.mysanko.time_database.date;
import static com.example.mysanko.time_database.date2;
import static com.example.mysanko.time_database.max_duration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class my_timeLimit extends AppCompatActivity {
    PieChart pieChart;
    BarChart barChart;
    time_database timeDatabase;
    public BarData data;
    int sound;
    private List<BarEntry> entries = new ArrayList<>();
    private List<String> dates = new ArrayList<>();
    private List<Integer> restingTimes = new ArrayList<>();
    private List<Long> yourDatabaseIdList = new ArrayList<>();
Button addbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_time_limit);

        timeDatabase = new time_database(this);

        pieChart = findViewById(R.id.lineChart);
        addbutton = findViewById(R.id.addbutton);
        barChart = findViewById(R.id.horizonatal);


        SoundPool soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        sound = soundPool.load(my_timeLimit.this,R.raw.poin,1);

        getDataFromDatabase();

        // 棒グラフを初期描画
        updateBarChart();
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(my_timeLimit.this, addT.class);
                soundPool.play(sound, 1f, 1f, 0, 0, 1f);

                startActivity(intent);
            }
        });


        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                // クリックされたエントリーのX軸のインデックスを取得
                int selectedIndex = (int) e.getX() ;

                // データベースIDのリストから選択されたインデックスに対応するデータベースIDを取得
                if (selectedIndex >= 0 && selectedIndex < yourDatabaseIdList.size()) {
                    long selectedDatabaseId = yourDatabaseIdList.get(selectedIndex);

                    // タップされたデータベースIDを使って詳細データを表示するIntentを作成
                    int requestCode = 1; // 任意の値
                    Intent intent = new Intent(my_timeLimit.this, adb.class);
                    intent.putExtra("time", selectedDatabaseId);
                    startActivityForResult(intent, requestCode);
                }

            }
            @Override
            public void onNothingSelected() {

            }
        });




        SQLiteDatabase db = timeDatabase.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,new String[]{break_duration},null,null,null,null,null);
            if (cursor != null && cursor.moveToFirst()) {
                int totalBreakDuration = 0;

                do {
                    int breakDuration = cursor.getInt(cursor.getColumnIndexOrThrow(break_duration));
                    totalBreakDuration += breakDuration;
                } while (cursor.moveToNext());

                // ここでtotalBreakDurationを使用して必要な処理を行う
                // 例えば、円グラフや他のビューに表示するなど

                // Set up pie chart entries
                ArrayList<PieEntry> entries = new ArrayList<>();
                entries.add(new PieEntry(totalBreakDuration, ""));

                // Set up the dataset
                PieDataSet dataSet = new PieDataSet(entries, "休んだ合計");
                int skyBlueColor = rgb(135, 206, 235);
                dataSet.setValueTextSize(13f);
                dataSet.setColors(skyBlueColor);

                // Set up the pie chart data
                PieData data = new PieData(dataSet);

                // Customize the pie chart
                pieChart.setData(data);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("合計\n" + totalBreakDuration);
                int whiteColor = android.graphics.Color.rgb(255, 255, 255);

                // ColorDrawableの作成
                Drawable whiteDrawable = new ColorDrawable(whiteColor);
                pieChart.setBackground(whiteDrawable);
                pieChart.invalidate(); // Refresh the chart
            }
        }

    private void getDataFromDatabase() {
        // データベースから日付と休んだ時間のデータを取得
        SQLiteDatabase db = timeDatabase.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{_ID, date,date2, break_duration},
                null, null, null, null, null);

        int index = 0; // インデックスを初期化

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(_ID));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(time_database.date));
            String date2 = cursor.getString(cursor.getColumnIndexOrThrow(time_database.date2));
            int time = cursor.getInt(cursor.getColumnIndexOrThrow(time_database.break_duration));
            String all = date + "/" + date2;
            restingTimes.add(time);
            entries.add(new BarEntry(index, time, all));// index を使用する

            yourDatabaseIdList.add(id); // データベースIDを追加
            dates.add(all);
            index++; // インデックスを増やす
        }


        cursor.close();
    }
    public void updateBarChart() {
        BarDataSet dataSet = new BarDataSet(entries, "休んだ時間");
        int skyBlueColor = rgb(135, 206, 235);
        dataSet.setColors(skyBlueColor);
        dataSet.setValueTextSize(12f);

        data = new BarData(dataSet);
        barChart.setData(data);

        // X軸の設定
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f); // グリッド線の密度
        xAxis.setTextSize(15f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                int index = (int) value;
                if (index >= 0 && index < entries.size()) {
                    return entries.get(index).getData().toString(); // BarEntryの日付を取得して表示
                }
                return "";
            }
        });

        // 新しいデータが追加されても初期表示で何エントリーまで表示するかを指定
        barChart.setVisibleXRangeMaximum(5); // 例: 初期表示で5エントリーまで表示

        YAxis yAxis = barChart.getAxisLeft(); // 左側のY軸を取得（右側のY軸を取得したい場合はgetAxisRight()を使用）
        yAxis.setGranularity(1f); // グリッド線の密度を1単位に設定
        yAxis.setAxisMinimum(0f);  // 最小値
        yAxis.setAxisMaximum(calculateYAxisMaximum());  // 最大値（適切なメソッドで計算する必要があります）
        YAxis yAxis1 = barChart.getAxisRight();
        yAxis1.setDrawLabels(false);


        // その他の設定（例: アニメーション、Y軸の設定）も必要に応じて行う
        barChart.setFitBars(true);
        barChart.getDescription().setText("日付ごとの休んだ時間");
        barChart.animateY(1000);

        // データが変更されたことを通知
        barChart.notifyDataSetChanged();
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setPinchZoom(false);
        barChart.invalidate();
    }
    private float calculateYAxisMaximum() {
        float maxValue = 0f;
        for (int time : restingTimes) {
            if (time > maxValue) {
                maxValue = time;
            }
        }
        return maxValue + 1;  // 最大値より1だけ大きい値を返すことで、最大値の次の整数になります
    }
}
