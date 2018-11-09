package com.universityofalabama.cs495f2018.berthaIRT;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.util.CognitoServiceConstants;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;


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

        etEmail.setText("hi@hi.com");
        etPassword.setText("111111");

        bLogin = findViewById(R.id.adminlogin_button_login);
        bLogin.setOnClickListener(x -> actionLogin());

        CardView bSignup = findViewById(R.id.adminlogin_button_signup);
        bSignup.setOnClickListener(x -> actionSignup());
    }

    private void actionLogin() {
        String sEmail = etEmail.getText().toString();
        String sPassword = etPassword.getText().toString();

        Client.net.ctx = AdminLoginActivity.this;

        Client.net.performLogin(sEmail, sPassword, true, r->{
            if(r.equals("INVALID_CREDENTIALS")){
                etPassword.setError("Invalid username or password.");
                etPassword.setText("");
            }
            else if (r.equals("SECURE")) startActivity(new Intent(AdminLoginActivity.this, AdminMainActivity.class));
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

        EditText etNewEmail = v.findViewById(R.id.newgroup_input_email);
        EditText etNewInstitution = v.findViewById(R.id.newgroup_input_institution);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);
        AlertDialog dialog = builder.create();

        v.findViewById(R.id.newgroup_button_signup).setOnClickListener(x->{
            Map<String, String> req = new HashMap<>();
            req.put("newAdmin", etNewEmail.getText().toString());
            req.put("groupName", etNewInstitution.getText().toString());

            Client.net.netSend("/group/new", req, r->{
                if(r.equals("OK")){
                    dialog.dismiss();
                    etEmail.setText(etNewEmail.getText().toString());
                    etPassword.requestFocus();
                    Util.showOkDialog(AdminLoginActivity.this, "Institution Created", "A temporary password has been sent to " + etNewEmail.getText().toString() + " along with further instructions.", null);
                }
            });
        });
        dialog.show();
    }

    private void actionForgot(String s) {
        Client.net.secureSend("admin/forgotpassword",s, (r)->{
            //TODO maybe capture return
        });
    }
}