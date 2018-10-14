package com.ua.cs495_f18.berthaIRT;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;

import static com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation.RUN_IN_BACKGROUND;

public class AdminLoginActivity extends AppCompatActivity {
    AuthenticationDetails details;
    AuthenticationHandler callback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        callback = new AuthenticationHandler() {
            @Override
            public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
                Toast.makeText(AdminLoginActivity.this, "Congratulations It Works...", Toast.LENGTH_LONG).show();
                startActivity(new Intent(AdminLoginActivity.this, AdminPortalActivity.class));
                finish();
            }

            @Override
            public void getAuthenticationDetails(AuthenticationContinuation continuation, String email) {
                continuation.setAuthenticationDetails(details);
                continuation.continueTask();
            }

            @Override
            public void getMFACode(MultiFactorAuthenticationContinuation continuation) {
                continuation.continueTask();
            }

            @Override
            public void authenticationChallenge(ChallengeContinuation continuation) {
                continuation.continueTask();
            }

            @Override
            public void onFailure(Exception exception) {
                TextView errorMessage = findViewById(R.id.message_invalid_credentials);
                errorMessage.setText(exception.toString());
                errorMessage.setVisibility(View.VISIBLE);
            }
        };

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

        final Button buttonToEnterKey = findViewById(R.id.button_to_enter_key);
        buttonToEnterKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                actionGotoEnterKey();
            }
        });
    }

    private void actionAdminLogin(){
        UtilityInterfaceTools.hideSoftKeyboard(AdminLoginActivity.this);
        String inputEmail = ((EditText) findViewById(R.id.input_admin_email)).getText().toString();
        String inputPassword = ((EditText) findViewById(R.id.input_admin_password)).getText().toString();
        CognitoController.userEmail = inputEmail;
        details = new AuthenticationDetails(inputEmail, inputPassword, null);
        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Handler handler = new Handler(AdminLoginActivity.this.getMainLooper());
                Runnable nextStep;
                try {
                    nextStep = CognitoController.pool.getUser(CognitoController.userEmail).initiateUserAuthentication(details, callback, RUN_IN_BACKGROUND);
                } catch (final Exception e) {
                    nextStep = new Runnable() {
                        @Override
                        public void run() {
                            callback.onFailure(e);
                        }
                    };
                }
                handler.post(nextStep);
            }
        }).start();
    }

    private void actionGotoEnterKey(){
        UtilityInterfaceTools.hideSoftKeyboard(AdminLoginActivity.this);
        startActivity(new Intent(AdminLoginActivity.this, AdminEnterKeyActivity.class));
        //finish();
    }

    private void actionGotoNewGroup(){
        UtilityInterfaceTools.hideSoftKeyboard(AdminLoginActivity.this);
        startActivity(new Intent(AdminLoginActivity.this, AdminCreateGroupActivity.class));
        //finish();
    }
}
