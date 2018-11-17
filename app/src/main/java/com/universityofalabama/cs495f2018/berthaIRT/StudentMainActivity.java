package com.universityofalabama.cs495f2018.berthaIRT;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.gson.JsonObject;

public class StudentMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);
        ((TextView) findViewById(R.id.student_main_name)).setText(Client.userGroup.getName());

        findViewById(R.id.student_main_button_createreport).setOnClickListener(v ->
                startActivity(new Intent(StudentMainActivity.this, StudentCreateReportActivity.class)));

        findViewById(R.id.student_main_viewhistory).setOnClickListener(v ->
                startActivity(new Intent(StudentMainActivity.this, StudentAlertCardsActivity.class)));

        //Util.makeDummieReports(this,20);
    }
}