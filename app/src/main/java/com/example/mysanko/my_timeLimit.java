package com.example.mysanko;

import static android.graphics.Color.rgb;
import static com.example.mysanko.time_database._ID;
import static com.example.mysanko.time_database.break_duration;
import static com.example.mysanko.time_database.date;
import static com.example.mysanko.time_database.date2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
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
    int maxNumber = 0;
    Button addbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_time_limit);

        timeDatabase = new time_database(this);

        pieChart = findViewById(R.id.lineChart);
        addbutton = findViewById(R.id.addbutton);
        barChart = findViewById(R.id.horizonatal);


        SoundPool soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        sound = soundPool.load(my_timeLimit.this, R.raw.poin, 1);

        getDataFromDatabase();

        // 棒グラフを初期描画
        updateBarChart();
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(my_timeLimit.this, addT.class);
                soundPool.play(sound, 1f, 1f, 0, 0, 1f);

                startActivity(intent);
                finish();
            }
        });


        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                // クリックされたエントリーのX軸のインデックスを取得
                int selectedIndex = (int) e.getX();

                // データベースIDのリストから選択されたインデックスに対応するデータベースIDを取得
                if (selectedIndex >= 0 && selectedIndex < yourDatabaseIdList.size()) {
                    long selectedDatabaseId = yourDatabaseIdList.get(selectedIndex);

                    // タップされたデータベースIDを使って詳細データを表示するIntentを作成
                    int requestCode = 1; // 任意の値
                    Intent intent = new Intent(my_timeLimit.this, adb.class);
                    intent.putExtra("time", selectedDatabaseId);
                    startActivityForResult(intent, requestCode);
                    finish();
                }

            }

            @Override
            public void onNothingSelected() {

            }
        });

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Intent intent = new Intent(my_timeLimit.this, max_time.class);
                startActivity(intent);

            }

            @Override
            public void onNothingSelected() {

            }
        });


        SQLiteDatabase db = timeDatabase.getReadableDatabase();
        Cursor cursor = db.query(time_database.TABLE_NAME, new String[]{break_duration}, null, null, null, null, null);

        max_time_database maxTimeDatabase = new max_time_database(my_timeLimit.this);

        SQLiteDatabase database = maxTimeDatabase.getReadableDatabase();

        Cursor cursor2 = database.query(max_time_database.TABLE_NAME, new String[]{max_time_database.maxT}, null, null, null, null, null);

        if(cursor2 != null && cursor2.moveToFirst()) {
            maxNumber = cursor2.getInt(cursor2.getColumnIndexOrThrow(max_time_database.maxT));
        }


        if (maxNumber != 0) {
            // Set up pie chart entries
            if (cursor != null && cursor.moveToFirst() && cursor2 != null && cursor2.moveToFirst()) {
                int totalBreakDuration = 0;
                maxNumber = cursor2.getInt(cursor2.getColumnIndexOrThrow(max_time_database.maxT));
                do {
                    int breakDuration = cursor.getInt(cursor.getColumnIndexOrThrow(break_duration));
                    totalBreakDuration += breakDuration;
                   // Toast.makeText(my_timeLimit.this,String.valueOf(totalBreakDuration),Toast.LENGTH_SHORT).show();
                } while (cursor.moveToNext());

                ArrayList<PieEntry> entries = new ArrayList<>();
                entries.add(new PieEntry(totalBreakDuration * 100 , "休憩した時間"));
                int n = maxNumber - totalBreakDuration ;
                entries.add(new PieEntry(n * 100 , "残りの時間"));

                // ... (previous code)

                // Set up the dataset
                PieDataSet dataSet = new PieDataSet(entries, "休んだ合計");
                dataSet.setValueTextSize(13f);
                int breakTimeColor = Color.rgb(255, 0, 0);
                int remainingTimeColor = Color.rgb(0, 0, 255);
                ArrayList<Integer> colors = new ArrayList<>();
                colors.add(breakTimeColor);
                colors.add(remainingTimeColor);
                dataSet.setColors(colors);

                // Set up the pie chart data
                PieData data = new PieData(dataSet);

                // Customize the pie chart
                pieChart.setData(data);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("合計\n" + totalBreakDuration + "/" + maxNumber + "時間");
                pieChart.setBackgroundColor(Color.WHITE);

                // Additional configuration for PieChart
                pieChart.setUsePercentValues(true);
                pieChart.setDrawHoleEnabled(true);
                pieChart.setHoleColor(Color.TRANSPARENT);
                pieChart.setHoleRadius(30f);
                pieChart.setTransparentCircleRadius(40f);
                pieChart.setEntryLabelColor(Color.BLACK);

                pieChart.invalidate();

                if(totalBreakDuration == maxNumber){
                    Toast.makeText(my_timeLimit.this,"もう休めないよ！！",Toast.LENGTH_SHORT).show();
                } else if (totalBreakDuration > maxNumber) {
                    Toast.makeText(my_timeLimit.this,"もう超えてるよ！涙",Toast.LENGTH_SHORT).show();
                }
            }
                }else{


                    // Set up pie chart entries
                    if (cursor != null && cursor.moveToFirst()) {
                        int totalBreakDuration = 0;

                        do {
                            int breakDuration = cursor.getInt(cursor.getColumnIndexOrThrow(break_duration));
                            totalBreakDuration += breakDuration;
                        } while (cursor.moveToNext());

                        ArrayList<PieEntry> entries = new ArrayList<>();
                        entries.add(new PieEntry(totalBreakDuration * 100 / 10, "休憩した時間"));

                        int breakTimeColor = Color.rgb(255, 0, 0);
                        ArrayList<Integer> colors = new ArrayList<>();
                        colors.add(breakTimeColor);

                        // Set up the dataset
                        PieDataSet dataSet = new PieDataSet(entries, "休んだ合計");
                        dataSet.setValueTextSize(13f);
                        dataSet.setColors(colors);

                        // Set up the pie chart data
                        PieData data = new PieData(dataSet);

                        // Customize the pie chart
                        pieChart.setData(data);
                        pieChart.getDescription().setEnabled(false);
                        pieChart.setCenterText("Your Center Text");
                        pieChart.setBackgroundColor(Color.WHITE);


                        pieChart.setCenterText("合計\n" + totalBreakDuration + "時間");

                        pieChart.invalidate();
                    }
                }
        cursor2.close();
        cursor.close();
            }
        private void getDataFromDatabase () {
            // データベースから日付と休んだ時間のデータを取得
            SQLiteDatabase db = timeDatabase.getReadableDatabase();
            Cursor cursor = db.query(time_database.TABLE_NAME, new String[]{_ID, date, date2, break_duration},
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
