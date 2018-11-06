package com.universityofalabama.cs495f2018.berthaIRT.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.universityofalabama.cs495f2018.berthaIRT.Message;
import com.universityofalabama.cs495f2018.berthaIRT.R;

import java.util.List;

/**
 * Created by Jerry on 12/19/2017.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageAdapterViewHolder> {

    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }

    private List<Message> messages;
    private RecyclerViewClickListener mListener;


    public MessageAdapter(List<Message> messages, RecyclerViewClickListener listener) {
        this.messages = messages;
        this.mListener = listener;
    }

    @Override
    public void onBindViewHolder(MessageAdapterViewHolder holder, int position) {
        Message message = messages.get(position);

        if(diffDate(position,message)) {
            holder.msgDate.setVisibility(View.VISIBLE);
            holder.msgDate.setText(message.date);
        }

        if(message.isSentByCurrentUser()) {
            holder.rightMsgLayout.setVisibility(RelativeLayout.VISIBLE);
            holder.rightMsgText.setText(message.text);
            holder.rightMsgTime.setText(message.time);

            holder.leftMsgLayout.setVisibility(RelativeLayout.GONE);
            holder.msgSendError.setVisibility(message.getSendingErrorVisibility());

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
            if(message.lastSent && (holder.msgSendError.getVisibility() != View.VISIBLE))
                holder.rightMsgTime.setVisibility(View.VISIBLE);
            else
                holder.rightMsgTime.setVisibility(View.GONE);
        }
        else {
            holder.leftMsgLayout.setVisibility(RelativeLayout.VISIBLE);
            holder.leftMsgText.setText(message.text);
            holder.leftMsgTime.setText(message.time);
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

    //returns true if the index is 0 or the dates are different
    private boolean diffDate(int i, Message message) {
        if (i == 0) return true;
        return !(messages.get(i-1).date.equals(message.date));
    }

    @Override
    public MessageAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.message_item_view, parent, false);
        return new MessageAdapterViewHolder(view,mListener);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public Object getItem(int i) {
        return messages.get(i);
    }

    public class MessageAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        LinearLayout leftMsgLayout;
        LinearLayout rightMsgLayout;

        TextView leftMsgText, rightMsgText, leftMsgTime, rightMsgTime, msgDate, msgSendError;

        public MessageAdapterViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);

            if(itemView != null) {
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
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view, getAdapterPosition());
        }
    }
}
