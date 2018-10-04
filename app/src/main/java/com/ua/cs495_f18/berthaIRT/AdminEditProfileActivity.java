package com.ua.cs495_f18.berthaIRT;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AdminEditProfileActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etSchoolName;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_editprofile);
        captureInput();
    }

    private void captureInput() {
        etFirstName = (EditText) findViewById(R.id.label_admin_first_name_status);
        etLastName = (EditText) findViewById(R.id.label_admin_last_name_status);
        etSchoolName = (EditText) findViewById(R.id.label_school_name_status);
        btnSubmit = (Button) findViewById(R.id.button_admin_update);

        final String oldFirstName = etFirstName.getText().toString();
        final String oldLastName = etLastName.getText().toString();
        final String oldSchoolName = etSchoolName.getText().toString();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newFirstName = etFirstName.getText().toString();
                String newLastName = etLastName.getText().toString();
                String newSchoolName = etSchoolName.getText().toString();

                String message = null;
                if (!oldFirstName.equals(newFirstName) || !oldLastName.equals(newLastName) || !oldSchoolName.equals(newSchoolName)) {
                    message = "You updated: ";
                }
                if (!oldFirstName.equals(newFirstName))
                    message += "\nFirst Name to: " + newFirstName;
                if (!oldLastName.equals(newLastName))
                    message += "\nLast Name to: " + newFirstName;
                if (!oldSchoolName.equals(newSchoolName))
                    message += "\nSchool Name to: " + newSchoolName;
                displayDialog(message);
            }
        });
    }

    private void displayDialog(String message) {
        //get the school code
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminEditProfileActivity.this);
        builder.setCancelable(true);

        builder.setTitle("Are you sure?");
        if (message != null)
            builder.setMessage(message);
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO Update the Profile
                        startActivity(new Intent(AdminEditProfileActivity.this,AdminPortalActivity.class));
                        //don't allow the app to go back
                        finish();
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
