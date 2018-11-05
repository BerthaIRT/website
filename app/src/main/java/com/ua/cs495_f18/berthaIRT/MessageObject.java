package com.ua.cs495_f18.berthaIRT;

import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MessageObject {
    private String text; // message body
    private String senderId; // data of the user that sent this message
    private String receiverId;
    private String date;
    private String time;
    private boolean sendingError;

    private boolean lastSent;

    public MessageObject(String text, String s, String r) {
        this.text = text;
        this.senderId = s;
        this.receiverId = r;
        this.date = new SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(new Date());
        this.time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        this.sendingError = false;
        this.lastSent = true;
    }

    public String getText() {
        return text;
    }

    public String getSenderID() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public boolean isSentByCurrentUser() {
        return Client.currentUser.equals(getSenderID());
    }

    public boolean isSendingError() {
        return sendingError;
    }

    public boolean isLastSent() {
        return lastSent;
    }

    public int getSendingErrorVisibility() {
        if (isSendingError())
            return View.VISIBLE;
        else
            return View.GONE;
    }

    public void setSendingError(boolean sendingError) {
        this.sendingError = sendingError;
    }

    public void setNotLast() {
        this.lastSent = false;
    }

}
