package com.example.chartapplication;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String SAMPLE_RESPONSE = "[{\"mark\":32,\"model\":13,\"year\":2012,\"total\":38,\"calculated\":38,\"average\":12093182,\"date\":\"2015-09-19 14:14:48\"},{\"mark\":32,\"model\":13,\"year\":2012,\"total\":127,\"calculated\":127,\"average\":10443505,\"date\":\"2015-10-01 06:12:34\"},{\"mark\":32,\"model\":13,\"year\":2012,\"total\":122,\"calculated\":122,\"average\":10698835,\"date\":\"2015-11-01 02:09:43\"},{\"mark\":32,\"model\":13,\"year\":2012,\"total\":13,\"calculated\":13,\"average\":13176462,\"date\":\"2015-12-24 22:09:00\"},{\"mark\":32,\"model\":13,\"year\":2012,\"total\":91,\"calculated\":91,\"average\":13796299,\"date\":\"2016-01-01 02:08:47\"},{\"mark\":32,\"model\":13,\"year\":2012,\"total\":72,\"calculated\":72,\"average\":14784650,\"date\":\"2016-02-01 02:09:52\"},{\"mark\":32,\"model\":13,\"year\":2012,\"total\":91,\"calculated\":91,\"average\":15010000,\"date\":\"2016-03-01 02:09:39\"},{\"mark\":32,\"model\":13,\"year\":2012,\"total\":49,\"calculated\":49,\"average\":15010000,\"date\":\"2016-04-01 06:14:38\"},{\"mark\":32,\"model\":13,\"year\":2012,\"total\":48,\"calculated\":38,\"average\":8773333,\"date\":\"2016-05-20 22:23:16\"},{\"mark\":32,\"model\":13,\"year\":2012,\"total\":91,\"calculated\":68,\"average\":8716608,\"date\":\"2016-06-02 22:23:13\"},{\"mark\":32,\"model\":13,\"year\":2012,\"total\":52,\"calculated\":28,\"average\":10620000,\"date\":\"2016-08-08 22:25:49\"},{\"mark\":32,\"model\":13,\"year\":2012,\"total\":43,\"calculated\":19,\"average\":11040278,\"date\":\"2016-09-01 22:25:36\"}]";

    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupLineChart();
        setupBarChart();
    }

    private void setupBarChart() {

    }

    private void setupLineChart() {
        long lastDate = 0;
        LineChart lineChart = (LineChart) findViewById(R.id.activity_main_line_chart);

        List<AveragePrice> prices = getSamplePrices();
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < prices.size(); i++) {
            AveragePrice price = prices.get(i);
            lastDate = price.getDateLong(mSimpleDateFormat) / 1000;
            entries.add(new Entry((float) lastDate, (float) price.getAverage()));
        }

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setSpaceTop(20f);
        leftAxis.setSpaceBottom(20f);
        leftAxis.setLabelCount(4);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setTextColor(Color.parseColor("#b4b4b4"));
        leftAxis.setDrawAxisLine(false);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setDrawLabels(false);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawGridLines(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setLabelRotationAngle(90f);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);

        LineDataSet dataSet = new LineDataSet(entries, " Average price");
        dataSet.setColor(Color.parseColor("#0060c0"));
        dataSet.setLineWidth(2f);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        LineData lineData = new LineData(dataSet);

        List<Entry> entries1 = new ArrayList<>();
        entries1.add(new Entry(lastDate, 9700000));
        LineDataSet currentDataSet = new LineDataSet(entries1, "Current advertisement");
        currentDataSet.setDrawCircleHole(false);
        currentDataSet.setCircleColor(Color.parseColor("#4caf50"));
        currentDataSet.setCircleRadius(4f);
        currentDataSet.setDrawValues(false);
        lineData.addDataSet(currentDataSet);

        lineChart.setData(lineData);
        lineChart.setDescription(null);
        lineChart.invalidate();
    }

    @NonNull
    private List<AveragePrice> getSamplePrices() {
        List<AveragePrice> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(SAMPLE_RESPONSE);
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(AveragePrice.formJson(jsonArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    static class AveragePrice {
        private int mMark;
        private int mModel;
        private int mYear;
        private int mTotal;
        private int mCalculated;
        private int mAverage;
        private String mDate;

        public AveragePrice() {
        }

        public static AveragePrice formJson(JSONObject object) throws JSONException {
            AveragePrice price = new AveragePrice();
            price.mMark = object.getInt("mark");
            price.mModel = object.getInt("model");
            price.mYear = object.getInt("year");
            price.mTotal = object.getInt("total");
            price.mCalculated = object.getInt("calculated");
            price.mAverage = object.getInt("average");
            price.mDate = object.getString("date");

            return price;
        }

        public int getMark() {
            return mMark;
        }

        public int getModel() {
            return mModel;
        }

        public int getYear() {
            return mYear;
        }

        public int getTotal() {
            return mTotal;
        }

        public int getCalculated() {
            return mCalculated;
        }

        public int getAverage() {
            return mAverage;
        }

        public String getDate() {
            return mDate;
        }

        public long getDateLong(SimpleDateFormat sdf) {
            try {
                return sdf.parse(mDate).getTime();
            } catch (ParseException e) {
                e.printStackTrace();

                return 0;
            }
        }
    }
}
