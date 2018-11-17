package com.universityofalabama.cs495f2018.berthaIRT.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.universityofalabama.cs495f2018.berthaIRT.R;
import com.universityofalabama.cs495f2018.berthaIRT.Log;
import com.universityofalabama.cs495f2018.berthaIRT.Util;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private List<Log> notes;

    public NotesAdapter(List<Log> notes) {
        this.notes = notes;
    }

    @NonNull
    @Override
    public NotesAdapter.NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_note, parent, false);
        return new NotesAdapter.NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.NotesViewHolder holder, int position) {
        Log notes = this.notes.get(position);
        holder.logTimestamp.setText(Util.formatTimestamp(notes.tStamp));
        holder.logText.setText(notes.logText);
        holder.logBy.setText(notes.sender);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class NotesViewHolder extends RecyclerView.ViewHolder{
        TextView logText, logTimestamp, logBy;

        NotesViewHolder(View itemView) {
            super(itemView);

            logTimestamp = itemView.findViewById(R.id.note_alt_timestamp);

            logText =  itemView.findViewById(R.id.note_alt_text);

            logBy = itemView.findViewById(R.id.note_alt_by);
        }
    }
}
