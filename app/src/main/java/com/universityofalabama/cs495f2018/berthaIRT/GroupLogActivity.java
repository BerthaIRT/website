package com.universityofalabama.cs495f2018.berthaIRT;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.universityofalabama.cs495f2018.berthaIRT.Adapter.GroupLogAdapter;

import java.util.ArrayList;
import java.util.List;

public class GroupLogActivity extends AppCompatActivity {

    private GroupLogAdapter logAdapter;
    private RecyclerView logRecyclerView;
    LinearLayoutManager linearLayoutManager;
    List<GroupLog> logList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_log);

        Toolbar toolbar = findViewById(R.id.toolbar_group_log);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());

        logRecyclerView = findViewById(R.id.group_log_recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);
        logRecyclerView.setLayoutManager(linearLayoutManager);

        logAdapter = new GroupLogAdapter(logList);
        logRecyclerView.setAdapter(logAdapter);

        populateGroupLog();
    }
    
    public void populateGroupLog() {

        //Temp for Testing
        GroupLog logObject = new GroupLog(GroupLog.adminCreated("James Frank"));
        logObject.newItem = "Test";
        logList.add(logObject);
        logList.add(new GroupLog(GroupLog.adminPasswordReset("James Frank")));
        logList.add(new GroupLog(GroupLog.adminSignedIn("Thomas T")));
        logList.add(new GroupLog(GroupLog.groupLogoUpdated()));
        logList.add(new GroupLog(GroupLog.registrationStatusChange("Open")));
        logList.add(new GroupLog(GroupLog.registrationStatusChange("Closed")));
        
        //if there is no log then show message
        if (logList.size() == 0) {
            findViewById(R.id.group_log_no_log).setVisibility(View.VISIBLE);
        }
    }
}
