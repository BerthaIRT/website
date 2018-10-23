package com.ua.cs495_f18.berthaIRT;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class AdminLoginActivity extends AppCompatActivity {
    private String inputEmail;
    private String inputPassword;
    private EditText et1, et2;
    private Button buttonLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        et1 = (EditText) findViewById(R.id.input_admin_email);
        et2 = (EditText) findViewById(R.id.input_admin_password);

        buttonLogin = findViewById(R.id.button_to_admin_portal);
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

        checkValidation();
        et1.addTextChangedListener(mWatcher);
        et2.addTextChangedListener(mWatcher);

    }

    private void actionAdminLogin(){
        StaticUtilities.hideSoftKeyboard(AdminLoginActivity.this);
        inputEmail = ((EditText) findViewById(R.id.input_admin_email)).getText().toString();
        inputPassword = ((EditText) findViewById(R.id.input_admin_password)).getText().toString();

        JsonObject jay = new JsonObject();
        jay.addProperty("username", inputEmail);
        jay.addProperty("password", inputPassword);

        Client.net.secureSend("signin", jay.toString(), (r) -> {
            Client.currentUser = inputEmail;
            if(r.equals("NEW_PASSWORD_REQUIRED")){
                Client.adminForceNewPassword = true;
                startActivity(new Intent(AdminLoginActivity.this, AdminEditProfileActivity.class));
                finish();
            }
            else if(r.equals("HELL YEAH BITCHES THIS SHIT WORKS WOOOOO")){
                startActivity(new Intent(AdminLoginActivity.this, AdminPortalActivity.class));
                finish();
            }
            else if(!r.equals("HELL YEAH BITCHES THIS SHIT WORKS WOOOOO")){
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminLoginActivity.this);
                builder.setTitle("Invalid Username/Password");
                builder.setMessage("The Username and/or Password you entered was incorrect.");
                builder.setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                builder.show();
            }
        });
    }

    private void saveLoginInfo(String email, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Unm",email);
        editor.putString("Psw",password);
        editor.apply();
    }

    private void actionGotoNewGroup(){
        StaticUtilities.hideSoftKeyboard(AdminLoginActivity.this);
        startActivity(new Intent(AdminLoginActivity.this, AdminCreateGroupActivity.class));
    }

    //checks for input in password and email fields and makes Login button clickable vs unclickable based on data.
    private void checkValidation() {
        if ((TextUtils.isEmpty(et1.getText())) || (TextUtils.isEmpty(et2.getText()))){
            buttonLogin.setAlpha(.5f);
            buttonLogin.setEnabled(false);
        }
        else {
            if((isEmailValid(et1.getText().toString())) && (StaticUtilities.validPassword(et2.getText().toString()))){
                buttonLogin.setAlpha(1);
                buttonLogin.setEnabled(true);
            }
            else {
                if(isEmailValid(et1.getText().toString())) { //REMOVE THIS IF STATEMENT WHEN TEMP PASSWORD ISSUE IS FIXED... and replace Else with this if's else.
                    buttonLogin.setAlpha(1); //
                    buttonLogin.setEnabled(true);//
                }
                else{
                    buttonLogin.setAlpha(.5f); //TODO ENABLE THIS ONCE temp password issue is addressed. buttonLogin.setAlpha(.5f);
                    buttonLogin.setEnabled(false);//TODO ENABLE THIS ONCE temp password issue is addressed. buttonLogin.setEnabled(false);
                }
            }
        }
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private TextWatcher mWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkValidation();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
}
