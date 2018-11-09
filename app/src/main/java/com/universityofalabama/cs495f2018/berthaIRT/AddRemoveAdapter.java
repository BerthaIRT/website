package com.universityofalabama.cs495f2018.berthaIRT;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

class AddRemoveAdapter extends RecyclerView.Adapter<AddRemoveAdapter.AddRemoveViewHolder>{
    List<String> dataList;
    Context ctx;

    public AddRemoveAdapter(Context c, List<String> l){
        ctx = c;
        dataList = l;
    }

    @NonNull
    @Override
    public AddRemoveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.adapter_addremove, parent, false);
        return new AddRemoveViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AddRemoveViewHolder holder, int position) {
        holder.tv.setText(dataList.get(position));
        holder.bRemove.setOnClickListener(l->{
            dataList.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class AddRemoveViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        ImageView bRemove;

        public AddRemoveViewHolder(View itemView){
            super(itemView);
            tv = itemView.findViewById(R.id.addremove_alt_text);
            bRemove = itemView.findViewById(R.id.addremove_button_delete);
        }
    }
}