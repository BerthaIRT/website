package com.universityofalabama.cs495f2018.berthaIRT;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogActivity extends AppCompatActivity {

    private LogAdapter logAdapter;
    private RecyclerView logRecyclerView;
    LinearLayoutManager linearLayoutManager;
    List<Log> logList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        logRecyclerView = findViewById(R.id.log_recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);
        logRecyclerView.setLayoutManager(linearLayoutManager);

        logAdapter = new LogAdapter(logList);
        logRecyclerView.setAdapter(logAdapter);

        populateReportLog();
    }

    private void populateReportLog() {
        //TEMP for testing
        List<String> temp = new ArrayList<>();
        //Client.activeReport = new Report("1231", "fsf", "1", temp);

        //adds all the reports logs in reverse order
        List<Log> list = Client.activeReport.logs;
        Collections.reverse(list);
        logList.addAll(list);


        //Temp for Testing
/*        Log logObject = new Log(Log.newReportCreated());
        logObject.newItem = "Test";
        logList.add(logObject);
        logList.add(new Log(Log.reportAccepted()));
        logList.add(new Log(Log.reportAssigned()));
        logList.add(new Log(Log.reportDetailsUpdated()));
        logList.add(new Log(Log.reportNewMessage()));
        logList.add(new Log(Log.reportStatusUpdated()));*/

        //if there is no log then show message
        if (logList.size() == 0) {
            findViewById(R.id.log_no_log).setVisibility(View.VISIBLE);
        }
    }

    class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogViewHolder> {

        private List<Log> reportLogs;


        public LogAdapter(List<Log> reportLogs) {
            this.reportLogs = reportLogs;
        }

        @Override
        public LogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.adapter_log, parent, false);
            return new LogViewHolder(view);
        }

        @Override
        public void onBindViewHolder(LogViewHolder holder, int position) {
            Log reportLog = reportLogs.get(position);
            holder.logTimestamp.setText(reportLog.timestamp);
            holder.logText.setText(reportLog.text);
            holder.logOld.setText(reportLog.oldItem);
            holder.logNew.setText(reportLog.newItem);
            holder.logBy.setText(reportLog.admin);


            holder.cardView.setOnClickListener(v -> {
                if (holder.layoutTop.getVisibility() == View.GONE)
                    holder.layoutTop.setVisibility(View.VISIBLE);
                else
                    holder.layoutTop.setVisibility(View.GONE);

                if (holder.layoutBottom.getVisibility() == View.GONE)
                    holder.layoutBottom.setVisibility(View.VISIBLE);
                else
                    holder.layoutBottom.setVisibility(View.GONE);

                //If any of the bottom parts are null then make them not visible
                if (holder.logOld == null)
                    holder.layoutBottom1.setVisibility(View.GONE);
                if(holder.logNew == null)
                    holder.layoutBottom2.setVisibility(View.GONE);
                if(holder.logBy == null)
                    holder.layoutBottom3.setVisibility(View.GONE);
            });
        }

        @Override
        public int getItemCount() {
            return reportLogs.size();
        }

        public Object getItem(int i) {
            return reportLogs.get(i);
        }

        public class LogViewHolder extends RecyclerView.ViewHolder{
            CardView cardView;
            LinearLayout layoutTop, layoutBottom, layoutBottom1, layoutBottom2, layoutBottom3;

            TextView logText, logTimestamp, logOld, logNew, logBy;

            public LogViewHolder(View itemView) {
                super(itemView);

                if(itemView != null) {
                    cardView = itemView.findViewById(R.id.log_cardview);

                    layoutTop = itemView.findViewById(R.id.linear_log1);
                    logTimestamp = itemView.findViewById(R.id.alt_log_timestamp);

                    logText =  itemView.findViewById(R.id.alt_log_text);

                    layoutBottom = itemView.findViewById(R.id.linear_log2);

                    layoutBottom1 = itemView.findViewById(R.id.linear_log3);
                    logOld = itemView.findViewById(R.id.alt_log_old);

                    layoutBottom2 = itemView.findViewById(R.id.linear_log4);
                    logNew = itemView.findViewById(R.id.alt_log_new);

                    layoutBottom3 = itemView.findViewById(R.id.linear_log5);
                    logBy = itemView.findViewById(R.id.alt_log_by);
                }
            }
        }
    }
}
