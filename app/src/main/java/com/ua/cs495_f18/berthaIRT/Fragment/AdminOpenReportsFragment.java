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

import com.ua.cs495_f18.berthaIRT.R;
import com.ua.cs495_f18.berthaIRT.Adapter.ReportCardAdapter;
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
        ReportCardAdapter recyclerViewAdapter = new ReportCardAdapter(getContext(),reportList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewAdapter);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get the current Date & time
        String date = new SimpleDateFormat("MM/dd/YY", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("hh:mm", Locale.getDefault()).format(new Date());

        reportList = new ArrayList<>();
        reportList.add(new ReportObject("1111111", "Bullying", date, time, "Open"));
        reportList.add(new ReportObject("3333333", "Cheating", date, time, "Open"));
        reportList.add(new ReportObject("6124511", "Cyberbullying", date, time, "Open"));
        reportList.add(new ReportObject("1111111", "Bullying", date, time, "Open"));
        reportList.add(new ReportObject("3333333", "Cheating", date, time, "Open"));
        reportList.add(new ReportObject("6124511", "Cyberbullying", date, time, "Open"));
        reportList.add(new ReportObject("1111111", "Bullying", date, time, "Open"));

    }

}
