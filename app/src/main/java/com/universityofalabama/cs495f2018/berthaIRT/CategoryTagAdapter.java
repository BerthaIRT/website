package com.universityofalabama.cs495f2018.berthairt;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class CategoryTagAdapter extends RecyclerView.Adapter<CategoryTagViewHolder>{
    int layoutID;
    List<String> categoryList;

    public CategoryTagAdapter(Boolean isTag){
        categoryList = new ArrayList<>();
        if(!isTag) layoutID = R.layout.adapter_category;
        else layoutID = R.layout.adapter_tag;
    }
    @NonNull
    @Override
    public CategoryTagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutID, parent, false);
        return new CategoryTagViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryTagViewHolder holder, int position) {
        holder.tvCategory.setText(categoryList.get(position));
    }

    @Override
    public int getItemCount() {
        System.out.println(categoryList.size());
        return categoryList.size();
    }
}

class CategoryTagViewHolder extends RecyclerView.ViewHolder{
    TextView tvCategory;

    public CategoryTagViewHolder(View itemView){
        super(itemView);
        tvCategory = itemView.findViewById(R.id.adapter_alt_category);
    }
}