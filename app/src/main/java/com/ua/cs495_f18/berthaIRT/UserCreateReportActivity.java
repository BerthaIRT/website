package com.ua.cs495_f18.berthaIRT;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class UserCreateReportActivity extends AppCompatActivity {
    boolean[] checkedItems;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_createreport);

        Button mCategories = findViewById(R.id.button_show_category_select);
        mCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionSelectCategories();
            }
        });

        Button btnSubmitReport = findViewById(R.id.button_send_report);
        btnSubmitReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionSubmitReport();
            }
        });

    }

    private void actionSelectCategories(){
        AlertDialog.Builder b = new AlertDialog.Builder(UserCreateReportActivity.this);

        final String[] categoryItems = getResources().getStringArray(R.array.category_item);
        checkedItems = new boolean[categoryItems.length];

        b.setTitle("Select Categories");
        b.setCancelable(false);

        b.setMultiChoiceItems(categoryItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                if(isChecked)
                    checkedItems[position] = true;
                else
                    checkedItems[position] = false;
            }
        });

        b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int x) {
                StringBuilder sb = new StringBuilder();
                for (int i=0; i<checkedItems.length; i++)
                    if(checkedItems[i])
                        sb.append(categoryItems[i] + ", ");
                String s = sb.toString().substring(0, sb.length()-2); //remove last comma
                ((TextView) findViewById(R.id.label_selected_categories)).setText(s);
                dialogInterface.dismiss();
            }
        });

        b.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int x) {
                dialogInterface.dismiss();
            }
        });

        b.setNeutralButton("CLEAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int x) {
                for (int i=0; i<checkedItems.length; i++)
                    checkedItems[i] = false;
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