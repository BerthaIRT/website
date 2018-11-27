package com.universityofalabama.cs495f2018.berthaIRT.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.universityofalabama.cs495f2018.berthaIRT.AdminReportDetailsActivity;
import com.universityofalabama.cs495f2018.berthaIRT.Client;
import com.universityofalabama.cs495f2018.berthaIRT.R;
import com.universityofalabama.cs495f2018.berthaIRT.Report;
import com.universityofalabama.cs495f2018.berthaIRT.Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AdminReportCardAdapter extends RecyclerView.Adapter<AdminReportCardAdapter.ReportViewHolder>{
    private Context ctx;
    private List<Report> data;

    public AdminReportCardAdapter(Context c){
        ctx = c;
        data = new ArrayList<>();
    }

    public void updateReports(Collection<Report> c){
        data = new ArrayList<>(c);
        List<Report> unfilteredList = new ArrayList<>(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdminReportCardAdapter.ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.adapter_reportcard, parent, false);

        return new ReportViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        Report r = data.get(position);

        holder.catTainer.removeAllViews();
        for(String cat : r.getCategories()) {
            @SuppressLint("InflateParams") View v = LayoutInflater.from(ctx).inflate(R.layout.adapter_category, null, false);
            ((TextView) v.findViewById(R.id.adapter_alt_category)).setText(cat);
            holder.catTainer.addView(v);
        }

        holder.tvReportID.setText(r.getReportID().toString());
        holder.tvStatus.setText(r.getStatus());
        holder.tvSubmitted.setText(Util.formatTimestamp(r.getCreationDate()));

        holder.cardContainer.setOnClickListener(v -> {
            //get the report clicked on
            Client.activeReport = data.get(holder.getAdapterPosition());
            ctx.startActivity(new Intent(ctx, AdminReportDetailsActivity.class));
        });
    }

    @Override
    public int getItemCount() { return data.size(); }

    class ReportViewHolder extends RecyclerView.ViewHolder {
        LinearLayout catTainer;
        CardView cardContainer;
        TextView tvReportID, tvSubmitted, tvStatus;

        ReportViewHolder(View v) {
            super(v);
            catTainer = v.findViewById(R.id.reportcard_container_categories);
            cardContainer = itemView.findViewById(R.id.reportcard_cv);
            tvReportID = itemView.findViewById(R.id.reportcard_alt_id);
            tvStatus = itemView.findViewById(R.id.reportcard_alt_status);
            tvSubmitted = itemView.findViewById(R.id.reportcard_alt_action);
        }
    }

    public List<Report> getData(){
        return this.data;
    }
}