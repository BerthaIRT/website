package com.universityofalabama.cs495f2018.berthaIRT;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class StudentCreateReportActivity extends AppCompatActivity {

    private TextView tvDate;
    private TextView tvTime;
    private EditText etLocation;
    private EditText etDescription;
    private SeekBar sbThreat;

    private long incidentDateStamp = 0;
    private long incidentTimeStamp = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_createreport);

        sbThreat = findViewById(R.id.createreport_input_threat);

        tvDate = findViewById(R.id.createreport_input_date);
        tvDate.setOnClickListener(v -> actionSelectDate());

        tvTime = findViewById(R.id.createreport_input_time);
        tvTime.setOnClickListener(v -> actionSelectTime());

        etLocation = findViewById(R.id.createreport_input_location);
        etDescription = findViewById(R.id.createreport_input_description);


        TextView bCreate = findViewById(R.id.createreport_button_submit);
        bCreate.setOnClickListener(v -> actionSubmitReport());
    }

    private void actionSelectDate() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) ->
        {
            String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
            tvDate.setText(date);
            incidentDateStamp = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTimeInMillis();
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void actionSelectTime() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) ->
        {
            String time = String.format(Locale.ENGLISH,"%02d:%02d", hourOfDay, minute);
            tvTime.setText(time);
            incidentTimeStamp = (60*minute) + (3600*hourOfDay);
        }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void actionSubmitReport() {
        if(etDescription.getText().toString().equals("")){
            etDescription.setError("You must provide a description.");
            return;
        }
        List<String> cats = Arrays.asList(getResources().getStringArray(R.array.category_item));
        List<Boolean> checked = new ArrayList<>();
        for(String s : cats)
            checked.add(false);

        String threat = ((Integer) sbThreat.getProgress()).toString();
        String description = etDescription.getText().toString();
        String location = etLocation.getText().toString();
        if(incidentDateStamp == 0) incidentDateStamp = new GregorianCalendar(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH).getTimeInMillis();

        CheckboxDialog dialog = new CheckboxDialog(this, checked, cats, r->{
            List<String> theseCats = new ArrayList<>();
            for(int i=0; i<cats.size(); i++){
                if(r.get(i))
                    theseCats.add(cats.get(i));
            }
            Report newReport = new Report();
            newReport.threatLevel = threat;
            newReport.description = description;
            newReport.location = location;
            newReport.incidentTimeStamp = ((Long) (incidentDateStamp + incidentTimeStamp)).toString();
            newReport.categories = theseCats;

            sendReport(newReport);
        });

        dialog.show();
    }

    private void sendReport(Report newReport) {
        Util.WaitDialog waitDialog = new Util.WaitDialog(StudentCreateReportActivity.this);
        waitDialog.message.setText("Sending report...");
        waitDialog.dialog.show();
        String jayReport = Client.net.gson.toJson(newReport);
        Client.net.secureSend(this, "/report/new", jayReport, r->{
            Report finalizedReport = Client.net.gson.fromJson(r, Report.class);
            Client.activeReport = finalizedReport;
            waitDialog.dialog.dismiss();
            startActivity(new Intent(this, ReportDetailsStudentActivity.class));
            finish();
        });
    }
}
