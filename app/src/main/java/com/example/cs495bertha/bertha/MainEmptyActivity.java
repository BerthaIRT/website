package com.example.cs495bertha.bertha;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainEmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_empty);

        Intent activityIntent;

        //See if this is the first time a user is logging in
        //by checking for UUID being created
        if (UUIDCreated())
            activityIntent = new Intent(this, MainActivity.class);
        else
            activityIntent = new Intent(this, MainActivity.class);
        startActivity(activityIntent);
    }

    private boolean UUIDCreated() {
        //TODO add code
        return true;
    }
}
