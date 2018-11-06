package com.ua.cs495_f18.berthaIRT;

import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Message {
    public String text; // message body
    public String senderId; // data of the user that sent this message
    public String receiverId;
    public String date;
    public String time;
    public boolean sendingError;

    public boolean lastSent;

    public Message(String t, String s, String r) {
        text = t;
        senderId = s;
        receiverId = r;
        date = new SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(new Date());
        time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        sendingError = false;
        lastSent = true;
    }

    public boolean isSentByCurrentUser() {
        return Client.currentUser.equals(senderId);
    }

    public int getSendingErrorVisibility() {
        if (sendingError)
            return View.VISIBLE;
        else
            return View.GONE;
    }
}
