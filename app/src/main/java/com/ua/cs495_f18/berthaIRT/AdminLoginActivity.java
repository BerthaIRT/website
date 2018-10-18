package com.ua.cs495_f18.berthaIRT;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminLoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        final Button buttonLogin = findViewById(R.id.button_to_admin_portal);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                actionAdminLogin();
            }
        });

        final Button buttonToNewGroup = findViewById(R.id.button_to_new_group);
        buttonToNewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                actionGotoNewGroup();
            }
        });
    }

    private void actionAdminLogin(){
        StaticUtilities.hideSoftKeyboard(AdminLoginActivity.this);
        String inputEmail = ((EditText) findViewById(R.id.input_admin_email)).getText().toString();
        String inputPassword = ((EditText) findViewById(R.id.input_admin_password)).getText().toString();
        Toast.makeText(AdminLoginActivity.this, "Congratulations It Works...", Toast.LENGTH_LONG).show();
        startActivity(new Intent(AdminLoginActivity.this, AdminPortalActivity.class));
        finish();
    }

    private void actionGotoNewGroup(){
        StaticUtilities.hideSoftKeyboard(AdminLoginActivity.this);
        startActivity(new Intent(AdminLoginActivity.this, AdminCreateGroupActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AdminLoginActivity.this, UnregisteredPortalActivity.class));
        finish();
    }

}
