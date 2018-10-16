package com.ua.cs495_f18.berthaIRT.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ua.cs495_f18.berthaIRT.R;
import com.ua.cs495_f18.berthaIRT.Adapter.AdminReportCardAdapter;
import com.ua.cs495_f18.berthaIRT.ReportObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AdminOpenReportsFragment extends Fragment {

    View v;
    private RecyclerView recyclerView;
    private List<ReportObject> reportList;

    public AdminOpenReportsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_admin_open_reports, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.view_fragment_admin_open_reports);
        AdminReportCardAdapter recyclerViewAdapter = new AdminReportCardAdapter(getContext(),reportList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewAdapter);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reportList = new ArrayList<>();
    }

    int temp = 0;
    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getActivity(), "R: Open " + temp, Toast.LENGTH_SHORT).show();
        populateFragment();
        temp++;
    }


    private void populateFragment() {
        //get the current Date & time
        String date = new SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("hh:mm", Locale.getDefault()).format(new Date());
        reportList.clear();
        reportList.add(new ReportObject("1111111", "Bullying", date, time, "Open"));
        reportList.add(new ReportObject("3333333", "Cheating", date, time, "Open"));
        reportList.add(new ReportObject("6124511", "Cyberbullying", date, time, "Open"));
        reportList.add(new ReportObject("1111111", "Bullying", date, time, "Open"));
        reportList.add(new ReportObject("3333333", "Cheating", date, time, "Open"));
        reportList.add(new ReportObject("6124511", "Cyberbullying", date, time, "Open"));
        reportList.add(new ReportObject("1111111", "Bullying", date, time, "Open"));
    }

}
