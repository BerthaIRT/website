package com.ua.cs495_f18.berthaIRT;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ua.cs495_f18.berthaIRT.Adapter.ReportLogAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReportLogActivity extends AppCompatActivity {

    private ReportLogAdapter logAdapter;
    private RecyclerView logRecyclerView;
    LinearLayoutManager linearLayoutManager;
    List<ReportLog> logList = new ArrayList<>();

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

        populateReportLog();
    }

    private void populateReportLog() {
        //TEMP for testing
        List<String> temp = new ArrayList<>();
        Client.activeReport = new ReportObject("1231", "fsf", "1", temp);

        //adds all the reports logs in reverse order
        List<ReportLog> list = Client.activeReport.reportLogs;
        Collections.reverse(list);
        logList.addAll(list);


        //Temp for Testing
        ReportLog logObject = new ReportLog(ReportLog.newReportCreated());
        logObject.newItem = "Test";
        logList.add(logObject);
        logList.add(new ReportLog(ReportLog.reportAccepted()));
        logList.add(new ReportLog(ReportLog.reportAssigned("John Frank")));
        logList.add(new ReportLog(ReportLog.reportDetailsUpdated()));
        logList.add(new ReportLog(ReportLog.reportNewMessage()));
        logList.add(new ReportLog(ReportLog.reportStatusUpdated()));

        //if there is no log then show message
        if (logList.size() == 0) {
            findViewById(R.id.report_log_no_log).setVisibility(View.VISIBLE);
        }
    }
}
