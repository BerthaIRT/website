package com.universityofalabama.cs495f2018.berthairt;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AlertCardsFragment extends Fragment {

    List<Report> fragList = new ArrayList<>();
    AlertCardsFragment.AlertCardAdapter adapter;

    public AlertCardsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater flater, ViewGroup tainer, Bundle savedInstanceState){
        System.out.println("onCreateView (Report)");
        View v = flater.inflate(R.layout.fragment_alertcards, tainer, false);
        RecyclerView rv = v.findViewById(R.id.alertcard_rv);

        adapter = new AlertCardsFragment.AlertCardAdapter(getContext(), fragList);
        rv.setAdapter(adapter);

        populateFraglist();

        return v;
    }

    private void populateFraglist() {
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
        adapter.notifyDataSetChanged();
    }

    class AlertCardAdapter extends RecyclerView.Adapter<AlertCardsFragment.ReportViewHolder>{
        Context ctx;
        List<Report> data;
        AlertCardsFragment.CategoryAdapter catAdapter;

        public AlertCardAdapter(Context c, List<Report> d){
            ctx = c;
            data = d;
        }

        @NonNull
        @Override
        public AlertCardsFragment.ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(ctx).inflate(R.layout.adapter_alertcard, parent, false);
            RecyclerView rv = v.findViewById(R.id.alertcard_rv_categories);

            catAdapter = new AlertCardsFragment.CategoryAdapter();
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

    class CategoryAdapter extends RecyclerView.Adapter<AlertCardsFragment.CategoryViewHolder>{
        List<String> categoryList;

        public CategoryAdapter(){
            categoryList = new ArrayList<>();
        }
        @NonNull
        @Override
        public AlertCardsFragment.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_category, parent, false);
            return new AlertCardsFragment.CategoryViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull AlertCardsFragment.CategoryViewHolder holder, int position) {
            holder.tvCategory.setText(categoryList.get(position));
        }

        @Override
        public int getItemCount() {
            System.out.println(categoryList.size());
            return categoryList.size();
        }
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder{
        TextView tvCategory;

        public CategoryViewHolder(View itemView){
            super(itemView);
            tvCategory = itemView.findViewById(R.id.adapter_alt_category);
        }
    }
}
