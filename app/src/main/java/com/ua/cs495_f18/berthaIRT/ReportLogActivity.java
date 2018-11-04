package com.ua.cs495_f18.berthaIRT;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageButton;

import com.ua.cs495_f18.berthaIRT.Adapter.MessageAdapter;
import com.ua.cs495_f18.berthaIRT.Adapter.ReportLogAdapter;

import java.util.ArrayList;
import java.util.List;

public class ReportLogActivity extends AppCompatActivity {

    private ReportLogAdapter logAdapter;
    private RecyclerView logRecyclerView;
    LinearLayoutManager linearLayoutManager;
    List<ReportLogObject> logList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_log);

        Toolbar toolbar = findViewById(R.id.toolbar_report_log);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());

        logRecyclerView = findViewById(R.id.report_log_recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);
        logRecyclerView.setLayoutManager(linearLayoutManager);

        logAdapter = new ReportLogAdapter(logList);
        logRecyclerView.setAdapter(logAdapter);

        ReportLogObject logObject = new ReportLogObject(ReportLogObject.newReportCreated());
        logList.add(logObject);

        logList.add(new ReportLogObject(ReportLogObject.reportAccepted()));
        logList.add(new ReportLogObject(ReportLogObject.reportAssigned("John Frank")));
        logList.add(new ReportLogObject(ReportLogObject.reportDetailsUpdated()));
        logList.add(new ReportLogObject(ReportLogObject.reportNewMessage()));
        logList.add(new ReportLogObject(ReportLogObject.reportStatusUpdated()));

    }
}
