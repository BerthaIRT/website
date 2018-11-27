package com.ua.cs495f2018.berthaIRT;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.ua.cs495f2018.berthaIRT.fragment.AlertCardsFragment;

public class StudentAlertCardsActivity extends AppCompatActivity {

    final Fragment fragAlerts = new AlertCardsFragment();
    final FragmentManager fragDaddy = getSupportFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_alertcards);

        fragDaddy.beginTransaction().add(R.id.student_alertcards_fragframe, fragAlerts, "Reports").show(fragAlerts).commit();
    }
}
