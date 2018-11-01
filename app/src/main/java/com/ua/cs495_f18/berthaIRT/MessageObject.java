package com.ua.cs495_f18.berthaIRT;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MessageObject {
    private String text; // message body
    private String id; // data of the user that sent this message
    private String date;
    private String time;
    private boolean belongsToCurrentUser; // is this message sent by us?

    public MessageObject(String text, String id, boolean belongsToCurrentUser) {
        this.text = text;
        this.id = id;
        this.date = new SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(new Date());
        this.time = new SimpleDateFormat("hh:mm", Locale.getDefault()).format(new Date());
        this.belongsToCurrentUser = belongsToCurrentUser;
    }

    public String getText() {
        return text;
    }

    public String getSenderID() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public boolean isBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }
}
