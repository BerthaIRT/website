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

    static String currentUser;
    static BerthaNet net;
    static HashMap<String, Report> reportMap = new HashMap<>();
    static Report activeReport;
    static CognitoUserPool pool;
    static CognitoUserSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        net = new BerthaNet(this);

        AWSMobileClient.getInstance().initialize(this).execute();
        AWSConfiguration awsConfiguration = new AWSConfiguration(this);
        if (IdentityManager.getDefaultIdentityManager() == null) {
            final IdentityManager identityManager = new IdentityManager(getApplicationContext(), awsConfiguration);
            IdentityManager.setDefaultIdentityManager(identityManager);
        }
        pool = new CognitoUserPool(this, awsConfiguration);



        startActivity(new Intent(this, AdminMainActivity.class));

        //Todo: check login
    }

    public static void updateReportMap(){
        String date = new SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());

        //set the incidentTimeStamp
        List<String> fakeCats = new ArrayList<String>();
        List<String> fakeTags = new ArrayList<>();
        fakeCats.add("Alcohol");
        Report r1 = new Report("1000", "Description A", "3", fakeCats);
        r1.incidentTimeStamp = date + " " + time;
        r1.location="Location A";
        r1.tags = fakeTags;
        fakeCats = new ArrayList<String>();
        fakeTags = new ArrayList<>();
        fakeCats.add("Alcohol");
        fakeCats.add("Bullying");
        Report r2 = new Report("1001", "Description B", "4", fakeCats);
        r2.incidentTimeStamp = date + " " + time;
        r2.location="Location B";
        r2.tags = fakeTags;
        fakeCats = new ArrayList<String>();
        fakeTags = new ArrayList<>();
        fakeCats.add("Alcohol");
        fakeCats.add("Bullying");
        Report r3 = new Report("1002", "Description C", "9", fakeCats);
        r3.incidentTimeStamp = date + " " + time;
        r3.location="Location C";
        r3.tags = fakeTags;


        for(Report r : new Report[]{r1, r2, r3})
            reportMap.put(r.reportId, r);
/*        reportMap = new HashMap<>();
        net.secureSend("report/getall", null, (r)->{
            JsonObject jay = net.jp.parse(r).getAsJsonObject();
            for(Map.Entry<String, JsonElement> e : jay.entrySet())
                reportMap.put(e.getKey(), net.gson.fromJson(e.getValue().getAsString(), Report.class));
        });*/
    }

    public static boolean updateReportMap(Report report) {
        //has to be atomic because it's updated in the lambda
        AtomicBoolean status = new AtomicBoolean(false);

        //updates that value in the reportMap with the new object
        reportMap.put(report.reportId, report);

        //Sends the report
        JsonObject jay = new JsonObject();
        jay.addProperty("id", report.reportId);
        jay.addProperty("data", Client.net.gson.toJson(report));

        Client.net.secureSend("report/update", jay.toString(), (rr)->{
            if(rr.equals("ALL GOOD HOMIE")){
                status.set(true);
            }
        });
        return status.get();
    }
}