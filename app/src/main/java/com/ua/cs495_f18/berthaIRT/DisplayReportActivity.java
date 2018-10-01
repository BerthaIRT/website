package com.ua.cs495_f18.berthaIRT;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


public class DisplayReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewreport);

        getIncomingIntent();

        FloatingActionButton fab = findViewById(R.id.button_goto_report_messages);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DisplayReportActivity.this,MessageActivity.class));
            }
        });
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
        }

    }

    private void setReportId(String s){
        TextView tv = findViewById(R.id.label_viewreport_id_value);
        tv.setText(s);
    }

    private void setDate(String s){
        TextView tv = findViewById(R.id.label_viewreport_date_value);
        tv.setText(s);
    }

    private void setTime(String s){
        TextView tv = findViewById(R.id.label_viewreport_time_value);
        tv.setText(s);
    }

    private void setStatus(String s){
        TextView tv = findViewById(R.id.label_viewreport_status_value);
        tv.setText(s);
    }

    private void setAssignedAdmins(String s){
        TextView tv = findViewById(R.id.label_viewreport_assignedto_value);
        tv.setText(s);
    }

    private void setDescription(String s){
        TextView tv = findViewById(R.id.label_viewreport_description_value);
        tv.setText(s);
    }

    private void setKeyTags(String s){
        TextView tv = findViewById(R.id.label_viewreport_tags_value);
        tv.setText(s);
    }

    private void setComments(String s){
        TextView tv = findViewById(R.id.input_viewreport_adminnotes);
        tv.setText(s);
    }
}
