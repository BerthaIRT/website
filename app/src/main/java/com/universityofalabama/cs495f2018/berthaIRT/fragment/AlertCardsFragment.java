package com.universityofalabama.cs495f2018.berthaIRT.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.universityofalabama.cs495f2018.berthaIRT.Client;
import com.universityofalabama.cs495f2018.berthaIRT.R;
import com.universityofalabama.cs495f2018.berthaIRT.Report;
import com.universityofalabama.cs495f2018.berthaIRT.adapter.AlertCardAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AlertCardsFragment extends Fragment {

    RecyclerView rv;
    AlertCardAdapter adapter;
    SwipeRefreshLayout swipeContainer;
    TextView tvNoAlerts;

    public AlertCardsFragment(){

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater flater, ViewGroup tainer, Bundle savedInstanceState){
        View v = flater.inflate(R.layout.fragment_alertcards, tainer, false);

        adapter = new AlertCardAdapter(getContext());
        rv = v.findViewById(R.id.alertcards_rv);
        rv.setAdapter(adapter);


        swipeContainer = v.findViewById(R.id.alertcards_sr);
        swipeContainer.setOnRefreshListener(this::actionSwipeRefresh);

        tvNoAlerts = v.findViewById(R.id.alertcards_alt_noalerts);

        adapter.updateAlerts(Client.userGroup.getAlerts());
        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    private void actionSwipeRefresh() {
        swipeContainer.setRefreshing(true); {
            Client.net.getUserGroup(getContext(), x->{
                adapter.updateAlerts(Client.userGroup.getAlerts());
                swipeContainer.setRefreshing(false);
            });
        }
    }
}