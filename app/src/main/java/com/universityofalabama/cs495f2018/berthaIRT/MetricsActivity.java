package com.universityofalabama.cs495f2018.berthaIRT;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.UniqueLegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

public class MetricsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metrics);

//        Client.updateReportMap();
//        int temp = Client.reportMap.size();
//        Toast.makeText(MetricsActivity.this, "Metrics " + temp, Toast.LENGTH_SHORT).show();
//        GraphView graph = (GraphView) findViewById(R.id.graph);
//        //first series
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
//                new DataPoint(0, 1),
//                new DataPoint(1, 5),
//                new DataPoint(2, 3),
//                new DataPoint(3, 2),
//                new DataPoint(4, 6)
//        });
//        //second series
//        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<DataPoint>(new DataPoint[] {
//
//                new DataPoint(0, 2),
//                new DataPoint(1, 6),
//                new DataPoint(2, 4),
//                new DataPoint(3, 3),
//                new DataPoint(4, 7)
//        });
//
//        series.setTitle("First Metric");
//        series.setDrawDataPoints(true);
//        series1.setTitle("Second Metric");
//        series1.setColor(Color.argb(255, 255, 60, 60));
//        series1.setDrawDataPoints(true);
//        graph.addSeries(series);
//        graph.addSeries(series1);
//
//        // legend
//        graph.getLegendRenderer().setVisible(true);
//        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);


        GraphView graph = (GraphView) findViewById(R.id.graph);
        TextView xValue = (TextView) findViewById(R.id.graph_x_value);
        TextView yValue = (TextView) findViewById(R.id.graph_y_value);

        // first series is a line
        DataPoint[] points = new DataPoint[50];
        for (int i = 0; i < 50; i++) {
            points[i] = new DataPoint(i, Math.sin(i*0.5) * 20*(Math.random()*10+1));
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);

        // set manual X bounds
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(10);

        // enable scrolling
        graph.getViewport().setScrollable(true);

        series.setTitle("Random Curve");
        series.setDrawDataPoints(true);
        graph.addSeries(series);

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {

                //Toast.makeText(graph.getContext(), "Series1: On Data Point clicked: "+dataPoint, Toast.LENGTH_SHORT).show();
                xValue.setText("Touched Point X Value: " + dataPoint.getX());
                yValue.setText("Touched Point Y Value: " + dataPoint.getY());
            }
        });
    }
}

