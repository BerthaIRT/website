package com.ua.cs495_f18.berthaIRT;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class UserPortalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_portal);

        final Button buttonCreate = findViewById(R.id.button_create_report);
        buttonCreate.setOnClickListener(v -> actionGotoCategorySelect());

        final Button buttonHistory = findViewById(R.id.button_report_history);
        buttonHistory.setOnClickListener(v -> actionGotoReportHistory());
    }

    private void actionGotoCategorySelect(){
        startActivity(new Intent(UserPortalActivity.this, UserCreateReportActivity.class));
    }

    private void actionGotoReportHistory(){
        startActivity(new Intent(UserPortalActivity.this, UserReportHistoryActivity.class));
    }
}