package com.universityofalabama.cs495f2018.berthaIRT;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MessagesFragment extends Fragment {

    private EditText editMessageText;
    private MessageAdapter adapter;
    private RecyclerView rv;
    List<Message> messageList = new ArrayList<>();

    ImageButton msgSendButton;

    public MessagesFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater flater, ViewGroup tainer, Bundle savedInstanceState){
        System.out.println("onCreateView (Message)");
        View v = flater.inflate(R.layout.fragment_messages, tainer, false);
        rv = v.findViewById(R.id.chat_recycler_view);

        MessageAdapter.RecyclerViewClickListener listener = (view, position) -> {
            if(messageList.get(position).sendingError) {
                resendMessage(position);
            }
        };

        adapter = new MessageAdapter(getContext(),messageList,listener);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(adapter);

        editMessageText = v.findViewById(R.id.input_chat_message);



        msgSendButton = v.findViewById(R.id.button_chat_send);
        msgSendButton.setOnClickListener(view -> sendMessage());

        editMessageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editMessageText.getText().length() > 0)
                    msgSendButton.setAlpha(1.0f);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        return v;
    }


    private void sendMessage() {
        String msgContent = editMessageText.getText().toString();
        if (!TextUtils.isEmpty(msgContent)) {

            //TEMP
            Client.currentUserName = "12345";

            Message message = new Message(msgContent, Client.currentUserName, "31321");
            messageList.add(message);
            List<String> t = new ArrayList<>();

            //TEMP
            Client.activeReport = new Report("i","t","",t);

            //get the current report Object and add the new message to its list
            Client.activeReport.messages.add(message);

            //TODO add the message to the reportLog

/*            //If there was a problem updating the report then set the error message
            if(!Client.updateReportMap(Client.activeReport)) {
                message.sendingError = true;
                messageList.set(messageList.size() - 1, message);
                adapter.notifyDataSetChanged();
            }*/

            //TEMP spot until we I get web services up
            //makes the error text show up
/*            Message temp = messageList.get(messageList.size() - 1);
            temp.setSendingError(true);
            messageList.set(messageList.size() - 1, temp);
            messageAdapter.notifyDataSetChanged();*/

            //makes the last sent items time visible
            for(int i = messageList.size() - 2; i >= 0; i --) {
                //gets that item
                Message temp = messageList.get(i);
                //make sure it belongs to the currentUser
                if(temp.isSentByCurrentUser()) {
                    temp.lastSent = false;
                    messageList.set(i, temp);
                    adapter.notifyDataSetChanged();
                    break;
                }
            }

            //replies back with the same for now
            Message msgDto1 = new Message(msgContent,"31321", Client.currentUserName);
            messageList.add(msgDto1);

            adapter.notifyItemInserted(messageList.size() - 1);
            rv.smoothScrollToPosition(messageList.size() - 1);
            editMessageText.setText("");
            msgSendButton.setAlpha(0.4f);
        }
    }

    public void resendMessage(int position) {
        Toast.makeText(getActivity(),"RESEND", Toast.LENGTH_SHORT).show();

        Message message = messageList.get(position);
        message.sendingError = false;

        //get the current report Object and add the new message
        Client.activeReport.messages.add(message);

        //If there was a problem updating the report then set the error message back
        /*if(!Client.updateReportMap(Client.activeReport))
            message.sendingError = true;*/

        messageList.set(position, message);
        adapter.notifyDataSetChanged();
    }

    public void recieveMessage() {
        //TODO Scott look at
        Client.net.secureSend("message/get", null, (r)->{
            JsonObject jay = Client.net.jp.parse(r).getAsJsonObject();
            for(Map.Entry<String, JsonElement> e : jay.entrySet())
                messageList.add(Client.net.gson.fromJson(e.getValue().getAsString(), Message.class));
        });
    }


}
