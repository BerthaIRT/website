package com.universityofalabama.cs495f2018.berthaIRT.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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

import com.universityofalabama.cs495f2018.berthaIRT.Client;
import com.universityofalabama.cs495f2018.berthaIRT.R;
import com.universityofalabama.cs495f2018.berthaIRT.Report;
import com.universityofalabama.cs495f2018.berthaIRT.Report.Message;
import com.universityofalabama.cs495f2018.berthaIRT.adapter.MessageAdapter;

import java.util.ArrayList;
import java.util.List;

public class MessagesFragment extends Fragment {

    private EditText editMessageText;
    private MessageAdapter adapter;
    private RecyclerView rv;
    List<Report.Message> messageList = new ArrayList<>();

    ImageButton msgSendButton;

    public MessagesFragment(){

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater flater, ViewGroup tainer, Bundle savedInstanceState){
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

    @Override
    public void onResume(){
        super.onResume();
        populateMessage(Client.activeReport.messages);
    }

    private void populateMessage(List<Message> m) {
        messageList.clear();
        if(m.size() != 0) {
            messageList.addAll(m);
            adapter.notifyDataSetChanged();
            //scroll to the bottom
            rv.smoothScrollToPosition(messageList.size() - 1);
        }
    }


    private void sendMessage() {
        String msgContent = editMessageText.getText().toString();
        if (!TextUtils.isEmpty(msgContent)) {

            //get the current report Object and add the new message to its list
            //messageList.add(Client.activeReport.addMessage(msgContent));

/*            //If there was a problem updating the report then set the error message
            if(!//Client.updateReportMap()) {
                message.sendingError = true;
                messageList.set(messageList.size() - 1, message);
                adapter.notifyDataSetChanged();
            }*/

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
            //Message msgDto1 = new Message(msgContent,"31321");
            //messageList.add(msgDto1);

            //temp if until the messages are sent
            if(messageList.size() > 0) {
                adapter.notifyItemInserted(messageList.size() - 1);
                rv.smoothScrollToPosition(messageList.size() - 1);
            }
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
        /*if(!//Client.updateReportMap(Client.activeReport))
            message.sendingError = true;*/

        messageList.set(position, message);
        adapter.notifyDataSetChanged();
    }

//    public void recieveMessage() {
//        //TODO Scott look at
//        Client.net.secureSend("message/get", null, (r)->{
//            JsonObject jay = Client.net.jp.parse(r).getAsJsonObject();
//            for(Map.Entry<String, JsonElement> e : jay.entrySet())
//                messageList.add(Client.net.gson.fromJson(e.getValue().getAsString(), Message.class));
//        });
//    }


}
