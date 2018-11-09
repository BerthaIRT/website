package com.universityofalabama.cs495f2018.berthaIRT;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidentityprovider.AmazonCognitoIdentityProviderClient;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client extends AppCompatActivity {

    static String currentUserName;
    static String currentUserGroupID;
    static BerthaNet net;
    static HashMap<String, Report> reportMap;
    static Report activeReport;

    static boolean startOnDashboard = false; //for new admins, maybe could be a pref

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landingpage);
        reportMap = new HashMap<>();
        net = new BerthaNet(this);
//
//        JsonObject studentLogin = Util.readFromUserfile(Client.this);
//        if(studentLogin != null){
//            net.performLogin(this, studentLogin.get("username").getAsString(), studentLogin.get("password").getAsString(), false, x->{
//                if(x.equals("SECURE")) startActivity(new Intent(this, StudentMainActivity.class));
//                finish();
//            });
//        }
//        else{
//            startActivity(new Intent(this, NewUserActivity.class));
//            finish();
//        }

        String[] cats = new String[]{"balls", "drugs", "weed", "guns", "lalalalalallallala"};
        ArrayList<String> s = new ArrayList<>();
        ArrayList<Boolean> b = new ArrayList<>();
        for (String z : cats){
            s.add(z);
            b.add(false);
        }
        new CheckboxDialog(Client.this, b, s, (r)->{}).show();
   }
}