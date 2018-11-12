package com.universityofalabama.cs495f2018.berthaIRT.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.universityofalabama.cs495f2018.berthaIRT.AdminReportDetailsActivity;
import com.universityofalabama.cs495f2018.berthaIRT.Client;
import com.universityofalabama.cs495f2018.berthaIRT.R;
import com.universityofalabama.cs495f2018.berthaIRT.Report;
import com.universityofalabama.cs495f2018.berthaIRT.StudentReportDetailsActivity;
import com.universityofalabama.cs495f2018.berthaIRT.Util;
import com.universityofalabama.cs495f2018.berthaIRT.adapter.CategoryTagAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AlertCardsFragment extends Fragment {

    List<Report> fragList = new ArrayList<>();
    RecyclerView rv;
    AlertCardsFragment.AlertCardAdapter adapter;
    SwipeRefreshLayout swipeContainer;
    TextView tvNoAlerts;

    public AlertCardsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater flater, ViewGroup tainer, Bundle savedInstanceState){
        System.out.println("onCreateView (Report)");
        View v = flater.inflate(R.layout.fragment_alertcards, tainer, false);
        rv = v.findViewById(R.id.alertcards_rv);

        adapter = new AlertCardsFragment.AlertCardAdapter(getContext(), fragList);
        rv.setAdapter(adapter);

        swipeContainer = v.findViewById(R.id.alertcards_sr);
        swipeContainer.setOnRefreshListener(this::refresh);

        tvNoAlerts = v.findViewById(R.id.alertcards_alt_noalerts);

        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        populateFragment();
    }

    private void populateFragment() {
        fragList.clear();
        for(Map.Entry e : Client.reportMap.entrySet())
            fragList.add((Report) e.getValue());

        if(fragList.size() == 0)
            tvNoAlerts.setVisibility(View.VISIBLE);
        else
            tvNoAlerts.setVisibility(View.INVISIBLE);

        adapter.notifyDataSetChanged();
    }

    private void refresh() {
        swipeContainer.setRefreshing(true);
        {
            //Client.updateReportMap();
            populateFragment();
        }
        if(swipeContainer.isRefreshing())
            swipeContainer.setRefreshing(false);
    }

    class AlertCardAdapter extends RecyclerView.Adapter<AlertCardsFragment.ReportViewHolder>{
        Context ctx;
        List<Report> data;
        CategoryTagAdapter catAdapter;

        public AlertCardAdapter(Context c, List<Report> d){
            ctx = c;
            data = d;
        }

        @NonNull
        @Override
        public AlertCardsFragment.ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(ctx).inflate(R.layout.adapter_alertcard, parent, false);

            RecyclerView rv = v.findViewById(R.id.alertcard_rv_categories);
            catAdapter = new CategoryTagAdapter(false);
            rv.setAdapter(catAdapter);

            return new AlertCardsFragment.ReportViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull AlertCardsFragment.ReportViewHolder holder, int position) {
            Report r = data.get(position);
            holder.tvReportID.setText(r.reportId);
            holder.tvStatus.setText(r.status);
            holder.tvSubmitted.setText(Util.getDate(r.creationTimestamp));
            System.out.println(r.categories);
            catAdapter.updateCategories(r.categories);

            holder.cardContainer.setOnClickListener(v -> {
                //get the report clicked on
                Client.activeReport = data.get(holder.getAdapterPosition());
                //if the parent activity is AdminMain
                if(getActivity().getClass().getSimpleName().equals("AdminMainActivity"))
                    startActivity(new Intent(getActivity(), AdminReportDetailsActivity.class));
                else
                    startActivity(new Intent(getActivity(), StudentReportDetailsActivity.class));
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    class ReportViewHolder extends RecyclerView.ViewHolder {
        CardView cardContainer;
        TextView tvReportID, tvSubmitted, tvStatus;

        public ReportViewHolder(View itemView) {
            super(itemView);
            cardContainer = itemView.findViewById(R.id.alertcard_cv);
            tvReportID = itemView.findViewById(R.id.alertcard_alt_id);
            tvStatus = itemView.findViewById(R.id.alertcard_alt_status);
            tvSubmitted = itemView.findViewById(R.id.alertcard_alt_action);
        }
    }
}
