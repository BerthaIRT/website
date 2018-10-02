package com.ua.cs495_f18.berthaIRT;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class UserCreateReportActivity extends AppCompatActivity {
    private ProgressBar spinner;
    Button mCategories;
    TextView mItemSelected;
    Button btnSubmitReport;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_createreport);
        selectCategories();

        btnSubmitReport = (Button) findViewById(R.id.button_send_report);
        btnSubmitReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //findViewById(R.id.userReportLayout).setVisibility(View.GONE);
                BackgroundTask task = new BackgroundTask(UserCreateReportActivity.this);
                task.execute();
            }
        });


    }

    private void selectCategories() {
        mCategories = (Button) findViewById(R.id.button_category_select_continue);
        mItemSelected = (TextView) findViewById(R.id.tvItemSelected);

        listItems = getResources().getStringArray(R.array.category_item);
        checkedItems = new boolean[listItems.length];
        for(int i = 0; i < listItems.length; i++) {
            mUserItems.add(-1);
        }
        mCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(UserCreateReportActivity.this);
                mBuilder.setTitle("Select Appropriate Categories");
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if(isChecked) {
                            mUserItems.set(position,position);
                        }
                        else{
                            mUserItems.set(position,-1);
                        }
                    }
                });

                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        StringBuilder item = new StringBuilder();
                        String prefix = "";
                        for (int i = 0; i < mUserItems.size(); i++) {
                            if (mUserItems.get(i) > -1) {
                                item.append(prefix);
                                prefix = ", ";
                                item.append(listItems[mUserItems.get(i)]);
                            }
                        }
                        mItemSelected.setText(item);
                    }
                });
                mBuilder.setNegativeButton("DISMISS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setNeutralButton("CLEAR ALL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = 0; i < checkedItems.length; i++) {
                            checkedItems[i] = false;
                            mUserItems.clear();
                            mItemSelected.setText("");
                        }
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

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
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}



