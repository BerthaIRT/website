package com.universityofalabama.cs495f2018.berthaIRT;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;

public class AdminLoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        etEmail = findViewById(R.id.input_adminlogin_email);
        etPassword = findViewById(R.id.input_adminlogin_password);
        etEmail.setText("berthairtapp@gmail.com");
        etPassword.setText("111111");

        buttonLogin = findViewById(R.id.button_adminlogin_login);
        buttonLogin.setOnClickListener(v -> actionAdminLogin());

        TextView tvForgot = findViewById(R.id.button_adminlogin_forgot);
        tvForgot.setOnClickListener(v -> initiateForgotPassword());

        final Button buttonToNewGroup = findViewById(R.id.button_goto_newgroup);
        buttonToNewGroup.setOnClickListener(v -> actionGotoNewGroup());

        validateCredentialInput();
        etEmail.addTextChangedListener(StaticUtilities.validator(this::validateCredentialInput));
        etPassword.addTextChangedListener(StaticUtilities.validator(this::validateCredentialInput));

    }

    private void actionAdminLogin(){
        StaticUtilities.hideSoftKeyboard(AdminLoginActivity.this);
        String sEmail = etEmail.getText().toString();
        String sPassword = etPassword.getText().toString();

        JsonObject jay = new JsonObject();
        jay.addProperty("username", sEmail);
        jay.addProperty("password", sPassword);

        Client.net.secureSend("signin", jay.toString(), (r) -> {
            Client.currentUser = sEmail;
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
                TextView errorUnPw = findViewById(R.id.alt_invalid_credentials);
                errorUnPw.setVisibility(View.VISIBLE);
                etPassword.setText("");
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
    private void validateCredentialInput() {
        if((StaticUtilities.isEmailValid(etEmail.getText().toString())) && (StaticUtilities.isPasswordValid(etPassword.getText().toString()))){
            buttonLogin.setAlpha(1);
            buttonLogin.setEnabled(true);
        }
        else{
            buttonLogin.setAlpha(.5f);
            buttonLogin.setEnabled(false);
        }
    }

    private void initiateForgotPassword(){
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.fragment_forgot_password_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialoglayout);
        EditText etForgotEmail = dialoglayout.findViewById(R.id.input_forgotpassword_email);

        builder.setPositiveButton("Confirm", (dialog, which) -> Client.net.secureSend("admin/forgotpassword",etForgotEmail.getText().toString(), (r)->{

        }));
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
