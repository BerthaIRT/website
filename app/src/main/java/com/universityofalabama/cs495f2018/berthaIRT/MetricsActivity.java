package com.universityofalabama.cs495f2018.berthaIRT;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MetricsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metrics);

        Client.updateReportMap();

        int temp = Client.reportMap.size();
        Toast.makeText(MetricsActivity.this, "Metrics " + temp, Toast.LENGTH_SHORT).show();

    }
}
