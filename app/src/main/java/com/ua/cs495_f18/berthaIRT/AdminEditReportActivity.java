package com.ua.cs495_f18.berthaIRT;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;


public class AdminEditReportActivity extends AppCompatActivity {

    private RadioButton rbOpen, rbInProgress, rbClosed, rbResolved;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_editreportdetails);

        initRadioButtons();
        refreshInfo();

        TextView bAssignedTo = findViewById(R.id.button_editreport_assignedto);
        bAssignedTo.setOnClickListener((v)-> actionAssignAdmins());
        Button bCancel = findViewById(R.id.button_editreport_cancel);
        bCancel.setOnClickListener((v)-> finish());
        Button bSubmit = findViewById(R.id.button_editreport_submit);
        bSubmit.setOnClickListener((v)-> actionSubmit());
        ImageView bInfo = findViewById(R.id.button_editreport_tagsinfo);
        bInfo.setOnClickListener((v)-> actionShowInfo());
        TextView bKeywords = findViewById(R.id.button_editreport_keywords);
        bKeywords.setOnClickListener((v)-> actionEditKeywords());
        TextView bCategories = findViewById(R.id.button_editreport_categories);
        bCategories.setOnClickListener((v)-> actionEditCategories());
    }

    private void actionAssignAdmins() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, Client.adminList);
        LayoutInflater inflater=AdminEditReportActivity.this.getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_admin_search, null);

        AutoCompleteTextView actv = findViewById(R.id.input_adminsearch_name);
        actv.setAdapter(adapter);
        AlertDialog.Builder b = new AlertDialog.Builder(AdminEditReportActivity.this);
        b.setTitle("Select an administrator.");
        b.setView(v);
        b.setNegativeButton("Clear", (d, w)->{
            Client.activeReport.assignedTo = "N/A";
            d.dismiss();
        });
        b.setPositiveButton("Assign", (d, w)->{
            Client.activeReport.assignedTo = actv.getText().toString();
            d.dismiss();
        });
        refreshInfo();
    }

    private void refreshInfo() {
        TextView tvAssignedTo = findViewById(R.id.alt_editreport_assignedto);
        TextView tvKeywords = findViewById(R.id.alt_editreport_keywords);
        TextView tvCategories = findViewById(R.id.alt_editreport_categories);
        tvAssignedTo.setText(Client.activeReport.assignedTo);
        tvKeywords.setText(StaticUtilities.listToString(Client.activeReport.keywords));
        tvCategories.setText(StaticUtilities.listToString(Client.activeReport.categories));

        if(Client.activeReport.status.equals("Open")) rbOpen.setChecked(true);
        if(Client.activeReport.status.equals("In Progress")) rbInProgress.setChecked(true);
        if(Client.activeReport.status.equals("Closed")) rbClosed.setChecked(true);
        if(Client.activeReport.status.equals("Resolved")) rbResolved.setChecked(true);
    }

    private void actionEditCategories() {
        //TODO
    }

    private void actionEditKeywords() {
        //TODO
    }

    private void initRadioButtons() {
        rbOpen = findViewById(R.id.rb_editreport_open);
        rbInProgress = findViewById(R.id.rb_editreport_inprogress);
        rbClosed = findViewById(R.id.rb_editreport_closed);
        rbResolved = findViewById(R.id.rb_editreport_resolved);

        rbOpen.setOnClickListener((v)-> updateRadioButtons((RadioButton) v));
        rbInProgress.setOnClickListener((v)-> updateRadioButtons((RadioButton) v));
        rbClosed.setOnClickListener((v)-> updateRadioButtons((RadioButton) v));
        rbResolved.setOnClickListener((v)-> updateRadioButtons((RadioButton) v));
    }

    private void updateRadioButtons(RadioButton v) {
        rbOpen.setChecked(false);
        rbInProgress.setChecked(false);
        rbClosed.setChecked(false);
        rbResolved.setChecked(false);

        if(v == rbOpen) Client.activeReport.status = "Open";
        if(v == rbInProgress) Client.activeReport.status = "In Progress";
        if(v == rbClosed) Client.activeReport.status = "Closed";
        if(v == rbResolved) Client.activeReport.status = "Resolved";

        refreshInfo();
    }

    private void actionShowInfo() {
        AlertDialog.Builder b = new AlertDialog.Builder(AdminEditReportActivity.this);
        b.setCancelable(true);
        b.setMessage("Categories help you quantify the type of issue described in the report.\nKey words are searchable and might include people, places or events");
        b.setPositiveButton("OK",null);
        b.create().show();
    }

    private void actionSubmit() {
        //TODO
    }
}