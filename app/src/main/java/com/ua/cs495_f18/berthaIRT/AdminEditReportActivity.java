package com.ua.cs495_f18.berthaIRT;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdminEditReportActivity extends AppCompatActivity {

    private Button btnCancel, btnSubmit;
    private RadioGroup radioStatusGroup;
    private RadioButton radioStatusButton;
    private ImageView infoButton;
    private TextView tvTags;
    private TextView tvEditAssignedToButton, tvAdminsSelected;
    private TextView tvEditKeyWords, tvKeyWordsSelected;
    private TextView tvEditCategories, tvCategoriesSelected;

    String[] listAdminItems;
    boolean[] checkedAdminItems;
    ArrayList<Integer> mAdminItems = new ArrayList<>();
    String[] listCategoryItems;
    boolean[] checkedCategoryItems;
    ArrayList<Integer> mCategoryItems = new ArrayList<>();

    private String status;

    private String oldStatus, oldAdmins, oldKeys, oldCategories;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_editreportdetails);

        radioStatusGroup = (RadioGroup) findViewById(R.id.radio_group_status);
        infoButton = (ImageView) findViewById(R.id.button_viewreport_edittags);
        tvAdminsSelected = (TextView) findViewById(R.id.label_editreport_assignedto_status);
        tvEditAssignedToButton = (TextView) findViewById(R.id.select_edit_assignedto);
        tvKeyWordsSelected = (TextView) findViewById(R.id.label_editreport_keywords_status);
        tvEditKeyWords = (TextView) findViewById(R.id.select_edit_keywords);
        tvCategoriesSelected = (TextView) findViewById(R.id.label_editreport_categories_status);
        tvEditCategories = (TextView) findViewById(R.id.select_edit_categories);
        tvTags = (TextView) findViewById(R.id.label_editreport_tags_status);


        //set the unopened button selected
        //TODO select the proper radio button
        radioStatusButton = (RadioButton) findViewById(R.id.rb_status_unopened);
        radioStatusButton.setChecked(true);

        status = "Unopened";
        //Get the original values
        oldStatus = "Unopened";
        oldAdmins = tvAdminsSelected.getText().toString();
        oldKeys = tvKeyWordsSelected.getText().toString();
        oldCategories = tvCategoriesSelected.getText().toString();

        //listen for changes
        radioStatusGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                setStatus(checkedId);
            }
        });

        //For producing Info Dialog Box
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDialog();
            }
        });

        //For producing Admin dialog box
        tvEditAssignedToButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminsAssignedDialog();
            }
        });

        updateTags();
        //For producing key word input box
                tvEditKeyWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyWordsSelected();
            }
        });

        //For producing categories dialog box
        tvEditCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoriesSelected();
            }
        });

        btnCancel= (Button) findViewById(R.id.btnCancel);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Before finish, will send the current data fields to SQL to be overwritten.
                //getEdits();
                verifyDialog();
            }

        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setStatus(int i) {
        switch (i) {
            case R.id.rb_status_unopened:
                status = "Unopened";
                break;
            case R.id.rb_status_open:
                status = "Open";
                break;
            case R.id.rb_status_closed:
                status = "Closed";
                break;
        }
    }

    private void  adminsAssignedDialog () {
        listAdminItems = getResources().getStringArray(R.array.admins_item);
        checkedAdminItems = new boolean[listAdminItems.length];
        for(int i = 0; i < listAdminItems.length; i++) {
            mAdminItems.add(-1);
        }
        tvEditAssignedToButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v7.app.AlertDialog.Builder mBuilder = new android.support.v7.app.AlertDialog.Builder(AdminEditReportActivity.this);
                mBuilder.setTitle("Select Admins to Assign them");
                mBuilder.setMultiChoiceItems(listAdminItems, checkedAdminItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if(isChecked) {
                            mAdminItems.set(position,position);
                        }
                        else{
                            mAdminItems.set(position,-1);
                        }
                    }
                });

                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        StringBuilder item = new StringBuilder();
                        String prefix = "";
                        for (int i = 0; i < mAdminItems.size(); i++) {
                            if (mAdminItems.get(i) > -1) {
                                item.append(prefix);
                                prefix = ", ";
                                item.append(listAdminItems[mAdminItems.get(i)]);
                            }
                        }
                        tvAdminsSelected.setText(item);
                    }
                });
                mBuilder.setNegativeButton("DISMISS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                android.support.v7.app.AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

    }

    //For updating the Tags each time a key word or category has changed
    private void updateTags() {
        StringBuilder item = new StringBuilder();
        item.append(tvKeyWordsSelected.getText());
        item.append(", ");
        item.append(tvCategoriesSelected.getText());
        tvTags.setText(item);
        Toast.makeText(getApplicationContext(), "Update Run", Toast.LENGTH_SHORT).show();

    }

    private void keyWordsSelected() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Key words separated by commas");
        final EditText input = new EditText(this);
        input.setText(tvKeyWordsSelected.getText());
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvKeyWordsSelected.setText(input.getText().toString());
                updateTags();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void categoriesSelected() {
        listCategoryItems = getResources().getStringArray(R.array.category_item);
        checkedCategoryItems = new boolean[listCategoryItems.length];
        for(int i = 0; i < listCategoryItems.length; i++) {
            mCategoryItems.add(-1);
        }
        tvEditCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v7.app.AlertDialog.Builder mBuilder = new android.support.v7.app.AlertDialog.Builder(AdminEditReportActivity.this);
                mBuilder.setTitle("Select Appropriate Categories");
                mBuilder.setMultiChoiceItems(listCategoryItems, checkedCategoryItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if(isChecked)
                            mCategoryItems.set(position,position);
                        else
                            mCategoryItems.set(position,-1);
                    }
                });

                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        StringBuilder item = new StringBuilder();
                        String prefix = "";
                        for (int i = 0; i < mCategoryItems.size(); i++) {
                            if (mCategoryItems.get(i) > -1) {
                                item.append(prefix);
                                prefix = ", ";
                                item.append(listCategoryItems[mCategoryItems.get(i)]);
                            }
                        }
                        tvCategoriesSelected.setText(item);
                        updateTags();
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
                        for (int i = 0; i < checkedCategoryItems.length; i++) {
                            checkedCategoryItems[i] = false;
                            mCategoryItems.clear();
                            tvCategoriesSelected.setText("");
                        }
                    }
                });
                android.support.v7.app.AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
    }

    private void InfoDialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(AdminEditReportActivity.this);
        b.setCancelable(true);
        b.setMessage("Tags are your assigned key words and the selected categories\nKey words might be people, places or events");
        b.setPositiveButton("OK",null);
        AlertDialog confirmationDialog = b.create();
        confirmationDialog.show();
    }

    //function to tell the user what changed
    private void verifyDialog() {
        String title;
        String message = "";

        String newStatus = status;
        String newAdmins = tvAdminsSelected.getText().toString();
        String newKeys = tvKeyWordsSelected.getText().toString();
        String newCategories = tvCategoriesSelected.getText().toString();

        if (!oldStatus.equals(newStatus) || !oldAdmins.equals(newAdmins) || !oldKeys.equals(newKeys) || !oldCategories.equals(newCategories))
            title = "You updated:\n";
        else
            title = "You changed nothing";
        if (!oldStatus.equals(newStatus))
            message += "Status to: " + newStatus;
        if (!oldAdmins.equals(newAdmins))
            message += "Admins Assigned to: " + newAdmins;
        if (!oldKeys.equals(newKeys))
            message += "Key words to: " + newKeys;
        if (!oldCategories.equals(newCategories))
            message += "Categories to: " + newCategories;
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminEditReportActivity.this);
        builder.setCancelable(true);

        builder.setTitle(title);
        if (!message.equals(""))
            builder.setMessage(message);
        builder.setPositiveButton("Submit",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO Update the Profile
                        BackgroundTask task = new BackgroundTask(AdminEditReportActivity.this);
                        task.execute();
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class BackgroundTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;

        public BackgroundTask(AdminEditReportActivity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Submitting Edits");
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
