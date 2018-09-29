package com.ua.cs495_f18.berthaIRT;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
                actionToNewGroup();
            }
        });

        final Button buttonToEnterKey = findViewById(R.id.button_to_enter_key);
        buttonToEnterKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                actionToEnterKey();
            }
        });
    }

    private void actionAdminLogin(){
        UtilityInterfaceTools.hideSoftKeyboard(AdminLoginActivity.this);
        EditText inputEmail = findViewById(R.id.input_admin_email);
        EditText inputPassword = findViewById(R.id.input_admin_password);

        //if(inputEmail.getText().toString().equals("nsaban@ua.edu") && inputPassword.getText().toString().equals("fuckauburn")){
        if(true) {
            startActivity(new Intent(AdminLoginActivity.this, AdminPortalActivity.class));
            finish();
        }
        else{
            TextView errorMessage = findViewById(R.id.message_invalid_credentials);
            errorMessage.setVisibility(View.VISIBLE);
            inputEmail.setText("");
            inputPassword.setText("");
        }
    }

    private void actionToEnterKey(){
        UtilityInterfaceTools.hideSoftKeyboard(AdminLoginActivity.this);
        startActivity(new Intent(AdminLoginActivity.this, AdminEnterKeyActivity.class));
        finish();
    }

    private void actionToNewGroup(){
        UtilityInterfaceTools.hideSoftKeyboard(AdminLoginActivity.this);
        startActivity(new Intent(AdminLoginActivity.this, AdminCreateGroupActivity.class));
        finish();
    }
}
