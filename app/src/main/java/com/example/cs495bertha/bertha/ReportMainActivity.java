package com.example.cs495bertha.bertha;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ReportMainActivity extends AppCompatActivity {
    Button btnNew, btnOld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_main);

        goToNewReport();
        goToPastReports();
    }

    private void goToNewReport () {
        btnNew = (Button) findViewById(R.id.btnNewReport);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReportMainActivity.this,SubmitReportActivity.class));
            }
        });
    }

    private void goToPastReports() {
        btnOld = (Button) findViewById(R.id.btnPastReports);
        btnOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReportMainActivity.this,PastReportsActivity.class));
            }
        });
    }
}
