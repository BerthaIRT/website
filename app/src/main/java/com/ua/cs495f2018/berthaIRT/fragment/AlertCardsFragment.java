package com.ua.cs495f2018.berthaIRT.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ua.cs495f2018.berthaIRT.Client;
import com.ua.cs495f2018.berthaIRT.R;
import com.ua.cs495f2018.berthaIRT.Util;
import com.ua.cs495f2018.berthaIRT.adapter.AlertCardAdapter;

import java.util.ArrayList;


public class AlertCardsFragment extends Fragment {

    RecyclerView rv;
    AlertCardAdapter adapter;

    public AlertCardsFragment(){

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater flater, ViewGroup tainer, Bundle savedInstanceState){
        View v = flater.inflate(R.layout.fragment_alertcards, tainer, false);


        rv = v.findViewById(R.id.alertcards_rv);
        adapter = new AlertCardAdapter(getContext());
        rv.setAdapter(adapter);

        adapter.updateAlerts(Client.alertList);

        if(adapter.getItemCount() == 0)
            v.findViewById(R.id.alertcards_alt_noalerts).setVisibility(View.VISIBLE);
        return v;
    }
}