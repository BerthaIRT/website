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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;

public class AdminLoginActivity extends AppCompatActivity {
    private String inputEmail;
    private String inputPassword;
    private EditText et1, et2;
    private Button buttonLogin;
    private TextView forgotPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        et1 = findViewById(R.id.input_admin_email);
        et2 = findViewById(R.id.input_admin_password);

        buttonLogin = findViewById(R.id.button_to_admin_portal);
        buttonLogin.setOnClickListener(v -> actionAdminLogin());

        forgotPass = findViewById(R.id.select_forgotPassword);
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateForgotPassword();
            }
        });

        final Button buttonToNewGroup = findViewById(R.id.button_to_new_group);
        buttonToNewGroup.setOnClickListener(v -> actionGotoNewGroup());

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
                TextView errorUnPw = findViewById(R.id.message_invalid_credentials);
                errorUnPw.setVisibility(View.VISIBLE);
                ((EditText) findViewById(R.id.input_admin_password)).setText(""); // resets password field text
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
            if((StaticUtilities.isEmailValid(et1.getText().toString())) && (StaticUtilities.isPasswordValid(et2.getText().toString()))){
                buttonLogin.setAlpha(1);
                buttonLogin.setEnabled(true);
            }
            else {
                if(StaticUtilities.isEmailValid(et1.getText().toString())) { //REMOVE THIS IF STATEMENT WHEN TEMP PASSWORD ISSUE IS FIXED... and replace Else with this if's else.
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

    private void initiateForgotPassword(){
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.activity_forgot_password_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialoglayout);

        EditText forgotPassEmail = findViewById(R.id.input_forgot_password_email);

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //PERFORM THE RESETPASSWORD FUNCTION
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // DO NOTHING
            }
        });

        TextView editTextTitle = (TextView) dialoglayout.findViewById(R.id.label_forgotPassword_admin_email);
        TextView forgotPasswordMessage = (TextView) dialoglayout.findViewById(R.id.label_forgotPassword_message);
        editTextTitle.setText("E-mail"); // Will Check SQL
        forgotPasswordMessage.setText("Enter your E-mail and a temporary code for you to reset your password will be sent to your email.");

        builder.show();
    }
}
