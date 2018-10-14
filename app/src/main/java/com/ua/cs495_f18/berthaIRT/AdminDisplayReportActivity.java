package com.ua.cs495_f18.berthaIRT;

import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

class EditNotesDialog extends AlertDialog.Builder{
    EditText input;
    public EditNotesDialog(Context context, final TextView existing){
        super(context);

        input = new EditText(context);
        input.setText(existing.getText());
        input.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setSingleLine(false);
        input.setLines(5);
        input.setMaxLines(5);
        input.setGravity(Gravity.LEFT | Gravity.TOP);

        setTitle("Notes");
        setView(input);

        setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int x) {
                existing.setText(input.getText());
                dialogInterface.dismiss();
            }
        });
        setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int x) {
                dialogInterface.dismiss();
            }
        });

        create();
    }

}
public class AdminDisplayReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_displayreport);

        getIncomingIntent();

        FloatingActionButton fab = findViewById(R.id.button_admin_goto_report_messages);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionGotoMessages();
            }
        });

        ImageView editStatus = findViewById(R.id.button_viewreport_edittags);
        editStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionEditTags();
            }
        });

        ImageView editNotes = findViewById(R.id.button_viewreport_editnotes);
        editNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionEditNotes();
            }
        });
    }

    private void actionGotoMessages() {
        startActivity(new Intent(AdminDisplayReportActivity.this,MessageActivity.class));
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("report_id")) {
            String reportId = getIntent().getStringExtra("report_id");
            String date = "05/04/22";
            String time = "5:22PM";
            String status = "Unopened";
            String description = "Lorum ipsum blah blah";
            ((TextView) findViewById(R.id.label_admin_viewreport_id_value)).setText(reportId);
            ((TextView) findViewById(R.id.label_admin_viewreport_date_value)).setText(date);
            ((TextView) findViewById(R.id.label_admin_viewreport_time_value)).setText(time);
            ((TextView) findViewById(R.id.label_admin_viewreport_status_value)).setText(status);
            ((TextView) findViewById(R.id.label_admin_viewreport_description_value)).setText(description);
        } else if (getIntent().hasExtra("need_update")) {
            updateDisplay();
        }
    }

    private void actionEditTags(){
        startActivity(new Intent(AdminDisplayReportActivity.this,AdminEditReportActivity.class));
        //Stops the current activity whilst edit is being done. after edit is closed, it resumes and updates.
        onStop();
    }

    private void actionEditNotes() {
        TextView notesDisplay = findViewById(R.id.input_viewreport_adminnotes);
        EditNotesDialog d = new EditNotesDialog(AdminDisplayReportActivity.this, notesDisplay);
        d.show();
    }

    //TODO finish this function to update the display with new SQL information after an edit is made.
    private void updateDisplay(){
        //get info from SQL
        //CAN REMOVE THIS ONLY FOR TEST
    }

    //TODO add an export report function.

    // Override onResume to update when resumed.
    @Override
    protected void onResume() {
        super.onResume();
            updateDisplay();
    }
}
