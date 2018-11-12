package com.universityofalabama.cs495f2018.berthaIRT;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.universityofalabama.cs495f2018.berthaIRT.dialog.WaitDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Client extends AppCompatActivity {

    public static String currentUserName;
    public static String currentUserGroupID;
    public static BerthaNet net;
    public static HashMap<String, Report> reportMap;
    public static Report activeReport;

    static boolean startOnDashboard = false; //for new admins, maybe could be a pref

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landingpage);
        reportMap = new HashMap<>();
        net = new BerthaNet(this);

        //TEMP Report for testing
/*        List<String> fakeCats = new ArrayList<>();
        List<String> fakeTags = new ArrayList<>();
        fakeCats.add("Alcohol");
        fakeCats.add("Hazing");
        fakeTags.add("TEST");
        fakeTags.add("Jim");
        Report r1 = new Report();
        r1.categories = fakeCats;
        r1.incidentTimeStamp = System.currentTimeMillis();
        r1.creationTimestamp = System.currentTimeMillis();
        r1.lastActionTimestamp = System.currentTimeMillis();
        r1.location="Location A";
        r1.tags = fakeTags;

        currentUserName = "12345";
        r1.messages.add(new Message("Test", currentUserName));
        r1.messages.add(new Message("Bob", "12"));
        Log log = new Log();
        log.text = "Test 1";
        r1.logs.add(log);
        log = new Log();
        log.text = "Test 2";
        r1.logs.add(log);
        log = new Log();
        log.text = "Test 3";
        r1.logs.add(log);
        Client.activeReport = r1;
        startActivity(new Intent(this, AdminReportDetailsActivity.class));*/

        JsonObject studentLogin = Util.readFromUserfile(Client.this);
        if(studentLogin != null){
            net.performLogin(this, studentLogin.get("username").getAsString(), studentLogin.get("password").getAsString(), false, x->{
                if(x.equals("SECURE")){
                //waitDialog.dialog.dismiss();
                 startActivity(new Intent(this, StudentMainActivity.class));}
                finish();
            });
        }
        else{
            startActivity(new Intent(this, NewUserActivity.class));
            finish();
        }
   }
}