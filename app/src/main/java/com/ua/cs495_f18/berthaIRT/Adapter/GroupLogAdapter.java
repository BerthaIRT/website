package com.ua.cs495_f18.berthaIRT.Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ua.cs495_f18.berthaIRT.GroupLogObject;
import com.ua.cs495_f18.berthaIRT.R;

import java.util.List;

/**
 * Created by Jerry on 12/19/2017.
 */

public class GroupLogAdapter extends RecyclerView.Adapter<GroupLogAdapter.GroupLogAdapterViewHolder> {

    private List<GroupLogObject> groupLogObjects;


    public GroupLogAdapter(List<GroupLogObject> groupLogObjects) {
        this.groupLogObjects = groupLogObjects;
    }

    @Override
    public void onBindViewHolder(GroupLogAdapterViewHolder holder, int position) {
        GroupLogObject groupLogObject = groupLogObjects.get(position);
        holder.logText.setText(groupLogObject.getText());

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
        View view = layoutInflater.inflate(R.layout.group_log_view, parent, false);
        return new GroupLogAdapterViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return groupLogObjects.size();
    }

    public Object getItem(int i) {
        return groupLogObjects.get(i);
    }

    public class GroupLogAdapterViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        LinearLayout layoutTop, layoutBottom, layoutBottom1, layoutBottom2, layoutBottom3;

        TextView logText, logDate, logTime, logOld, logNew, logBy;

        public GroupLogAdapterViewHolder(View itemView) {
            super(itemView);

            if(itemView != null) {
                cardView = itemView.findViewById(R.id.group_log_cardview);

                layoutTop = itemView.findViewById(R.id.linear_group_log1);
                logDate = itemView.findViewById(R.id.alt_group_log_date);
                logTime = itemView.findViewById(R.id.alt_group_log_time);

                logText =  itemView.findViewById(R.id.alt_group_log_text);

                layoutBottom = itemView.findViewById(R.id.linear_group_log2);

                layoutBottom1 = itemView.findViewById(R.id.linear_group_log3);
                logOld = itemView.findViewById(R.id.alt_group_log_old);

                layoutBottom2 = itemView.findViewById(R.id.linear_group_log4);
                logNew = itemView.findViewById(R.id.alt_group_log_new);

                layoutBottom3 = itemView.findViewById(R.id.linear_group_log5);
                logBy = itemView.findViewById(R.id.alt_group_log_by);
            }
        }
    }
}
