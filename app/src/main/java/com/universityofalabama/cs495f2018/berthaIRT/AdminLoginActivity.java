package com.universityofalabama.cs495f2018.berthaIRT;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.gson.JsonObject;

public class AdminLoginActivity extends AppCompatActivity {

    EditText etEmail;
    EditText etPassword;
    CardView bLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        etEmail = findViewById(R.id.adminlogin_input_email);
        etPassword = findViewById(R.id.adminlogin_input_password);

        bLogin = findViewById(R.id.adminlogin_button_login);
        bLogin.setOnClickListener(x->actionLogin());

        CardView bForgot = findViewById(R.id.adminlogin_button_forgot);
        bForgot.setOnClickListener(x->actionForgot());

        CardView bSignup = findViewById(R.id.adminlogin_button_signup);
        bSignup.setOnClickListener(x->actionSignup());
    }

    private void actionLogin() {
        String sEmail = etEmail.getText().toString();
        String sPassword = etPassword.getText().toString();

        JsonObject jay = new JsonObject();
        jay.addProperty("username", sEmail);
        jay.addProperty("password", sPassword);

        startActivity(new Intent(AdminLoginActivity.this, AdminMainActivity.class));
        Client.net.secureSend("signin", jay.toString(), (r) -> {
            Client.currentUser = sEmail;
            if(r.equals("NEW_PASSWORD_REQUIRED")){
                Client.adminForceNewPassword = true;
                actionCompleteSignUp();
                //TODO: PENIS PENIS PENIS PENIS
            }
            else if(r.equals("HELL YEAH BITCHES THIS SHIT WORKS WOOOOO")){
                startActivity(new Intent(AdminLoginActivity.this, AdminMainActivity.class));
                finish();
            }
            else if(!r.equals("HELL YEAH BITCHES THIS SHIT WORKS WOOOOO")){
                etPassword.setError("Invalid credentials.");
                etPassword.setText("");
            }
        });
    }

    private void actionSignup() {
        LayoutInflater flater = getLayoutInflater();
        View v = flater.inflate(R.layout.dialog_admin_signuptype, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);
        AlertDialog dialog = builder.create();

        v.findViewById(R.id.signuptype_button_newgroup).setOnClickListener(x -> {
            dialog.dismiss();
            actionNewGroup();
        });
        v.findViewById(R.id.signuptype_button_existinggroup).setOnClickListener(x -> {
            dialog.dismiss();
            Util.showOkDialog(AdminLoginActivity.this, null, "To join an existing institution as an administrator, you must be invited by an existing administrator of that group.\n\nAdministrators may invite others via email address.", null);
        });
        dialog.show();
    }

    private void actionNewGroup() {
        LayoutInflater flater = getLayoutInflater();
        View v = flater.inflate(R.layout.dialog_admin_newgroup, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);
        AlertDialog dialog = builder.create();
        dialog.show();
        v.findViewById(R.id.newgroup_button_signup).setOnClickListener(x -> {
            dialog.dismiss();
            JsonObject jay = new JsonObject();
            jay.addProperty("email", ((EditText) v.findViewById(R.id.inviteadmin_input_email)).getText().toString());
            jay.addProperty("name", ((EditText) v.findViewById(R.id.inviteadmin_input_institution)).getText().toString());
            Client.net.secureSend("admin/creategroup", jay.toString(), (r)->{
                if(r.equals("AIGHT LOL")) {
                    Util.showOkDialog(AdminLoginActivity.this, null, "A passcode was sent to your email", null);
                }
            });
        });
    }

    private void actionCompleteSignUp() {
        LayoutInflater flater = getLayoutInflater();
        View v = flater.inflate(R.layout.dialog_admin_completesignup, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);
        AlertDialog dialog = builder.create();
        dialog.show();

        v.findViewById(R.id.completesignup_button_confirm).setOnClickListener(v1 -> actionUpdate(v));



    }

    private void actionUpdate(View v) {
        EditText etName = v.findViewById(R.id.inviteadmin_input_name);
        EditText etPass1 = v.findViewById(R.id.completesignup_input_password1);
        EditText etPass2 = v.findViewById(R.id.completesignup_input_password2);

        String sName = etName.getText().toString();
        String sPass1 = etPass1.getText().toString();
        String sPass2 = etPass2.getText().toString();

        if(sName.equals(""))
            etName.setError("You must provide a name.");
        if(!Util.isPasswordValid(sPass1)) {
            etPass1.setError("Your password must be at least 8 characters long and must contain at least a letter and either a number or special character.");
        }
        else if(!sPass1.equals(sPass2)){
            etPass2.setError("Passwords do not match.");
        }
        else {
            JsonObject jay = new JsonObject();
            jay.addProperty("name", sName);
            jay.addProperty("newpassword", sPass1);
            Client.net.secureSend("admin/updateinfo", jay.toString(), (r)->{
                if(r.equals("ALL GOOD HOMIE"))
                    Util.showOkDialog(this, null, "Password was updated", this::launchToAdminMain);
            });
        }
    }

    private void launchToAdminMain() {
        startActivity(new Intent(AdminLoginActivity.this, AdminMainActivity.class));
        finish();
    }

    private void actionForgot() {
    }
}