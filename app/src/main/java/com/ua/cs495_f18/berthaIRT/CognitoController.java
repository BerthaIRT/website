package com.ua.cs495_f18.berthaIRT;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amazonaws.regions.Regions;

public class CognitoController extends Application {

    static CognitoUserPool pool;
    static CognitoUserSession session;
    static AWSCredentials credentials;
    //static AWSCognitoIdentityProvider idClient;
    static String userEmail;
    static PinpointManager pinpointManager;

    public void onCreate(){
        super.onCreate();
        AWSMobileClient.getInstance().initialize(this).execute();
        Toast.makeText(this, "Connected to AWS", Toast.LENGTH_SHORT).show();
        pool = new CognitoUserPool(this,
                "us-east-1_h7x5gfd1d",
                "13jkbnq3vrvjh9n2m09tekbld5",
                "1bcklspgq026c7llcp9q2tqoorh1ckv1vcqd24oh7ikjlnkob6nm",
                new ClientConfiguration(),
                Regions.US_EAST_1); //waaaaay down the line this may be insecure

        AWSConfiguration awsConfiguration = new AWSConfiguration(this);
        if (IdentityManager.getDefaultIdentityManager() == null) {
            final IdentityManager identityManager = new IdentityManager(getApplicationContext(), awsConfiguration);
            IdentityManager.setDefaultIdentityManager(identityManager);
        }
    }
}
