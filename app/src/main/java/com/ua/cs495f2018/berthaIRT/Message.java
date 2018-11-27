package com.ua.cs495f2018.berthaIRT;

//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

//Message class used for messaging, alerts and logs.
public class Message {

    private Integer messageID; //to keep track of who has read it
    private Long messageTimestamp;
    private Integer reportID; //used only for Alerts to link to relevant report
    private String messageSubject;
    private String messageBody;

    public Message(){}

//    public Message(Client u, String body){
//        messageTimestamp = System.currentTimeMillis();
//        messageSubject = u.username;
//        messageBody = body;
//    }
//
//    public Message(Client u, String body, Integer reportID){
//        messageTimestamp = System.currentTimeMillis();
//        messageSubject = u.username;
//        messageBody = body;
//        this.reportID = reportID;
//    }

    public Integer getMessageID() { return messageID; }

    public void setMessageID(Integer messageID) { this.messageID = messageID; }

    public Long getMessageTimestamp() {
        return messageTimestamp;
    }

    public void setMessageTimestamp(Long messageTimestamp) {
        this.messageTimestamp = messageTimestamp;
    }

    public Integer getReportID() {
        return reportID;
    }

    public void setReportID(Integer reportID) {
        this.reportID = reportID;
    }

    public String getMessageSubject() {
        return messageSubject;
    }

    public void setMessageSubject(String messageSubject) {
        this.messageSubject = messageSubject;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

//    public class MessageConverter implements DynamoDBTypeConverter<String, Message> {
//        @Override
//        public String convert(Message object) {
//            return WSMain.gson.toJson(object);
//        }
//
//        @Override
//        public Message unconvert(String object) {
//            return WSMain.gson.fromJson(object, Message.class);
//        }
//    }
}