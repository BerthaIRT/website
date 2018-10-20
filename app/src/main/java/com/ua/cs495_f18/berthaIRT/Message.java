package com.ua.cs495_f18.berthaIRT;

public class Message {
    private String text; // message body
    private String id; // data of the user that sent this message
    private String date;
    private String time;
    private boolean belongsToCurrentUser; // is this message sent by us?

    public Message(String text, String id, String date, String time, boolean belongsToCurrentUser) {
        this.text = text;
        this.id = id;
        this.date = date;
        this.time = time;
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
