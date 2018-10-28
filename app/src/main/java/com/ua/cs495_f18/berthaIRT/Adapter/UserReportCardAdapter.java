package com.ua.cs495_f18.berthaIRT.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ua.cs495_f18.berthaIRT.AdminDisplayReportActivity;
import com.ua.cs495_f18.berthaIRT.Client;
import com.ua.cs495_f18.berthaIRT.R;
import com.ua.cs495_f18.berthaIRT.ReportObject;
import com.ua.cs495_f18.berthaIRT.StaticUtilities;
import com.ua.cs495_f18.berthaIRT.UserDisplayReportActivity;

import java.util.List;

public class UserReportCardAdapter extends RecyclerView.Adapter<UserReportCardAdapter.MyViewHolder> {

    private Context mCtx;
    private List<ReportObject> mData;


    public UserReportCardAdapter(Context mCtx, List<ReportObject> mData) {
        this.mCtx = mCtx;
        this.mData = mData;
    }

    @NonNull
    @Override
    public UserReportCardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mCtx).inflate(R.layout.adapter_report_card,parent,false);
        final UserReportCardAdapter.MyViewHolder vHolder = new UserReportCardAdapter.MyViewHolder(v);

        vHolder.singleReportCard.setOnClickListener(v1 -> {
            Intent intent = new Intent(mCtx,AdminDisplayReportActivity.class);
            Client.activeReport = mData.get(vHolder.getAdapterPosition());
            mCtx.startActivity(intent);
        });

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserReportCardAdapter.MyViewHolder holder, int position) {
        ReportObject r = mData.get(position);
        holder.textViewReportID.setText("RPT: " + r.reportId);
        holder.textViewCategories.setText(StaticUtilities.listToString(r.categories));
        holder.textViewStatus.setText(r.status);
        holder.textViewDate.setText(r.submittedDate);
        holder.textViewTime.setText(r.submittedTime);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private ConstraintLayout singleReportCard;
        private TextView textViewReportID, textViewCategories, textViewDate,  textViewTime, textViewStatus;

        public MyViewHolder(View itemView) {
            super(itemView);
            singleReportCard = itemView.findViewById(R.id.layout_single_report_card);
            textViewReportID = itemView.findViewById(R.id.alt_report_card_id);
            textViewCategories = itemView.findViewById(R.id.alt_report_card_tags);
            textViewStatus = itemView.findViewById(R.id.alt_report_card_status);
            textViewDate = itemView.findViewById(R.id.alt_report_card_date);
            textViewTime = itemView.findViewById(R.id.alt_report_card_time);
        }
    }
}