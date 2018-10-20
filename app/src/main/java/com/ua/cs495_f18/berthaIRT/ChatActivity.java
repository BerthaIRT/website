package com.ua.cs495_f18.berthaIRT;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.ua.cs495_f18.berthaIRT.Adapter.MessageAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class ChatActivity extends AppCompatActivity {

    private EditText editMessageText;
    private MessageAdapter messageAdapter;
    private ListView messagesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_chat);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editMessageText = (EditText) findViewById(R.id.input_message);

        messageAdapter = new MessageAdapter(this);
        messagesView = (ListView) findViewById(R.id.messages_view);
        messagesView.setAdapter(messageAdapter);

    }

    public void sendMessage(View view) {
        String input = editMessageText.getText().toString();
        if (input.length() > 0) {
            String date = new SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(new Date());
            String time = new SimpleDateFormat("hh:mm", Locale.getDefault()).format(new Date());
            Message message = new Message(input,"12313", date, time, true);
            messageAdapter.add(message);
            Message message1 = new Message(input,"123413", date, time, false);
            messageAdapter.add(message1);
            messagesView.smoothScrollToPosition(messagesView.getCount()-1);
            editMessageText.getText().clear();
        }
    }

}