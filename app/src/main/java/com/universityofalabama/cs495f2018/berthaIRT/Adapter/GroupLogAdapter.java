package com.universityofalabama.cs495f2018.berthaIRT.Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.universityofalabama.cs495f2018.berthaIRT.GroupLog;
import com.universityofalabama.cs495f2018.berthaIRT.R;

import java.util.List;

public class GroupLogAdapter extends RecyclerView.Adapter<GroupLogAdapter.GroupLogAdapterViewHolder> {

    private List<GroupLog> groupLogs;


    public GroupLogAdapter(List<GroupLog> groupLogs) {
        this.groupLogs = groupLogs;
    }

    @Override
    public void onBindViewHolder(GroupLogAdapterViewHolder holder, int position) {
        GroupLog groupLog = groupLogs.get(position);
        holder.logTimestamp.setText(groupLog.timestamp);
        holder.logText.setText(groupLog.text);
        holder.logOld.setText(groupLog.oldItem);
        holder.logNew.setText(groupLog.newItem);
        holder.logBy.setText(groupLog.admin);

        holder.cardView.setOnClickListener(v -> {
            if (holder.layoutTop.getVisibility() == View.GONE)
                holder.layoutTop.setVisibility(View.VISIBLE);
            else
                holder.layoutTop.setVisibility(View.GONE);

            if (holder.layoutBottom.getVisibility() == View.GONE) {
                holder.layoutBottom.setVisibility(View.VISIBLE);
                holder.layoutBottom1.setVisibility(View.VISIBLE);
                holder.layoutBottom2.setVisibility(View.VISIBLE);
                holder.layoutBottom3.setVisibility(View.VISIBLE);
            }
            else
                holder.layoutBottom.setVisibility(View.GONE);

            //If any of the bottom parts are null then make them not visible
            if (holder.logOld.getText().toString().equals(""))
                holder.layoutBottom1.setVisibility(View.GONE);
            if(holder.logNew.getText().toString().equals(""))
                holder.layoutBottom2.setVisibility(View.GONE);
            if(holder.logBy.getText().toString().equals(""))
                holder.layoutBottom3.setVisibility(View.GONE);
        });
    }

    @Override
    public GroupLogAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.log_view, parent, false);
        return new GroupLogAdapterViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return groupLogs.size();
    }

    public Object getItem(int i) {
        return groupLogs.get(i);
    }

    public class GroupLogAdapterViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        LinearLayout layoutTop, layoutBottom, layoutBottom1, layoutBottom2, layoutBottom3;

        TextView logText, logTimestamp, logOld, logNew, logBy;

        public GroupLogAdapterViewHolder(View itemView) {
            super(itemView);

            if(itemView != null) {
                cardView = itemView.findViewById(R.id.log_cardview);

                layoutTop = itemView.findViewById(R.id.linear_log1);
                logTimestamp = itemView.findViewById(R.id.alt_log_timestamp);

                logText =  itemView.findViewById(R.id.alt_log_text);

                layoutBottom = itemView.findViewById(R.id.linear_log2);

                layoutBottom1 = itemView.findViewById(R.id.linear_log3);
                logOld = itemView.findViewById(R.id.alt_log_old);

                layoutBottom2 = itemView.findViewById(R.id.linear_log4);
                logNew = itemView.findViewById(R.id.alt_log_new);

                layoutBottom3 = itemView.findViewById(R.id.linear_log5);
                logBy = itemView.findViewById(R.id.alt_log_by);
            }
        }
    }
}
