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

    private String oldFirstName, oldLastName, oldSchoolName;

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

        oldFirstName = etFirstName.getText().toString();
        oldLastName = etLastName.getText().toString();
        oldSchoolName = etSchoolName.getText().toString();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyDialog();
            }
        });
    }

    private void verifyDialog() {
        String newFirstName = etFirstName.getText().toString();
        String newLastName = etLastName.getText().toString();
        String newSchoolName = etSchoolName.getText().toString();

        String title;
        String message = "";
        if (!oldFirstName.equals(newFirstName) || !oldLastName.equals(newLastName) || !oldSchoolName.equals(newSchoolName))
            title = "You updated:\n";
        else
            title = "Nothing was Changed";
        if (!oldFirstName.equals(newFirstName))
            message += "\nFirst Name to: " + newFirstName;
        if (!oldLastName.equals(newLastName))
            message += "\nLast Name to: " + newFirstName;
        if (!oldSchoolName.equals(newSchoolName))
            message += "\nSchool Name to: " + newSchoolName;

        //get the school code
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminEditProfileActivity.this);
        builder.setCancelable(true);

        builder.setTitle(title);
        if (!message.equals(""))
            builder.setMessage(message);
        builder.setPositiveButton("Submit",
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
