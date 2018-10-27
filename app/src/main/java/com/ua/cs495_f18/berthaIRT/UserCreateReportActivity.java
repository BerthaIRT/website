package com.ua.cs495_f18.berthaIRT;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UserCreateReportActivity extends AppCompatActivity {

    private EditText etDate;
    private EditText etTime;
    private EditText etLocation;
    private EditText etDescription;
    private SeekBar sbThreat;
    private TextView tvCategories;
    private List<String> newCategories;

    private boolean[] checkedCategories;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_createreport);

        sbThreat = findViewById(R.id.input_createreport_threat);

        etDate = findViewById(R.id.input_createreport_date);
        etDate.setOnClickListener(v -> actionSelectDate());

        etTime = findViewById(R.id.input_createreport_time);
        etTime.setOnClickListener(v -> actionSelectTime());

        etLocation = findViewById(R.id.input_createreport_location);
        etDescription = findViewById(R.id.input_createreport_description);
        tvCategories = findViewById(R.id.alt_createreport_categories);

        Button bCategories = findViewById(R.id.button_createreport_showcategories);
        bCategories.setOnClickListener(v -> actionSelectCategories());

        Button bCreate = findViewById(R.id.button_createreport_send);
        bCreate.setOnClickListener(v -> actionSubmitReport());
    }

    private void actionSelectDate() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) ->
                etDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year), mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void actionSelectTime() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) ->
        {
            if(minute < 10)
                etTime.setText(hourOfDay + ":0" + minute);
            else
                etTime.setText(hourOfDay + ":" + minute);
        }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void actionSelectCategories(){
        final String[] categoryItems = getResources().getStringArray(R.array.category_item);
        checkedCategories = new boolean[categoryItems.length];

        AlertDialog.Builder b = new AlertDialog.Builder(UserCreateReportActivity.this);
        b.setTitle("Select Categories");
        b.setCancelable(false);

        b.setMultiChoiceItems(categoryItems, checkedCategories, (dialog, position, isChecked) -> {
            if(isChecked)
                checkedCategories[position] = true;
            else
                checkedCategories[position] = false;
        });

        b.setPositiveButton("OK", (dialogInterface, x) -> {
            String label = "";
            newCategories = new ArrayList<>();
            for(int i=0; i<checkedCategories.length; i++)
                if(checkedCategories[i]) {
                    label += categoryItems[i] + ", ";
                    newCategories.add(categoryItems[i]);
                }
            label = label.substring(0, label.length()-2);

            if(label.equals(""))
                label = "No Categories Selected";
            tvCategories.setText(label);
            dialogInterface.dismiss();
        });

        b.setNegativeButton("CANCEL", (dialogInterface, x) -> dialogInterface.dismiss());

        b.setNeutralButton("CLEAR ALL", (dialogInterface, x) -> {
            for (boolean bool : checkedCategories)
                bool = false;
            tvCategories.setText("No Categories Selected");
        });

        b.create().show();
    }

    private void actionSubmitReport() {
        if(etDescription.getText().toString().equals("")){
            etDescription.setError("You must provide a description.");
            return;
        }

        Client.net.secureSend("report/newid", null, (r)->{
            ReportObject newReport = new ReportObject(r, etDescription.getText().toString(),
                    ((Integer) sbThreat.getProgress()).toString(), newCategories);
            String date = etDate.getText().toString();
            String time = etTime.getText().toString();
            String loc = etLocation.getText().toString();
            if(!date.equals("")) newReport.date = date;
            if(!time.equals("")) newReport.time = time;
            if(!loc.equals("")) newReport.date = loc;

            JsonObject jay = new JsonObject();
            jay.addProperty("id", r);
            jay.addProperty("data", Client.net.gson.toJson(newReport));

            Client.net.secureSend("report/submit", jay.toString(), (rr)->{
                if(rr.equals("ALL GOOD HOMIE")){
                    StaticUtilities.showSimpleAlert(UserCreateReportActivity.this, "Success", "Report submitted.", (d, w)->{
                        startActivity(new Intent(UserCreateReportActivity.this, UserPortalActivity.class));
                        finish();
                    });
                }
            });
        });
    }
}