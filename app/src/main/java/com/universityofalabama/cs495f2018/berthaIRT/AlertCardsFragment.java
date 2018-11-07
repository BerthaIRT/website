package com.universityofalabama.cs495f2018.berthaIRT;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AlertCardsFragment extends Fragment {

    List<Report> fragList = new ArrayList<>();
    RecyclerView rv;
    AlertCardsFragment.AlertCardAdapter adapter;
    SwipeRefreshLayout swipeContainer;

    public AlertCardsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater flater, ViewGroup tainer, Bundle savedInstanceState){
        System.out.println("onCreateView (Report)");
        View v = flater.inflate(R.layout.fragment_alertcards, tainer, false);
        rv = v.findViewById(R.id.alertcard_rv);

        adapter = new AlertCardsFragment.AlertCardAdapter(getContext(), fragList);
        rv.setAdapter(adapter);

        swipeContainer = v.findViewById(R.id.alerts_sr);
        swipeContainer.setOnRefreshListener(this::refresh);

        //populateFraglist();

        return v;
    }

    private void populateFraglist() {
        Toast.makeText(getActivity(), "PopulatingFragList ",Toast.LENGTH_SHORT).show();

        fragList.clear();
        for(Map.Entry e : Client.reportMap.entrySet())
            fragList.add((Report) e.getValue());

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume(){
        super.onResume();
        populateFragment();
    }

    private void populateFragment() {
        populateFraglist();
    }

    private void refresh() {
        swipeContainer.setRefreshing(true);
        {
            Client.updateReportMap();
            populateFraglist();
            adapter.notifyDataSetChanged();
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
            holder.tvSubmitted.setText(r.submittedDate);
            System.out.println(r.categories);
            catAdapter.categoryList.clear();
            for(String s : r.categories) catAdapter.categoryList.add(s);
            catAdapter.notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView tvReportID, tvSubmitted, tvStatus;

        public ReportViewHolder(View itemView) {
            super(itemView);
            tvReportID = itemView.findViewById(R.id.alertcard_alt_id);
            tvStatus = itemView.findViewById(R.id.alertcard_alt_status);
            tvSubmitted = itemView.findViewById(R.id.alertcard_alt_action);
        }
    }
}
