package com.ua.cs495f2018.berthaIRT;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client extends AppCompatActivity {

    //Class for secure and insecure network functions, AWS Cognito functionality
    public static BerthaNet net;

    //The NAME attribute of the user.  Null for students.
    //To get the EMAIL use Client.net.pool.getCurrentUser()
    public static String userName;
    public static Integer userGroupID;
    public static String userGroupName;
    public static String userGroupStatus;
    public static boolean loggedIn = false;

    //List containing reports pulled from server.
    //Should NOT be updated outside of a network callback function or else inconsistencies between client and server occur
    public static Map<Integer, Report> reportMap;

    //List containing alerts pulled from server.
    public static List<Message> alertList;

    //The report object currently being viewed.
    //This CAN be updated - but be sure to call /report/update with it
    public static Report activeReport;

    //For new admins to start on the dashboard screen instead of an empty RV
    static boolean startOnDashboard = false; //for new admins, maybe could be a pref

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landingpage);
        reportMap = new HashMap<>();
        net = new BerthaNet(this);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( Client.this, instanceIdResult -> {
            String token = instanceIdResult.getToken();
            Log.d("FCMTOKEN",token);
        });

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
    static AsyncTask<Void, Void, Void> t;
    public static void makeRefreshTask(Context ctx, Interface.WithVoidListener onUpdateHandler){
        if(t != null) t.cancel(true);
        t = new AsyncTask<Void, Void, Void>()  {
            @Override
            protected Void doInBackground(Void... voids) {
                while(true){
                    net.secureSend(ctx, "/refresh", "", r->{
                        if(r.equals("nope")) return;
                        net.pullReports(ctx, net.jp.parse(r).getAsJsonArray(), onUpdateHandler);
                    });
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return null;
                    }

                    if(((AppCompatActivity) ctx).isFinishing()){
                        cancel(true);
                        return null;
                    }
                }
            }
        };
        t.execute(null, null, null);
    }
}