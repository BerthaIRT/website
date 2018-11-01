package com.ua.cs495_f18.berthaIRT.Adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ua.cs495_f18.berthaIRT.MessageObject;
import com.ua.cs495_f18.berthaIRT.R;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends BaseAdapter{

    private List<MessageObject> messageObjects = new ArrayList<>();
    private Context context;

    public MessageAdapter(Context context) {
        this.context = context;
    }

    public void add(MessageObject messageObject) {
        this.messageObjects.add(messageObject);
        notifyDataSetChanged(); // to render the list we need to notify
    }

    @Override
    public int getCount() {
        return messageObjects.size();
    }

    @Override
    public Object getItem(int i) {
        return messageObjects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    // This is the backbone of the class, it handles the creation of single ListView row (chat bubble)
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        MessageViewHolder holder = new MessageViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        MessageObject messageObject = messageObjects.get(i);
        if(messageObject.isBelongsToCurrentUser()) {
            convertView = messageInflater.inflate(R.layout.my_message, null);
            holder.messageDate = convertView.findViewById(R.id.my_message_date);
            holder.messageBody = convertView.findViewById(R.id.my_message_body);
            convertView.setTag(holder);
            if(diffDate(i, messageObject)) {
                holder.messageDate.setVisibility(View.VISIBLE);
                holder.messageDate.setText(messageObject.getDate());
            }
            holder.messageBody.setText(messageObject.getText());
        }
        else {
            convertView = messageInflater.inflate(R.layout.their_message, null);
            holder.messageDate = convertView.findViewById(R.id.their_message_date);
            holder.messageBody = convertView.findViewById(R.id.their_message_body);
            convertView.setTag(holder);
            if(diffDate(i, messageObject)) {
                holder.messageDate.setVisibility(View.VISIBLE);
                holder.messageDate.setText(messageObject.getDate());
            }
            holder.messageBody.setText(messageObject.getText());
        }
        return convertView;
    }

    //returns true if the index is 0 or the dates are different
    private boolean diffDate(int i, MessageObject messageObject) {
        if (i == 0)
            return true;
        return !(messageObjects.get(i-1).getDate().equals(messageObject.getDate()));
    }

}

class MessageViewHolder {
    public TextView messageBody;
    public TextView messageDate;
}
