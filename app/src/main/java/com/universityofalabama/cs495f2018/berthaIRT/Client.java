package com.universityofalabama.cs495f2018.berthaIRT;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;

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
        Report.Log notes = new Report.Log();
        notes.text = "TEST";
        notes.sender = "FRANK";
        r1.notes.add(notes);
        notes = new Report.Log();
        notes.text = "Pass";
        notes.sender = "FRANK";
        r1.notes.add(notes);


        Client.activeReport = r1;
        startActivity(new Intent(this, AdminReportDetailsActivity.class));*/

        startActivity(new Intent(this, NewUserActivity.class));
        finish();

//        JsonObject studentLogin = Util.readFromUserfile(Client.this);
//        if(studentLogin != null){
//            net.performLogin(this, studentLogin.get("username").getAsString(), studentLogin.get("password").getAsString(), false, x->{
//                if(x.equals("SECURE")){
//                 //waitDialog.dialog.dismiss();
//                 startActivity(new Intent(this, StudentMainActivity.class));}
//                finish();
//            });
//        }
//        else{
//            startActivity(new Intent(this, NewUserActivity.class));
//            finish();
//        }
   }
}