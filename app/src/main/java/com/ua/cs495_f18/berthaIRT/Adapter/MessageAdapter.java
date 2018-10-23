package com.ua.cs495_f18.berthaIRT.Adapter;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ua.cs495_f18.berthaIRT.Message;
import com.ua.cs495_f18.berthaIRT.R;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends BaseAdapter{

    private List<Message> messages = new ArrayList<>();
    private Context context;

    public MessageAdapter(Context context) {
        this.context = context;
    }

    public void add(Message message) {
        this.messages.add(message);
        notifyDataSetChanged(); // to render the list we need to notify
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
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
        Message message = messages.get(i);
        if(message.isBelongsToCurrentUser()) {
            convertView = messageInflater.inflate(R.layout.my_message, null);
            holder.messageDate = convertView.findViewById(R.id.my_message_date);
            holder.messageBody = convertView.findViewById(R.id.my_message_body);
            convertView.setTag(holder);
            if(diffDate(i,message)) {
                holder.messageDate.setVisibility(View.VISIBLE);
                holder.messageDate.setText(message.getDate());
            }
            holder.messageBody.setText(message.getText());
        }
        else {
            convertView = messageInflater.inflate(R.layout.their_message, null);
            holder.messageDate = convertView.findViewById(R.id.their_message_date);
            holder.messageBody = convertView.findViewById(R.id.their_message_body);
            convertView.setTag(holder);
            if(diffDate(i,message)) {
                holder.messageDate.setVisibility(View.VISIBLE);
                holder.messageDate.setText(message.getDate());
            }
            holder.messageBody.setText(message.getText());
        }
        return convertView;
    }

    //returns true if the index is 0 or the dates are different
    private boolean diffDate(int i, Message message) {
        if (i == 0)
            return true;
        return !(messages.get(i-1).getDate().equals(message.getDate()));
    }

}

class MessageViewHolder {
    public TextView messageBody;
    public TextView messageDate;
    public TextView messageTime;
}
