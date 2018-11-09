package com.universityofalabama.cs495f2018.berthaIRT;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StudentCreateReportActivity extends AppCompatActivity {

    private TextView tvDate;
    private TextView tvTime;
    private EditText etLocation;
    private EditText etDescription;
    private SeekBar sbThreat;
    //private List<String> newCategories;

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
        }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void actionSubmitReport() {
        if(etDescription.getText().toString().equals("")){
            etDescription.setError("You must provide a description.");
            return;
        }
        List<Boolean> checked = new ArrayList<>();
        List<String> cat = Arrays.asList(getResources().getStringArray(R.array.category_item));
        for(int i = 0; i < cat.size(); i++)
            checked.add(false);
        Util.showSelectCategoriesDialog(this,checked,cat,this::submitReport);
    }

    private void submitReport(List<String> selectedCategories) {
        Client.net.secureSend("report/newid", null, (r)->{
            Report newReport = new Report(r, etDescription.getText().toString(),
                    ((Integer) sbThreat.getProgress()).toString(), selectedCategories);
            String date = tvDate.getText().toString();
            String time = tvTime.getText().toString();
            String loc = etLocation.getText().toString();
            //if the user didn't select date or time set to current
            if(date.equals("")) date = new SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(new Date());
            if(time.equals("")) time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());

            //set the incidentTimeStamp
            newReport.incidentTimeStamp = date + " " + time;

            if(!loc.equals("")) newReport.location = loc;

            //TODO adds to the report log

            JsonObject jay = new JsonObject();
            jay.addProperty("id", r);
            jay.addProperty("data", Client.net.gson.toJson(newReport));

            Client.net.secureSend("report/submit", jay.toString(), (rr)->{
                if(rr.equals("ALL GOOD HOMIE")){
                    Util.showOkDialog(StudentCreateReportActivity.this, "Success", "Your report was submitted!", this::finish);
                }
            });
        });
    }
}
