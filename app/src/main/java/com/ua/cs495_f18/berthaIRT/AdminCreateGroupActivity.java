package com.ua.cs495_f18.berthaIRT;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AdminCreateGroupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_creategroup);

        Button buttonCreate = findViewById(R.id.button_create_new_group);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                actionCreateNewGroup();
            }
        });
    }

    private void actionCreateNewGroup(){
        UtilityInterfaceTools.hideSoftKeyboard(AdminCreateGroupActivity.this);
        EditText inputEmail = findViewById(R.id.input_new_group_email);
        EditText inputName = findViewById(R.id.input_new_group_name);
        String sEmail = inputEmail.getText().toString();
        String sName = inputName.getText().toString();

        AlertDialog.Builder b = new AlertDialog.Builder(AdminCreateGroupActivity.this);
        b.setCancelable(true);
        b.setTitle("Check Your Inbox");
        b.setMessage("An administration key has been sent to " + sEmail + " along with the new general access code for " + sName + ".");
        b.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(AdminCreateGroupActivity.this, AdminInviteActivity.class));
                        finish();
                    }
                });
        AlertDialog confirmationDialog = b.create();
        confirmationDialog.show();
    }
}