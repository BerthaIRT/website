package com.universityofalabama.cs495f2018.berthaIRT.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.universityofalabama.cs495f2018.berthaIRT.Client;
import com.universityofalabama.cs495f2018.berthaIRT.Message;
import com.universityofalabama.cs495f2018.berthaIRT.R;
import com.universityofalabama.cs495f2018.berthaIRT.Report;
import com.universityofalabama.cs495f2018.berthaIRT.Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }

    private Context ctx;
    private List<Message> data;
    private RecyclerViewClickListener mListener;


    public MessageAdapter(Context c, RecyclerViewClickListener l) {
        ctx = c;
        mListener = l;
        data = Client.activeReport.getMessages();
    }

    public void updateMessages(Collection<Message> c){
        data = new ArrayList<>(c);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.adapter_message, parent, false); //todo: should not be attached to parent
        return new MessageViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = data.get(position);

//        if(diffDate(position,message)) {
//            holder.msgDate.setVisibility(View.VISIBLE);
//            holder.msgDate.setText(Util.formatTimestamp(message.tStamp));
//        }
        holder.msgDate.setVisibility(View.VISIBLE);
        holder.msgDate.setText(Util.formatTimestamp(message.getMessageTimestamp()));

        if(message.getMessageSubject().equals(Client.net.pool.getCurrentUser())) {
            holder.rightMsgLayout.setVisibility(RelativeLayout.VISIBLE);
            holder.rightMsgText.setText(message.getMessageBody());
            holder.rightMsgTime.setText(Util.formatTimestamp(message.getMessageTimestamp()));

            holder.leftMsgLayout.setVisibility(RelativeLayout.GONE);
            //holder.msgSendError.setVisibility(message.getSendingErrorVisibility());

            //make time invisible because error message will be in it's place
            if(holder.msgSendError.getVisibility() == View.VISIBLE)
                holder.rightMsgTime.setVisibility(View.GONE);

            else {
                //Listener for showing time
                holder.rightMsgText.setOnClickListener(v -> {
                    if (holder.rightMsgTime.getVisibility() == View.GONE)
                        holder.rightMsgTime.setVisibility(View.VISIBLE);
                    else
                        holder.rightMsgTime.setVisibility(View.GONE);
                });
            }

            //makes the time visible if it was the last sent
            if(position == data.size()-1)
                holder.rightMsgTime.setVisibility(View.VISIBLE);
            else
                holder.rightMsgTime.setVisibility(View.GONE);
        }
        else {
            holder.leftMsgLayout.setVisibility(RelativeLayout.VISIBLE);
            holder.leftMsgText.setText(message.getMessageBody());
            holder.leftMsgTime.setText(Util.justGetTheFuckingTime(message.getMessageTimestamp()));
            holder.rightMsgLayout.setVisibility(RelativeLayout.GONE);

            //Listener for showing time
            holder.leftMsgText.setOnClickListener(v -> {
                if (holder.leftMsgTime.getVisibility() == View.GONE)
                    holder.leftMsgTime.setVisibility(View.VISIBLE);
                else
                    holder.leftMsgTime.setVisibility(View.GONE);
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        LinearLayout leftMsgLayout;
        LinearLayout rightMsgLayout;

        TextView leftMsgText, rightMsgText, leftMsgTime, rightMsgTime, msgDate, msgSendError;

        MessageViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);

            leftMsgLayout =  itemView.findViewById(R.id.chat_left_msg_layout);
            rightMsgLayout = itemView.findViewById(R.id.chat_right_msg_layout);
            leftMsgText = itemView.findViewById(R.id.left_message_body);
            rightMsgText = itemView.findViewById(R.id.right_message_body);
            leftMsgTime = itemView.findViewById(R.id.left_message_time);
            rightMsgTime = itemView.findViewById(R.id.right_message_time);
            msgDate = itemView.findViewById(R.id.message_date);
            msgSendError = itemView.findViewById(R.id.message_error);
            mListener = listener;
            rightMsgText.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view, getAdapterPosition());
        }
    }
}
