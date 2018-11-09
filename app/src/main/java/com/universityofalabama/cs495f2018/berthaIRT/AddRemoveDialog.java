package com.universityofalabama.cs495f2018.berthaIRT;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AddRemoveDialog extends AlertDialog.Builder{
    RecyclerView rv;
    AddRemoveAdapter adapter;
    public AddRemoveDialog(Context ctx, List<String> listData, Util.AddRemoveOnClickInterface onOkClicked){
        super(ctx);
        View v = ((AppCompatActivity) ctx).getLayoutInflater().inflate(R.layout.dialog_addremove, null);
        rv = v.findViewById(R.id.addremove_rv);

        adapter = new AddRemoveAdapter(ctx);
        for(String s : listData) adapter.dataList.add(s);
        adapter.notifyDataSetChanged();
        rv.setAdapter(adapter);

        v.findViewById(R.id.addremove_button_add).setOnClickListener(l->{
            EditText et = v.findViewById(R.id.addremove_input);
            adapter.dataList.add(et.getText().toString());
            et.setText("");
            adapter.notifyDataSetChanged();
        });

        v.findViewById(R.id.addremove_button_confirm).setOnClickListener(l->{
            onOkClicked.onClick(adapter.dataList);
        });
        setView(v);
    }

    @Override
    public AlertDialog show() {
        return super.show();
    }

    class AddRemoveAdapter extends RecyclerView.Adapter<AddRemoveViewHolder>{
        List<String> dataList;
        Context ctx;

        public AddRemoveAdapter(Context c){
            ctx = c;
            dataList = new ArrayList<>();
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
