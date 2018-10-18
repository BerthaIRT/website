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

    //TODO NEEDS CLEANUP

    private TextView tvTags, tvAdminsSelected, tvKeyWordsSelected, tvCategoriesSelected;

    private String status;

    private String oldStatus, oldAdmins, oldKeys, oldCategories;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_editreportdetails);

        RadioGroup radioStatusGroup = (RadioGroup) findViewById(R.id.radio_group_status);
        ImageView infoButton = (ImageView) findViewById(R.id.button_viewreport_edittags);
        tvAdminsSelected = (TextView) findViewById(R.id.label_editreport_assignedto_status);
        TextView tvEditAssignedToButton = (TextView) findViewById(R.id.select_edit_assignedto);
        tvKeyWordsSelected = (TextView) findViewById(R.id.label_editreport_keywords_status);
        TextView tvEditKeyWords = (TextView) findViewById(R.id.select_edit_keywords);
        tvCategoriesSelected = (TextView) findViewById(R.id.label_editreport_categories_status);
        TextView tvEditCategories = (TextView) findViewById(R.id.select_edit_categories);
        tvTags = (TextView) findViewById(R.id.label_editreport_tags_status);


        //set the unopened button selected
        //TODO select the proper radio button
        RadioButton radioStatusButton = (RadioButton) findViewById(R.id.rb_status_unopened);
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

        Button btnCancel= (Button) findViewById(R.id.btnCancel);
        Button btnSubmit = (Button) findViewById(R.id.btnSubmit);
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
        android.support.v7.app.AlertDialog.Builder b = new android.support.v7.app.AlertDialog.Builder(AdminEditReportActivity.this);

        //TODO get from list of Admins
        final String[] adminItems = {
                "Johnathan",
                "Jake",
                "Scott",
                "Fahad",
                "Lucy",
                "Jason",
                "TestName1",
                "Test Name 2"
        };

        //Pre check the appropriate boxes
        final boolean[] checkedAdminItems = getPreChecked(adminItems,tvAdminsSelected.getText().toString());

        b.setTitle("Select Admins");
        b.setCancelable(false);

        b.setMultiChoiceItems(adminItems, checkedAdminItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                if(isChecked)
                    checkedAdminItems[position] = true;
                else
                    checkedAdminItems[position] = false;
            }
        });

        b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int x) {
                StringBuilder sb = new StringBuilder();
                String prefix = "";
                for (int i=0; i<checkedAdminItems.length; i++) {
                    if (checkedAdminItems[i]) {
                        sb.append(prefix);
                        prefix = ", ";
                        sb.append(adminItems[i]);
                    }
                }
                if(sb.toString().equals(""))
                    sb.append("No Admins Assigned");
                tvAdminsSelected.setText(sb);
                updateTags();
                dialogInterface.dismiss();
            }
        });

        b.setNegativeButton("DISMISS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int x) {
                dialogInterface.dismiss();
            }
        });

        b.create().show();
    }

    //For updating the Tags each time a key word or category has changed
    private void updateTags() {
        StringBuilder sb = new StringBuilder();

        String key = tvKeyWordsSelected.getText().toString();
        String cat = tvCategoriesSelected.getText().toString();
        //if it's default then append
        if(key.equals("No Key Words Assigned") && cat.equals("No Categories Selected"))
            sb.append("No Tags Assigned");
        //if key words have been changed
        else if (!key.equals("No Key Words Assigned")) {
            sb.append(tvKeyWordsSelected.getText());
            if (!cat.equals("No Categories Selected")) {
                sb.append(", ");
                sb.append(cat);
            }
        }
        //not key words but categories have been changed
        else if (!cat.equals("No Categories Selected"))
            sb.append(cat);
        tvTags.setText(sb);

    }

    private void keyWordsSelected() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Key words separated by commas");
        final EditText input = new EditText(this);
        input.setText(tvKeyWordsSelected.getText());
        final String defaultString = "No Key Words Assigned";
        //if the default is there then make the text appear as nothing
        if(input.getText().toString().equals(defaultString)) {
            input.setText("");
        }
        input.setSelection(input.getText().length());
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //If the input is empty set it to default
                if(input.getText().toString().trim().length() == 0)
                    tvKeyWordsSelected.setText(defaultString);
                else
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
        android.support.v7.app.AlertDialog.Builder b = new android.support.v7.app.AlertDialog.Builder(AdminEditReportActivity.this);

        final String[] categoryItems = getResources().getStringArray(R.array.category_item);

        //Pre check the appropriate boxes
        final boolean[] checkedCategoryItems = getPreChecked(categoryItems,tvCategoriesSelected.getText().toString());

        b.setTitle("Select Categories");
        b.setCancelable(false);

        b.setMultiChoiceItems(categoryItems, checkedCategoryItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                if(isChecked)
                    checkedCategoryItems[position] = true;
                else
                    checkedCategoryItems[position] = false;
            }
        });

        b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int x) {
                StringBuilder sb = new StringBuilder();
                String prefix = "";
                for (int i=0; i<checkedCategoryItems.length; i++) {
                    if (checkedCategoryItems[i]) {
                        sb.append(prefix);
                        prefix = ", ";
                        sb.append(categoryItems[i]);
                    }
                }
                if(sb.toString().equals(""))
                    sb.append("No Categories Selected");
                tvCategoriesSelected.setText(sb);
                updateTags();
                dialogInterface.dismiss();
            }
        });

        b.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int x) {
                dialogInterface.dismiss();
            }
        });
        b.create().show();
    }

    //Function that parses the admin/category fields to figure out what
    //should be selected on the dialog box
    private boolean[] getPreChecked(String[] items, String selected) {
        //Creates an array of each item that is selected
        String[] array = selected.split("\\s*,\\s*");

        boolean[] checkedItems = new boolean[items.length];

        //read through the array and see if it matches with the items
        for(int i = 0; i < checkedItems.length; i++) {
            for(int j = 0; j < array.length; j++) {
                if (items[i].equals(array[j]))
                    checkedItems[i] = true;
            }
        }



        return checkedItems;
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
