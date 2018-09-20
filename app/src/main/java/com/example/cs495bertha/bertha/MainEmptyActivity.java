package com.example.cs495bertha.bertha;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainEmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_empty);

        Intent activityInent;

        if (UUIDCreated())
            activityInent = new Intent(this, MainActivity.class);
        else
            activityInent = new Intent(this, MainActivity.class);
    }

    private boolean UUIDCreated() {
        return true;
    }
}
