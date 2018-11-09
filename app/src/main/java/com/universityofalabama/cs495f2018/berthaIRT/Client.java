package com.universityofalabama.cs495f2018.berthaIRT;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

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
import java.util.Arrays;
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

        JsonObject studentLogin = Util.readFromUserfile(Client.this);
        if(studentLogin != null){
            net.performLogin(this, studentLogin.get("username").getAsString(), studentLogin.get("password").getAsString(), false, x->{
                if(x.equals("SECURE")) startActivity(new Intent(this, StudentMainActivity.class));
                finish();
            });
        }
        else{
            startActivity(new Intent(this, NewUserActivity.class));
            finish();
        }


        List<String> fakeCats = new ArrayList<String>();
        fakeCats.add("John");
        fakeCats.add("James");

        String date = new SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        List<String> fakeTags = new ArrayList<>();
        fakeCats.add("Alcohol");
        fakeCats.add("Hazing");
        fakeTags.add("TEST");
        fakeTags.add("Jim");
        Report r1 = new Report();
        r1.categories = fakeCats;
        r1.incidentTimeStamp = date + " " + time;
        r1.location="Location A";
        r1.tags = fakeTags;
        Client.activeReport = r1;
        startActivity(new Intent(this, AdminMainActivity.class));

/*        List<String> fakeCats = new ArrayList<String>();
        fakeCats.add("John");
        fakeCats.add("James");
        Util.showAddRemoveDialog(Client.this, fakeCats, this::finishRemoveAdmin);

        String date = new SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        List<String> fakeTags = new ArrayList<>();
        fakeCats.add("Alcohol");
        fakeCats.add("Hazing");
        fakeTags.add("TEST");
        fakeTags.add("Jim");
        Report r1 = new Report();
        r1.categories = fakeCats;
        r1.incidentTimeStamp = date + " " + time;
        r1.location="Location A";
        r1.tags = fakeTags;
        Client.activeReport = r1;

        Util.showCheckboxDialog(Client.this, Util.getPreChecked(Arrays.asList(getResources().getStringArray(R.array.category_item)),Client.activeReport.categories),
                Arrays.asList(getResources().getStringArray(R.array.category_item)),this::finishRemoveAdmin);

        Util.showInputDialog(Client.this, "test", null, null, "Confimr", null);*/
    }


    private void finishRemoveAdmin(List<String> s) {
        Toast.makeText(Client.this,"t " + s, Toast.LENGTH_SHORT).show();
    }
}