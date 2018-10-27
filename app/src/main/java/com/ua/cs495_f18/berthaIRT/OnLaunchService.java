package com.ua.cs495_f18.berthaIRT;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OnLaunchService extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Client.net = new BerthaNet(this);
        Client.reportMap = new HashMap<>();
        //berthaNet contains code to log in user
        List<String> l = new ArrayList<>();
        l.add("Bullying");
    }
}
