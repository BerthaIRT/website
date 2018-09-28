package com.ua.cs495_f18.berthaIRT;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class ReportDisplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewreport_template);

        getIncomingIntent();

    }

    private void getIncomingIntent(){
        if(getIntent().hasExtra("report_id")){
            String reportId = getIntent().getStringExtra("report_id");
            //TODO Look up ReportID in SQL and set the rest of the values accordingly.
            setReportId(reportId);
            //setKeyTags();
            //
        }

    }

    private void setReportId(String s){
        TextView tv = findViewById(R.id.reportID);
        tv.setText(s);
    }
}
