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

import org.w3c.dom.Text;

import java.util.Calendar;

public class UserCreateReportActivity extends AppCompatActivity {

    private SeekBar threat;
    private EditText selectDate;
    private EditText selectTime;
    private EditText location;
    private TextView categories;
    private TextView description;
    private TextView errorDate;
    private TextView errorLocaiton;
    private TextView errorDescription;
    private TextView errorCategory;

    private ReportObject reportObject;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_createreport);

        threat = findViewById(R.id.input_threat_slider);
        categories = findViewById(R.id.label_selected_categories);
        location = findViewById(R.id.input_incident_location);
        description = findViewById(R.id.input_incident_description);
        errorDate = findViewById(R.id.error_date_time);
        errorLocaiton = findViewById(R.id.error_location);
        errorDescription = findViewById(R.id.error_description);
        errorCategory = findViewById(R.id.error_categories);

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
        if(!isRequiredInput(true)) return;
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


    private boolean matchesTextView(TextView tv, String input) {
        return tv.getText().toString().equals(input);
    }

    private boolean isRequiredInput(boolean setView) {
        boolean valid = true;

        if(matchesTextView(selectDate,"") || matchesTextView(selectTime,"")) {
            if(!setView)
                errorDate.setVisibility(View.GONE);
            else {
                if (matchesTextView(selectDate,""))
                    errorDate.setText("You must select a date");
                if(matchesTextView(selectTime,""))
                    errorDate.setText("You must select a time");
                if (matchesTextView(selectDate,"") && matchesTextView(selectTime,""))
                    errorDate.setText("You must select a date and time");
                errorDate.setVisibility(View.VISIBLE);
            }
            valid = false;
        }
        else
            errorLocaiton.setVisibility(View.GONE);

        if (matchesTextView(location,"")) {
            if(setView)
                errorLocaiton.setVisibility(View.VISIBLE);
            else
                errorLocaiton.setVisibility(View.GONE);
            valid = false;
        }
        else
            errorLocaiton.setVisibility(View.GONE);

        if (matchesTextView(description,"")) {
            if(setView)
                errorDescription.setVisibility(View.VISIBLE);
            else
                errorDescription.setVisibility(View.GONE);
            valid = false;
        }
        else
            errorDescription.setVisibility(View.GONE);

        if(matchesTextView(categories,"No Categories Selected")) {
            if(setView) errorCategory.setVisibility(View.VISIBLE);
            else
                errorCategory.setVisibility(View.GONE);
            valid =  false;
        }
        else
            errorCategory.setVisibility(View.GONE);
        return valid;
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