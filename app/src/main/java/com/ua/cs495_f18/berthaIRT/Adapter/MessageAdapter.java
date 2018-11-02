package com.ua.cs495_f18.berthaIRT.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ua.cs495_f18.berthaIRT.MessageObject;
import com.ua.cs495_f18.berthaIRT.R;

import java.util.List;

/**
 * Created by Jerry on 12/19/2017.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageAdapterViewHolder> {

    private List<MessageObject> messageObjects;

    public MessageAdapter(List<MessageObject> messageObjects) {
        this.messageObjects = messageObjects;
    }

    @Override
    public void onBindViewHolder(MessageAdapterViewHolder holder, int position) {
        MessageObject messageObject = messageObjects.get(position);
        if(messageObject.isBelongsToCurrentUser()) {
            holder.rightMsgLayout.setVisibility(RelativeLayout.VISIBLE);
            holder.rightMsgText.setText(messageObject.getText());
            holder.rightMsgTime.setText(messageObject.getTime());
            holder.leftMsgLayout.setVisibility(RelativeLayout.GONE);
            holder.msgSendError.setVisibility(messageObject.getSendingErrorVisibility());

            if(diffDate(position,messageObject)) {
                holder.rightMsgDate.setVisibility(View.VISIBLE);
                holder.rightMsgDate.setText(messageObject.getDate());
            }
        }
        else {
            holder.leftMsgLayout.setVisibility(RelativeLayout.VISIBLE);
            holder.leftMsgText.setText(messageObject.getText());
            holder.leftMsgTime.setText(messageObject.getTime());
            holder.rightMsgLayout.setVisibility(RelativeLayout.GONE);

            if(diffDate(position,messageObject)) {
                holder.leftMsgDate.setVisibility(View.VISIBLE);
                holder.leftMsgDate.setText(messageObject.getDate());
            }
        }
    }

    //returns true if the index is 0 or the dates are different
    private boolean diffDate(int i, MessageObject messageObject) {
        if (i == 0) return true;
        return !(messageObjects.get(i-1).getDate().equals(messageObject.getDate()));
    }

    @Override
    public MessageAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.message_item_view, parent, false);
        return new MessageAdapterViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return messageObjects.size();
    }

    public Object getItem(int i) {
        return messageObjects.get(i);
    }

    public class MessageAdapterViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout leftMsgLayout;
        RelativeLayout rightMsgLayout;

        TextView leftMsgText, rightMsgText, leftMsgTime, rightMsgTime, leftMsgDate, rightMsgDate;
        ImageView msgSendError;

        public MessageAdapterViewHolder(View itemView) {
            super(itemView);

            if(itemView!=null) {
                leftMsgLayout =  itemView.findViewById(R.id.chat_left_msg_layout);
                rightMsgLayout = itemView.findViewById(R.id.chat_right_msg_layout);
                leftMsgText = itemView.findViewById(R.id.left_message_body);
                rightMsgText = itemView.findViewById(R.id.right_message_body);
                leftMsgTime = itemView.findViewById(R.id.left_message_time);
                rightMsgTime = itemView.findViewById(R.id.right_message_time);
                leftMsgDate = itemView.findViewById(R.id.left_message_date);
                rightMsgDate = itemView.findViewById(R.id.right_message_date);
                msgSendError = itemView.findViewById(R.id.message_error);
            }
        }
    }
}
