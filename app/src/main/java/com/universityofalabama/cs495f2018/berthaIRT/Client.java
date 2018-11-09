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

        net = new BerthaNet(this);
        reportMap = new HashMap<>();


        startActivity(new Intent(this, AdminLoginActivity.class));

        //Todo: check login
    }

    public static void updateReportMap(){
        reportMap = new HashMap<>();


        String date = new SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());

        //set the incidentTimeStamp
        List<String> fakeCats = new ArrayList<String>();
        List<String> fakeTags = new ArrayList<>();
        fakeCats.add("Drugs");
        fakeCats.add("WEED");
        fakeTags.add("TSET");
        fakeTags.add("Jim");
        Report r1 = new Report("1000", "Description A", "3", fakeCats);
        r1.incidentTimeStamp = date + " " + time;
        r1.location="Location A";
        r1.tags = fakeTags;
        fakeCats = new ArrayList<String>();
        fakeTags = new ArrayList<>();
        fakeCats.add("Bullying");
        fakeCats.add("Sexual shit");
        fakeTags.add("Daddy!");
        Report r2 = new Report("1001", "Description B", "4", fakeCats);
        r2.incidentTimeStamp = date + " " + time;
        r2.location="Location B";
        r2.tags = fakeTags;
        fakeCats = new ArrayList<String>();
        fakeTags = new ArrayList<>();
        fakeCats.add("fuck");
        fakeCats.add("jim");
        fakeTags.add("in the butt");
        Report r3 = new Report("1002", "Description C", "9", fakeCats);
        r3.incidentTimeStamp = date + " " + time;
        r3.location="Location C";
        r3.tags = fakeTags;


        for(Report r : new Report[]{r1, r2, r3})
            reportMap.put(r.reportId, r);
    }
}