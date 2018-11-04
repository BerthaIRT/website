package com.ua.cs495_f18.berthaIRT;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.ua.cs495_f18.berthaIRT.Adapter.GroupLogAdapter;

import java.util.ArrayList;
import java.util.List;

public class GroupLogActivity extends AppCompatActivity {

    private GroupLogAdapter logAdapter;
    private RecyclerView logRecyclerView;
    LinearLayoutManager linearLayoutManager;
    List<GroupLogObject> logList = new ArrayList<>();

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

        GroupLogObject logObject = new GroupLogObject("Test");
        logList.add(logObject);

        logList.add(new GroupLogObject("Test2"));
        logList.add(new GroupLogObject("Test2"));
        logList.add(new GroupLogObject("Test2"));
        logList.add(new GroupLogObject("Test2"));
        logList.add(new GroupLogObject("Test2"));

    }
}
