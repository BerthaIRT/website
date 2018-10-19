package com.ua.cs495_f18.berthaIRT;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.android.volley.toolbox.Volley;
import com.google.gson.GsonBuilder;

public class OnLaunchService extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StaticUtilities.initNetwork(this);
        startActivity(new Intent(this, UnregisteredPortalActivity.class));
        finish();
    }
}
