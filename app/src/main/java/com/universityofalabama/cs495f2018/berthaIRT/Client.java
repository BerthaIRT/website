package com.universityofalabama.cs495f2018.berthaIRT;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.universityofalabama.cs495f2018.berthaIRT.dialog.WaitDialog;

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
        startActivity(new Intent(this, NewUserActivity.class));

//        JsonObject studentLogin = Util.readFromUserfile(Client.this);
//        if(studentLogin != null){
//            net.performLogin(this, studentLogin.get("username").getAsString(), studentLogin.get("password").getAsString(), false, x->{
//                if(x.equals("SECURE")){
//
//                waitDialog.dialog.dismiss();
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