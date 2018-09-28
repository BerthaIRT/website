package com.example.cs495bertha.bertha;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class OnGoingReportsFragment extends Fragment {

    View v;
    private RecyclerView recyclerView;
    private List<ReportDisplay> reportList;

    public OnGoingReportsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_on_going_reports, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.actionRecyclerView);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getContext(),reportList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewAdapter);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reportList = new ArrayList<>();
        reportList.add(new ReportDisplay(1111111, "Bullying", "Open", "10/04/18", ""));
        reportList.add(new ReportDisplay(3333333, "Cheating", "Open", "10/07/18", ""));
        reportList.add(new ReportDisplay(6124511, "Cyberbullying", "Open", "10/01/18", ""));

    }

}
