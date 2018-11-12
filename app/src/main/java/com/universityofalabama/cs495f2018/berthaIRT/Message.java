package com.universityofalabama.cs495f2018.berthaIRT;

import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Message {
    public String text; // message body
    private String senderId; // data of the user that sent this message
    public String date;
    public String time;
    public boolean sendingError;

    public boolean lastSent;

    public Message(String t, String s) {
        text = t;
        senderId = s;
        date = new SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(new Date());
        time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        sendingError = false;
        lastSent = true;
    }

    public boolean isSentByCurrentUser() {
        return Client.currentUserName.equals(senderId);
    }

    public int getSendingErrorVisibility() {
        if (sendingError)
            return View.VISIBLE;
        else
            return View.GONE;
    }
}
