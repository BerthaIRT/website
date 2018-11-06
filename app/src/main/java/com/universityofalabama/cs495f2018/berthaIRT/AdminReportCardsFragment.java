package com.universityofalabama.cs495f2018.berthaIRT;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminReportCardsFragment extends Fragment {
    List<Report> fragList = new ArrayList<>();
    ReportCardAdapter adapter;
    public AdminReportCardsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater flater, ViewGroup tainer, Bundle savedInstanceState){
        System.out.println("onCreateView (Report)");
        View v = flater.inflate(R.layout.fragment_admin_reportcards, tainer, false);
        RecyclerView rv = v.findViewById(R.id.admin_reports_rv);

        adapter = new ReportCardAdapter(getContext(), fragList);
        rv.setAdapter(adapter);

        populateFraglist();
        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        populateFraglist();

    }

    private void populateFraglist() {
        fragList.clear();
        for(Map.Entry e : Client.reportMap.entrySet())
            fragList.add((Report) e.getValue());

        String filter = " ";//TODO
        applyFilter(filter);

        adapter.notifyDataSetChanged();
    }

    private void applyFilter(String filter){
        //TODO
    }

    class ReportCardAdapter extends RecyclerView.Adapter<ReportViewHolder>{
        Context ctx;
        List<Report> data;
        CategoryTagAdapter catAdapter;

        public ReportCardAdapter(Context c, List<Report> d){
            ctx = c;
            data = d;
        }

        @NonNull
        @Override
        public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(ctx).inflate(R.layout.adapter_reportcard, parent, false);
            RecyclerView rv = v.findViewById(R.id.admin_reportcard_rv_categories);

            catAdapter = new CategoryTagAdapter(false);
            rv.setAdapter(catAdapter);

            return new ReportViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
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
        CardView cardContainer;
        TextView tvReportID, tvSubmitted, tvStatus;

        public ReportViewHolder(View itemView) {
            super(itemView);
            cardContainer = itemView.findViewById(R.id.reportcard_cv);
            tvReportID = itemView.findViewById(R.id.reportcard_alt_id);
            tvStatus = itemView.findViewById(R.id.reportcard_alt_status);
            tvSubmitted = itemView.findViewById(R.id.reportcard_alt_action);
        }
    }
}

