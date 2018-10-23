package com.ua.cs495_f18.berthaIRT;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class UserCreateReportActivity extends AppCompatActivity {

    EditText selectDate,selectTime;
    TextView description;
    private int mYear, mMonth, mDay, mHour, mMinute;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_createreport);

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
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) ->
                selectDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year), mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void actionSelectTime() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) ->
                selectTime.setText(hourOfDay + ":" + minute), mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void actionSelectCategories(){
        AlertDialog.Builder b = new AlertDialog.Builder(UserCreateReportActivity.this);

        final String[] categoryItems = getResources().getStringArray(R.array.category_item);
        final boolean [] checkedItems = new boolean[categoryItems.length];

        b.setTitle("Select Categories");
        b.setCancelable(false);

        b.setMultiChoiceItems(categoryItems, checkedItems, (dialog, position, isChecked) -> {
            if(isChecked)
                checkedItems[position] = true;
            else
                checkedItems[position] = false;
        });

        b.setPositiveButton("OK", (dialogInterface, x) -> {
            StringBuilder sb = new StringBuilder();
            for (int i=0; i<checkedItems.length; i++)
                if(checkedItems[i])
                    sb.append(categoryItems[i] + ", ");
            String s = sb.toString().substring(0, sb.length()-2); //remove last comma
            ((TextView) findViewById(R.id.label_selected_categories)).setText(s);
            dialogInterface.dismiss();
        });

        b.setNegativeButton("CANCEL", (dialogInterface, x) -> dialogInterface.dismiss());

        b.setNeutralButton("CLEAR ALL", (dialogInterface, x) -> {
            for (int i=0; i<checkedItems.length; i++) {
                checkedItems[i] = false;
                ((TextView) findViewById(R.id.label_selected_categories)).setText("");
            }
        });

        b.create().show();
    }

    private void actionSubmitReport() {
        BackgroundTask task = new BackgroundTask(UserCreateReportActivity.this);
        task.execute();
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