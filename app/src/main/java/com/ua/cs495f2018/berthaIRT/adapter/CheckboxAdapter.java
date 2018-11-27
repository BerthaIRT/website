package com.ua.cs495f2018.berthaIRT.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ua.cs495f2018.berthaIRT.R;

import java.util.ArrayList;
import java.util.List;


public class CheckboxAdapter extends RecyclerView.Adapter<CheckboxAdapter.CheckboxViewHolder>{
    private List<Boolean> boolList;
    private List<String> labelList;
    private Context ctx;

    public CheckboxAdapter(Context c, List<String> l, List<Boolean> b){
        ctx = c;
        this.labelList = l;
        this.boolList = b;
    }

    public List<String> getCheckedItems() {
        List<String> selectedItems = new ArrayList<>();
        for(int i = 0; i < this.labelList.size(); i++){
            if(this.boolList.get(i))
                selectedItems.add(this.labelList.get(i));
        }
        return selectedItems;
    }

    @NonNull
    @Override
    public CheckboxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.adapter_checkbox, parent, false);
        return new CheckboxViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckboxViewHolder holder, int position) {
        holder.tv.setText(labelList.get(position));

        //set the visibility before anything is checked
        if(boolList.get(position)){
            holder.bChecked.setVisibility(View.VISIBLE);
            holder.bUnchecked.setVisibility(View.INVISIBLE);
        }
        else {
            holder.bChecked.setVisibility(View.INVISIBLE);
            holder.bUnchecked.setVisibility(View.VISIBLE);
        }

        holder.cv.setOnClickListener(l->{
            if(!boolList.get(position)){
                boolList.set(position, true);
                holder.bChecked.setVisibility(View.VISIBLE);
                holder.bUnchecked.setVisibility(View.INVISIBLE);
                return;
            }
            boolList.set(position, false);
            holder.bChecked.setVisibility(View.INVISIBLE);
            holder.bUnchecked.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public int getItemCount() {
        return labelList.size();
    }

    class CheckboxViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        ImageView bChecked, bUnchecked;
        CardView cv;

        CheckboxViewHolder(View itemView){
            super(itemView);
            tv = itemView.findViewById(R.id.checkbox_alt_text);
            cv = itemView.findViewById(R.id.checkbox_cv);
            bChecked = itemView.findViewById(R.id.checkbox_button_active);
            bUnchecked = itemView.findViewById(R.id.checkbox_button_inactive);
        }
    }
}