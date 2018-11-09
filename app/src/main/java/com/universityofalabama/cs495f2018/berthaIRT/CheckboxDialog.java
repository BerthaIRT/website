package com.universityofalabama.cs495f2018.berthaIRT;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CheckboxDialog extends AlertDialog.Builder{
    RecyclerView rv;
    CheckboxAdapter adapter;
    public CheckboxDialog(Context ctx, List<Boolean> boolList, List<String> labelList, Util.CheckboxOnClickInterface onOkClicked){
        super(ctx);
        View v = ((AppCompatActivity) ctx).getLayoutInflater().inflate(R.layout.dialog_checkboxes, null);
        rv = v.findViewById(R.id.checkboxes_rv);

        adapter = new CheckboxAdapter(ctx);
        for(int i=0; i<labelList.size(); i++){
            adapter.boolList.add(boolList.get(i));
            adapter.labelList.add(labelList.get(i));
        }
        adapter.notifyDataSetChanged();
        rv.setAdapter(adapter);

        v.findViewById(R.id.checkboxes_button_confirm).setOnClickListener(l->{
            onOkClicked.onClick(adapter.boolList);
        });
        setView(v);
    }

    @Override
    public AlertDialog show() {
        return super.show();
    }

    class CheckboxAdapter extends RecyclerView.Adapter<CheckboxViewHolder>{
        List<Boolean> boolList;
        List<String> labelList;
        Context ctx;

        public CheckboxAdapter(Context c){
            ctx = c;
            this.labelList = new ArrayList<>();
            this.boolList = new ArrayList<>();
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
    }

    class CheckboxViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        ImageView bChecked, bUnchecked;
        CardView cv;

        public CheckboxViewHolder(View itemView){
            super(itemView);
            tv = itemView.findViewById(R.id.checkbox_alt_text);
            cv = itemView.findViewById(R.id.checkbox_cv);
            bChecked = itemView.findViewById(R.id.checkbox_button_active);
            bUnchecked = itemView.findViewById(R.id.checkbox_button_inactive);
        }
    }
}
