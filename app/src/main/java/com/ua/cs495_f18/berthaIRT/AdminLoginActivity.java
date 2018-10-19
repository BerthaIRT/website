package com.ua.cs495_f18.berthaIRT;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.mobile.auth.core.StartupAuthResult;
import com.amazonaws.mobile.auth.core.StartupAuthResultHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;

public class AdminLoginActivity extends AppCompatActivity {
    private String inputEmail;
    private String inputPassword;

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
        inputEmail = ((EditText) findViewById(R.id.input_admin_email)).getText().toString();
        inputPassword = ((EditText) findViewById(R.id.input_admin_password)).getText().toString();

        CognitoUser user = StaticUtilities.pool.getUser(inputEmail);
        user.getSessionInBackground(new AuthenticationHandler() {
            @Override
            public void onSuccess(CognitoUserSession userSession) {
                startActivity(new Intent(AdminLoginActivity.this, AdminPortalActivity.class));
            }

            @Override
            public void getAuthenticationDetails(AuthenticationContinuation continuation, String UserId) {
                continuation.setAuthenticationDetails(new AuthenticationDetails(inputEmail, inputPassword, null));
                continuation.continueTask();
            }

            @Override
            public void getMFACode(MultiFactorAuthenticationContinuation continuation) {
                continuation.continueTask();
            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(AdminLoginActivity.this, exception.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void actionGotoNewGroup(){
        StaticUtilities.hideSoftKeyboard(AdminLoginActivity.this);
        startActivity(new Intent(AdminLoginActivity.this, AdminCreateGroupActivity.class));
    }

}
