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
            setDate("05/04/22");
            setTime("05:22 PM/AM");
            setStatus("Unopened");
            setAssignedAdmins("Johnathan K");
            setDescription("I once knew a fish named Larry.");
            setKeyTags("Bullying , Abuse, He Hurt Me, Im Crying, Abuse, HELLPPPPPPPP MEEEEEEEE");
            setComments("I hate this job.");
            setFinalAction("Settled.");
        }

    }

    private void setReportId(String s){
        TextView tv = findViewById(R.id.reportID);
        tv.setText(s);
    }

    private void setDate(String s){
        TextView tv = findViewById(R.id.reportDate);
        tv.setText(s);
    }

    private void setTime(String s){
        TextView tv = findViewById(R.id.reportTime);
        tv.setText(s);
    }

    private void setStatus(String s){
        TextView tv = findViewById(R.id.reportStatus);
        tv.setText(s);
    }

    private void setAssignedAdmins(String s){
        TextView tv = findViewById(R.id.reportAssignedAdmins);
        tv.setText(s);
    }

    private void setDescription(String s){
        TextView tv = findViewById(R.id.reportDescription);
        tv.setText(s);
    }

    private void setKeyTags(String s){
        TextView tv = findViewById(R.id.reportKeyTags);
        tv.setText(s);
    }

    private void setComments(String s){
        TextView tv = findViewById(R.id.reportComments);
        tv.setText(s);
    }

    private void setFinalAction(String s){
        TextView tv = findViewById(R.id.reportFinalAction);
        tv.setText(s);
    }
}
