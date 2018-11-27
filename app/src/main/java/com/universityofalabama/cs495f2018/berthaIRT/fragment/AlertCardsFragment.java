package com.universityofalabama.cs495f2018.berthaIRT.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.universityofalabama.cs495f2018.berthaIRT.Client;
import com.universityofalabama.cs495f2018.berthaIRT.R;
import com.universityofalabama.cs495f2018.berthaIRT.adapter.AlertCardAdapter;


public class AlertCardsFragment extends Fragment {

    RecyclerView rv;
    AlertCardAdapter adapter;

    public AlertCardsFragment(){

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater flater, ViewGroup tainer, Bundle savedInstanceState){
        View v = flater.inflate(R.layout.fragment_alertcards, tainer, false);

        adapter = new AlertCardAdapter(getContext());

        rv = v.findViewById(R.id.alertcards_rv);
        rv.setAdapter(adapter);

        adapter.updateAlerts(Client.alertList);

        if(adapter.getItemCount() == 0)
            v.findViewById(R.id.alertcards_alt_noalerts).setVisibility(View.VISIBLE);
        return v;
    }
}