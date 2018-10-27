package com.ua.cs495_f18.berthaIRT;

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


public class AdminDisplayReportActivity extends AppCompatActivity {
    private TextView tvCategory, tvReportID, tvDate, tvTime, tvLocation, tvThreatLevel, tvDescription, tvMedia, tvStatus, tvAssignedTo, tvTags, tvNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_displayreport);

        tvCategory = findViewById(R.id.alt_admin_viewreport_category);
        tvReportID = findViewById(R.id.alt_admin_viewreport_id);
        tvDate = findViewById(R.id.alt_admin_viewreport_date);
        tvTime = findViewById(R.id.alt_admin_viewreport_time);
        tvLocation = findViewById(R.id.alt_admin_viewreport_location);
        tvThreatLevel = findViewById(R.id.alt_admin_viewreport_threat);
        tvDescription = findViewById(R.id.alt_admin_viewreport_description);
        tvAssignedTo = findViewById(R.id.alt_admin_viewreport_assignedto);
        tvMedia = findViewById(R.id.alt_admin_viewreport_media);
        tvStatus = findViewById(R.id.alt_admin_viewreport_status);
        tvTags = findViewById(R.id.alt_admin_viewreport_tags);
        tvNotes = findViewById(R.id.input_viewreport_adminnotes);

        FloatingActionButton fab = findViewById(R.id.button_admin_goto_report_messages);
        fab.setOnClickListener(v -> actionGotoMessages());

        ImageView editStatus = findViewById(R.id.button_viewreport_edittags);
        editStatus.setOnClickListener(v -> actionEditTags());

        ImageView editNotes = findViewById(R.id.button_viewreport_editnotes);
        editNotes.setOnClickListener(v -> actionEditNotes());

        updateDisplay(Client.activeReport);
    }

    private void actionGotoMessages() {
        startActivity(new Intent(AdminDisplayReportActivity.this,ChatActivity.class));
    }

    private void updateDisplay(ReportObject r){
        tvCategory.setText(StaticUtilities.listToString(r.categories));
        tvReportID.setText(r.reportId);
        tvDate.setText(r.date);
        tvTime.setText(r.time);
        tvLocation.setText(r.location);
        tvThreatLevel.setText(r.threatLevel);
        tvDescription.setText(r.description);
        tvMedia.setText(r.media);
        tvStatus.setText(r.status);
        tvAssignedTo.setText(r.assignedTo);
        tvTags.setText(StaticUtilities.listToString(r.keywords));
        tvNotes.setText(r.notes);
    }

    private void actionEditTags(){
        startActivity(new Intent(AdminDisplayReportActivity.this,AdminEditReportActivity.class));
        //Stops the current activity whilst edit is being done. after edit is closed, it resumes and updates.
        onStop();
    }

    private void actionEditNotes() {
        final TextView notesDisplay = findViewById(R.id.input_viewreport_adminnotes);

        android.support.v7.app.AlertDialog.Builder b = new android.support.v7.app.AlertDialog.Builder(AdminDisplayReportActivity.this);
        //EditNotesDialog d = new EditNotesDialog(AdminDisplayReportActivity.this, notesDisplay);
        final EditText input = new EditText(this);
        input.setText(notesDisplay.getText());
        input.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setSingleLine(false);
        input.setLines(5);
        input.setMaxLines(5);
        input.setSelection(input.getText().length());
        input.setGravity(Gravity.LEFT | Gravity.TOP);

        setTitle("Notes");
        b.setView(input);

        b.setPositiveButton("OK", (dialogInterface, x) -> {
            notesDisplay.setText(input.getText());
            dialogInterface.dismiss();
        });
        b.setNegativeButton("CANCEL", (dialogInterface, x) -> dialogInterface.dismiss());
        b.create().show();
    }

    //TODO add an export report function.

    // Override onResume to update when resumed.
    @Override
    protected void onResume() {
        super.onResume();
        updateDisplay(Client.activeReport);
    }
}
