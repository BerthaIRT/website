package com.universityofalabama.cs495f2018.berthaIRT;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.JsonObject;

import java.util.HashMap;

public class Client extends AppCompatActivity {

    //Class for secure and insecure network functions, AWS Cognito functionality
    public static BerthaNet net;

    //The NAME attribute of the user.  Null for students.
    //To get the EMAIL use Client.net.pool.getCurrentUser()
    public static String currentUserName;

    //Group object pulled from server.  Includes group-wide alerts so must be refreshed regularly.
    public static Group userGroup;

    //Map containing reports freshly pulled from server.
    //Should NOT be updated outside of a network callback function or else inconsistencies between client and server occur
    public static HashMap<String, Report> reportMap;

    //The report object currently being viewed.
    //This CAN be updated - but be sure to call /report/update with it
    public static Report activeReport;

    //Last time reportMap was updated
    //If someone in the group makes an edit, the timestamp on the server shall be updated.
    //Report refresh shall be triggered if lastUpdated < serverTimeStamp
    public static Long lastUpdated;

    //For new admins to start on the dashboard screen instead of an empty RV
    static boolean startOnDashboard = false; //for new admins, maybe could be a pref

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landingpage);
        reportMap = new HashMap<>();
        net = new BerthaNet(this);


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