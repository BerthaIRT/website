package com.ua.cs495_f18.berthaIRT;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    List<MessageObject> messageList = new ArrayList<>();


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

        MessageAdapter.RecyclerViewClickListener listener = (view, position) -> resendMessage(position);

        messageAdapter = new MessageAdapter(messageList, listener);
        msgRecyclerView.setAdapter(messageAdapter);
        editMessageText = findViewById(R.id.input_chat_message);

        ImageButton msgSendButton = findViewById(R.id.button_chat_send);
        msgSendButton.setOnClickListener(view -> sendMessage());

    }

    private void sendMessage() {
        String msgContent = editMessageText.getText().toString();
        if (!TextUtils.isEmpty(msgContent)) {
            MessageObject messageObject = new MessageObject(msgContent, "12313", true);
            messageList.add(messageObject);

            //TODO Scott look at
            /*Client.net.secureSend("message/newmessage", null, (r) -> {
                JsonObject jay = new JsonObject();
                jay.addProperty("id", r);
                jay.addProperty("data", Client.net.gson.toJson(messageObject));

                Client.net.secureSend("message/submit", jay.toString(), (rr) -> {
                    if (!rr.equals("ALL GOOD HOMIE")) {
                        //makes the error icon show up
                        MessageObject temp = messageList.get(messageList.size() - 1);
                        temp.setSendingError(true);
                        messageList.set(messageList.size() - 1, temp);
                        messageAdapter.notifyDataSetChanged();
                    }
                });
            });*/
            //TEMP until we I get web services up
            //makes the error icon show up
            MessageObject temp = messageList.get(messageList.size() - 1);
            temp.setSendingError(true);
            messageList.set(messageList.size() - 1, temp);
            messageAdapter.notifyDataSetChanged();

            //replies back with the same for now
            MessageObject msgDto1 = new MessageObject(msgContent, "12313", false);
            messageList.add(msgDto1);

            messageAdapter.notifyItemInserted(messageList.size() - 1);
            msgRecyclerView.smoothScrollToPosition(messageList.size() - 1);
            editMessageText.setText("");
        }
    }

    public void resendMessage(int position) {
        Toast.makeText(this,"RESEND", Toast.LENGTH_SHORT).show();
        MessageObject messageObject = messageList.get(position);
        //get the last message you tried to send
        //TODO Scott look at
        /*Client.net.secureSend("message/newmessage", null, (r)->{
            JsonObject jay = new JsonObject();
            jay.addProperty("id", r);
            jay.addProperty("data", Client.net.gson.toJson(messageObject));

            Client.net.secureSend("message/submit", jay.toString(), (rr)->{
                if(rr.equals("ALL GOOD HOMIE")){

                }
            });
        });*/
    }

    public void recieveMessage() {
        //TODO Scott look at
        Client.net.secureSend("message/get", null, (r)->{
            JsonObject jay = Client.net.jp.parse(r).getAsJsonObject();
            for(Map.Entry<String, JsonElement> e : jay.entrySet())
                messageList.add(Client.net.gson.fromJson(e.getValue().getAsString(), MessageObject.class));
        });
    }
}