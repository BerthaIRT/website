package com.ua.cs495_f18.berthaIRT;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ua.cs495_f18.berthaIRT.Adapter.MessageAdapter;

import java.util.ArrayList;
import java.util.Map;

public class MessagingActivity extends AppCompatActivity {

    private EditText editMessageText;
    private MessageAdapter messageAdapter;
    private ListView messagesView;
    private ImageView messageError;

    private ArrayList<MessageObject> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.toolbar_chat);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        toolbar.setNavigationOnClickListener(v -> finish());

        editMessageText = findViewById(R.id.input_chat_message);

        messageAdapter = new MessageAdapter(this);
        messagesView = findViewById(R.id.view_chat_messages);
        messagesView.setAdapter(messageAdapter);

        messageError = findViewById(R.id.not_sent_error);
        messageError.setOnClickListener(v -> resendMessage());
    }

    public void sendMessage() {
        String input = editMessageText.getText().toString();
        //makes sure that the input is something
        if (input.length() > 0) {
            MessageObject messageObject = new MessageObject(input,"12313", true);
            messageAdapter.add(messageObject);

            //TODO Scott look at
            Client.net.secureSend("message/newmessage", null, (r)->{
                JsonObject jay = new JsonObject();
                jay.addProperty("id", r);
                jay.addProperty("data", Client.net.gson.toJson(messageObject));

                Client.net.secureSend("message/submit", jay.toString(), (rr)->{
                    if(!rr.equals("ALL GOOD HOMIE")){
                        messageError.setVisibility(View.VISIBLE);
                    }
                });
            });

            editMessageText.getText().clear();

            //TEMP to simulate reply
            MessageObject messageObject1 = new MessageObject(input,"123413", false);
            messageAdapter.add(messageObject1);

            //scrolls to the bottom of the list of messages
            messagesView.smoothScrollToPosition(messagesView.getCount() - 1);
        }
    }

    public void resendMessage() {
        //get the last message you tried to send
        MessageObject messageObject = (MessageObject) messageAdapter.getItem(messageAdapter.getCount() - 1);
        //TODO Scott look at
        Client.net.secureSend("message/newmessage", null, (r)->{
            JsonObject jay = new JsonObject();
            jay.addProperty("id", r);
            jay.addProperty("data", Client.net.gson.toJson(messageObject));

            Client.net.secureSend("message/submit", jay.toString(), (rr)->{
                if(rr.equals("ALL GOOD HOMIE")){
                    messageError.setVisibility(View.GONE);
                }
            });
        });

    }

    public void recieveMessage() {
        messageList = new ArrayList<>();
        //TODO Scott look at
        Client.net.secureSend("message/get", null, (r)->{
            JsonObject jay = Client.net.jp.parse(r).getAsJsonObject();
            for(Map.Entry<String, JsonElement> e : jay.entrySet())
                messageList.add(Client.net.gson.fromJson(e.getValue().getAsString(), MessageObject.class));
        });

        for(MessageObject messageObject : messageList) {
            messageAdapter.add(messageObject);
        }
    }


}