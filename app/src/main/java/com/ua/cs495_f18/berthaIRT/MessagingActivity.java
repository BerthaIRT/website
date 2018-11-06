package com.ua.cs495_f18.berthaIRT;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ua.cs495_f18.berthaIRT.Adapter.MessageAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MessagingActivity extends AppCompatActivity {

    private EditText editMessageText;
    private MessageAdapter messageAdapter;
    private RecyclerView msgRecyclerView;
    LinearLayoutManager linearLayoutManager;
    List<Message> messageList = new ArrayList<>();

    ImageButton msgSendButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar_chat);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());

        msgRecyclerView = findViewById(R.id.chat_recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(linearLayoutManager);

        //interface for resending message
        MessageAdapter.RecyclerViewClickListener listener = (view, position) -> {
            if(messageList.get(position).sendingError) {
                resendMessage(position);
            }
        };

        messageAdapter = new MessageAdapter(messageList, listener);
        msgRecyclerView.setAdapter(messageAdapter);
        editMessageText = findViewById(R.id.input_chat_message);

        msgSendButton = findViewById(R.id.button_chat_send);
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
    }

    private void sendMessage() {
        String msgContent = editMessageText.getText().toString();
        if (!TextUtils.isEmpty(msgContent)) {

            //TEMP
            Client.currentUser = "12345";

            Message message = new Message(msgContent, Client.currentUser, "31321");
            messageList.add(message);
            List<String> t = new ArrayList<>();

            //TEMP
            Client.activeReport = new ReportObject("i","t","",t);

            //get the current report Object and add the new message to its list
            Client.activeReport.messages.add(message);

            //TODO add the message to the reportLog

            //If there was a problem updating the report then set the error message
            if(!Client.updateReportMap(Client.activeReport)) {
                message.sendingError = true;
                messageList.set(messageList.size() - 1, message);
                messageAdapter.notifyDataSetChanged();
            }

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
                    messageAdapter.notifyDataSetChanged();
                    break;
                }
            }

            //replies back with the same for now
            Message msgDto1 = new Message(msgContent,"31321", Client.currentUser);
            messageList.add(msgDto1);

            messageAdapter.notifyItemInserted(messageList.size() - 1);
            msgRecyclerView.smoothScrollToPosition(messageList.size() - 1);
            editMessageText.setText("");
            msgSendButton.setAlpha(0.4f);
        }
    }

    public void resendMessage(int position) {
        Toast.makeText(this,"RESEND", Toast.LENGTH_SHORT).show();

        Message message = messageList.get(position);
        message.sendingError = false;

        //get the current report Object and add the new message
        Client.activeReport.messages.add(message);

        //If there was a problem updating the report then set the error message back
        if(!Client.updateReportMap(Client.activeReport))
            message.sendingError = true;

        messageList.set(position, message);
        messageAdapter.notifyDataSetChanged();
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