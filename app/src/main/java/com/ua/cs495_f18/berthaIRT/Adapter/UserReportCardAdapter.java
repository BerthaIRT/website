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
import com.ua.cs495_f18.berthaIRT.R;
import com.ua.cs495_f18.berthaIRT.ReportObject;
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
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mCtx).inflate(R.layout.adapter_report_card,parent,false);
        final MyViewHolder vHolder = new MyViewHolder(v);


        vHolder.singleReportCard.setOnClickListener(v1 -> {
            Intent intent = new Intent(mCtx,UserDisplayReportActivity.class);
            ReportObject r = mData.get(vHolder.getAdapterPosition());
            intent.putExtra("report_id", mData.get(vHolder.getAdapterPosition()).getReportId() );
            mCtx.startActivity(intent);
        });

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Add "RPT: " in front of report ID
        String rptIDCon = "RPT: " + mData.get(position).getReportId();
        holder.textViewReportID.setText(rptIDCon);
        holder.textViewKeyTags.setText(mData.get(position).getKeyTagsString());
        holder.textViewStatus.setText(mData.get(position).getStatus());
        holder.textViewDate.setText(mData.get(position).getDate());
        holder.textViewTime.setText(mData.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private ConstraintLayout singleReportCard;
        private TextView textViewReportID, textViewKeyTags, textViewDate,  textViewTime, textViewStatus;

        public MyViewHolder(View itemView) {
            super(itemView);
            singleReportCard = itemView.findViewById(R.id.layout_single_report_card);
            textViewReportID = itemView.findViewById(R.id.label_report_card_id);
            textViewKeyTags = itemView.findViewById(R.id.label_report_card_tags);
            textViewStatus = itemView.findViewById(R.id.label_report_card_status);
            textViewDate = itemView.findViewById(R.id.label_report_card_date);
            textViewTime = itemView.findViewById(R.id.label_report_card_time);
        }
    }
}
