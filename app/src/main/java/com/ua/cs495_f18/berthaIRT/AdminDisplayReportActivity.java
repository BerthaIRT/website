package com.ua.cs495_f18.berthaIRT;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class AdminDisplayReportActivity extends AppCompatActivity {
    private TextView tvAdminNotes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_displayreport);

        getIncomingIntent();

        FloatingActionButton fab = findViewById(R.id.button_admin_goto_report_messages);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDisplayReportActivity.this,MessageActivity.class));
            }
        });



        //Opens Edit activity when the Linear Layout space shown as editable is clicked in the box.
        ImageView editStatus = (ImageView) findViewById(R.id.button_viewreport_edittags);
        editStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInfo();
            }
        });

        tvAdminNotes = (TextView) findViewById(R.id.input_viewreport_adminnotes);
        tvAdminNotes.getText();
        //Opens Edit activity when the Linear Layout space shown as editable is clicked in the box.
        ImageView editNotes = (ImageView) findViewById(R.id.button_edit_administrator_notes);
        editNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editNotesDialogBox();
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
        TextView tv = findViewById(R.id.label_admin_viewreport_id_value);
        tv.setText(s);
    }

    private void setDate(String s){
        TextView tv = findViewById(R.id.label_admin_viewreport_date_value);
        tv.setText(s);
    }

    private void setTime(String s){
        TextView tv = findViewById(R.id.label_admin_viewreport_time_value);
        tv.setText(s);
    }

    private void setStatus(String s){
        TextView tv = findViewById(R.id.label_admin_viewreport_status_value);
        tv.setText(s);
    }

    private void setAssignedAdmins(String s){
        TextView tv = findViewById(R.id.label_viewreport_assignedto_value);
        tv.setText(s);
    }

    private void setDescription(String s){
        TextView tv = findViewById(R.id.label_admin_viewreport_description_value);
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
        startActivity(new Intent(AdminDisplayReportActivity.this,AdminEditReportActivity.class));
        //Stops the current activity whilst edit is being done. after edit is closed, it resumes and updates.
        onStop();
    }

    private void editNotesDialogBox () {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Notes");

        final EditText input = new EditText(this);

        final String item_value = tvAdminNotes.toString();

        input.setText(item_value);
        input.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setSingleLine(false);
        input.setLines(5);
        input.setMaxLines(5);
        input.setGravity(Gravity.LEFT | Gravity.TOP);
        builder.setView(input);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                tvAdminNotes.setText(input.toString());
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
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
