package com.ua.cs495_f18.berthaIRT;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    Context mCtx;
    List<ReportDetails> mData;

    public RecyclerViewAdapter(Context mCtx, List<ReportDetails> mData) {
        this.mCtx = mCtx;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mCtx).inflate(R.layout.fragment_reportlist_layout,parent,false);
        final MyViewHolder vHolder = new MyViewHolder(v);


        vHolder.item_report_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mCtx, "TestClick"+ mData.get(vHolder.getAdapterPosition()).getReportId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mCtx,ReportDisplay.class);
                ReportDetails r = mData.get(vHolder.getAdapterPosition());
                intent.putExtra("report_id", mData.get(vHolder.getAdapterPosition()).getReportId() );
                mCtx.startActivity(intent);
            }
        });

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textViewReportID.setText(mData.get(position).getReportId());
        holder.textViewKeyTags.setText(mData.get(position).getKeyTags());
        holder.textViewStatus.setText(mData.get(position).getStatus());
        holder.textViewDate.setText(mData.get(position).getDate());
        holder.textViewTime.setText(mData.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout item_report_preview;
        private TextView textViewReportID, textViewKeyTags, textViewDate,  textViewTime, textViewStatus;

        public MyViewHolder(View itemView) {
            super(itemView);
            item_report_preview = (LinearLayout) itemView.findViewById(R.id.reportPreview);
            textViewReportID = (TextView) itemView.findViewById(R.id.textViewReportID);
            textViewKeyTags = (TextView) itemView.findViewById(R.id.textViewKeyTagsOfReport);
            textViewStatus = (TextView) itemView.findViewById(R.id.textViewStatusOfReport);
            textViewDate = (TextView) itemView.findViewById(R.id.textViewDateOfReportCreation);
            textViewTime = (TextView) itemView.findViewById(R.id.textViewTimeOfReportCreation);
        }
    }
}
