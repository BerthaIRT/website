package com.ua.cs495_f18.berthaIRT.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.ua.cs495_f18.berthaIRT.AdminDisplayReportActivity;
import com.ua.cs495_f18.berthaIRT.R;
import com.ua.cs495_f18.berthaIRT.ReportObject;

import java.util.ArrayList;
import java.util.List;

public class AdminReportCardAdapter extends RecyclerView.Adapter<AdminReportCardAdapter.MyViewHolder> implements Filterable {

    Context mCtx;
    List<ReportObject> mData;
    List<ReportObject> mDataFiltered;


    public AdminReportCardAdapter(Context mCtx, List<ReportObject> mData) {
        this.mCtx = mCtx;
        this.mData = mData;
        this.mDataFiltered = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mCtx).inflate(R.layout.adapter_report_card,parent,false);
        final MyViewHolder vHolder = new MyViewHolder(v);


        vHolder.singleReportCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx,AdminDisplayReportActivity.class);
                ReportObject r = mDataFiltered.get(vHolder.getAdapterPosition());
                intent.putExtra("report_id", mDataFiltered.get(vHolder.getAdapterPosition()).getReportId() );
                mCtx.startActivity(intent);
            }
        });

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Add "RPT: " in front of report ID
        String rptIDCon = "RPT: " + mDataFiltered.get(position).getReportId();
        holder.textViewReportID.setText(rptIDCon);
        holder.textViewKeyTags.setText(mDataFiltered.get(position).getKeyTags());
        holder.textViewStatus.setText(mDataFiltered.get(position).getStatus());
        holder.textViewDate.setText(mDataFiltered.get(position).getDate());
        holder.textViewTime.setText(mDataFiltered.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return mDataFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mDataFiltered = mData;
                } else {
                    List<ReportObject> filteredList = new ArrayList<>();
                    for (ReportObject item : mData) {
                        if (item.getReportId().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(item);
                        }
                    }
                    mDataFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mDataFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mDataFiltered = (ArrayList<ReportObject>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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
