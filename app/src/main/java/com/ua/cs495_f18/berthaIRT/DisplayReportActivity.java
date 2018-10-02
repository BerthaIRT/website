package com.ua.cs495_f18.berthaIRT;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
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



        //Opens Edit activity when the Linear Layout space shown as editable is clicked in the box.
        LinearLayout editible = (LinearLayout) findViewById(R.id.editable);
        editible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInfo();
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
        else if(getIntent().hasExtra("need_update")){
            updateDisplay();
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

    private void editInfo(){
        startActivity(new Intent(DisplayReportActivity.this,AdminEditReportActivity.class));
        //Stops the current activity whilst edit is being done. after edit is closed, it resumes and updates.
        onStop();
    }

    //TODO finish this function to update the display with new SQL information after an edit is made.
    private void updateDisplay(){
        //get info from SQL
        //CAN REMOVE THIS ONLY FOR TEST
        setComments("this is fire...");
    }

    //TODO add an export report function.


    // Override onResume to update when resumed.
    @Override
    protected void onResume() {
        super.onResume();
            updateDisplay();
    }
}
