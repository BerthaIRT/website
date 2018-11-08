package com.universityofalabama.cs495f2018.berthaIRT;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CheckBoxAdapter extends RecyclerView.Adapter<CheckBoxAdapter.CheckBoxViewHolder>{

    private Context mCtx;
    private List<String> mData;
    private List<Boolean> mCheckedIds;

    public CheckBoxAdapter(Context mCtx, List<String> mData, List<Boolean> mCheckedIds){
        this.mCtx = mCtx;
        this.mData = mData;
        this.mCheckedIds = mCheckedIds;
    }

    @NonNull
    @Override
    public CheckBoxViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mCtx).inflate(R.layout.checkbox_view,parent,false);
        return new CheckBoxViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckBoxViewHolder holder, int position) {
        holder.textViewName.setText(mData.get(position));
//
        //in some cases, it will prevent unwanted situations
        holder.check.setOnCheckedChangeListener(null);

        //Set Original Status
        if (!mCheckedIds.get(position))
            holder.check.setChecked(true);
        else
            holder.check.setChecked(false);

        holder.check.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mCheckedIds.set(holder.getAdapterPosition(), isChecked);
            //Toast.makeText(mCtx, "Status is: " + isChecked + "", Toast.LENGTH_SHORT).show();
        });
        mCheckedIds.set(position,holder.check.isChecked());
    }

    @Override
    public int getItemCount() {
        if(mData == null)
            return 0;
        else
            return mData.size();
    }

    public class CheckBoxViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout singleCheckBox;
        private TextView textViewName;
        private CheckBox check;

        public CheckBoxViewHolder(View itemView){
            super(itemView);
            singleCheckBox = itemView.findViewById(R.id.layout_single_checkbox);
            textViewName = itemView.findViewById(R.id.name_tv);
            check = itemView.findViewById(R.id.chk_box);
            this.setIsRecyclable(false);
        }
    }

    public List<String> getCheckedItems(){
        List<String> selectedItems = new ArrayList<>();
        for(int i = 0; i < this.mData.size(); i++){
            if(this.mCheckedIds.get(i))
                selectedItems.add(0, this.mData.get(i));
        }
        return selectedItems;
    }
}