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

        final Button buttonCreate = (Button) findViewById(R.id.button_create_report);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                actionToCategorySelect();
            }
        });

        final Button buttonHistory = (Button) findViewById(R.id.button_report_history);
        buttonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                actionToReportHistory();
            }
        });
    }

    private void actionToCategorySelect(){
        startActivity(new Intent(UserPortalActivity.this, UserCreateReportActivity.class));
    }

    private void actionToReportHistory(){
        startActivity(new Intent(UserPortalActivity.this, UserReportHistoryActivity.class));
    }
}
