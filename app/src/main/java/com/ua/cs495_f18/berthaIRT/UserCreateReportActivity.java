package com.ua.cs495_f18.berthaIRT;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Calendar;

public class UserCreateReportActivity extends AppCompatActivity {

    private SeekBar threat;
    private EditText selectDate;
    private EditText selectTime;
    private EditText location;
    private TextView categories;
    private TextView description;
    private ReportObject reportObject;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_createreport);

        threat = findViewById(R.id.input_threat_slider);
        categories = findViewById(R.id.label_selected_categories);
        location = findViewById(R.id.input_incident_location);
        description = findViewById(R.id.input_incident_description);

        selectDate = findViewById(R.id.input_incident_date);
        selectDate.setOnClickListener(v -> actionSelectDate());

        selectTime = findViewById(R.id.input_incident_time);
        selectTime.setOnClickListener(v -> actionSelectTime());

        Button mCategories = findViewById(R.id.button_show_category_select);
        mCategories.setOnClickListener(v -> actionSelectCategories());

        Button btnSubmitReport = findViewById(R.id.button_send_report);
        btnSubmitReport.setOnClickListener(v -> actionSubmitReport());

    }

    private void actionSelectDate() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) ->
                selectDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year), mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void actionSelectTime() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) ->
                selectTime.setText(hourOfDay + ":" + minute), mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void actionSelectCategories(){
        AlertDialog.Builder b = new AlertDialog.Builder(UserCreateReportActivity.this);

        final String[] categoryItems = getResources().getStringArray(R.array.category_item);
        final boolean[] checkedItems = StaticUtilities.getPreChecked(categoryItems,categories.getText().toString());

        b.setTitle("Select Categories");
        b.setCancelable(false);

        b.setMultiChoiceItems(categoryItems, checkedItems, (dialog, position, isChecked) -> {
            if(isChecked)
                checkedItems[position] = true;
            else
                checkedItems[position] = false;
        });

        b.setPositiveButton("OK", (dialogInterface, x) -> {
            StringBuilder sb = StaticUtilities.getStringBuilder(StaticUtilities.getListOfStrings(checkedItems, categoryItems));
            if(sb.toString().equals(""))
                sb.append("No Categories Selected");
            categories.setText(sb);
            dialogInterface.dismiss();
        });

        b.setNegativeButton("CANCEL", (dialogInterface, x) -> dialogInterface.dismiss());

        b.setNeutralButton("CLEAR ALL", (dialogInterface, x) -> {
            for (int i=0; i<checkedItems.length; i++) {
                checkedItems[i] = false;
                categories.setText("No Categories Selected");
            }
        });

        b.create().show();
    }

    private void actionSubmitReport() {
        if(!isRequiredInput()) return;
        //Create a new Report Object
        reportObject = new ReportObject("11111111",
                StaticUtilities.getStringList(categories.getText().toString()),
                selectDate.getText().toString(),
                selectTime.getText().toString(),
                "Unopened",
                location.getText().toString(),
                Integer.toString(threat.getProgress()),
                description.getText().toString(),
                "", "",
                StaticUtilities.getStringList(categories.getText().toString()),
                StaticUtilities.getStringList(""));

        BackgroundTask task = new BackgroundTask(UserCreateReportActivity.this);
        task.execute();
    }

    private boolean isRequiredInput() {
        boolean temp = true;
        if(selectDate.getText().toString().equals("")) {
            ((TextView)findViewById(R.id.error_date)).setText("You must select a date");

            findViewById(R.id.error_date).setVisibility(View.VISIBLE);
            //StaticUtilities.showSimpleAlert(this,"No Date", "You must select a date");
            temp = false;
        }
        if (selectTime.getText().toString().equals("")) {
            ((TextView)findViewById(R.id.error_time)).setText("You must select a time");

            findViewById(R.id.error_time).setVisibility(View.VISIBLE);
            //StaticUtilities.showSimpleAlert(this,"No Time!", "You must select a time");
            temp = false;
        }
        if (location.getText().toString().equals("")) {
            ((TextView)findViewById(R.id.error_location)).setText("You must give a location");
            findViewById(R.id.error_location).setVisibility(View.VISIBLE);
            //StaticUtilities.showSimpleAlert(this,"No Location!", "You must give a location");
            temp = false;
        }
        if (description.getText().toString().equals("")) {
            ((TextView)findViewById(R.id.error_description)).setText("You must give a description");
            findViewById(R.id.error_description).setVisibility(View.VISIBLE);
            //StaticUtilities.showSimpleAlert(this, "No Description!", "You must give a description");
            temp = false;
        }
        if(categories.getText().toString().equals("No Categories Selected")) {
            ((TextView)findViewById(R.id.error_categories)).setText("You must give select at least 1 category");
            findViewById(R.id.error_categories).setVisibility(View.VISIBLE);
            //StaticUtilities.showSimpleAlert(this,"No Categories Selected!", "You must select at least 1 category");
            temp =  false;
        }
        return temp;
    }

    private class BackgroundTask extends AsyncTask <Void, Void, Void> {
        private ProgressDialog dialog;

        public BackgroundTask(UserCreateReportActivity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Submitting Report");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            finish();
        }

        @Override
        protected Void doInBackground(Void... params) {
            //TODO update SQL
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}