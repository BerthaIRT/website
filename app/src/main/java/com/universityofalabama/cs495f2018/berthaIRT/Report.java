package com.universityofalabama.cs495f2018.berthaIRT;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

//@DynamoDBTable(tableName = "report")
public class Report {
    public String reportID = "";
    public String groupID = "";
    public String studentID = "";
    public long creationTimestamp = new Long(0);
    public long incidentTimeStamp = new Long(0);
    public String status = "";
    public String location = "";
    public String threatLevel = "";
    public String description = "";
    public List<String> assignedTo = new ArrayList<>();
    public List<String> tags = new ArrayList<>();
    public List<String> categories = new ArrayList<>();
    public List<Message> messages = new ArrayList<>();
    public String logs = "[]";
    public String notes = "[]";

    // @DynamoDBHashKey(attributeName="reportID")
    public String getReportID() {
        return reportID;
    }

    public void setReportID(String reportID) {
        this.reportID = reportID;
    }

    //@DynamoDBHashKey(attributeName="groupID")
    public String getGroupID() { return groupID; }

    public void setGroupID(String groupID) { this.groupID = groupID; }

    public String getStudentID() { return studentID; }

    public void setStudentID(String studentID) { this.studentID = studentID; }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public long getIncidentTimeStamp() {
        return incidentTimeStamp;
    }

    public void setIncidentTimeStamp(long incidentTimeStamp) {
        this.incidentTimeStamp = incidentTimeStamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getThreatLevel() {
        return threatLevel;
    }

    public void setThreatLevel(String threatLevel) {
        this.threatLevel = threatLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(List<String> assignedTo) {
        this.assignedTo = assignedTo;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) { this.tags = tags; }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    //public List<Message> getMessages() {
    //    return messages;
    //}

    //public void setMessages(List<Message> messages) {
    //    this.messages = messages;
    //}


    public List<Log> getLogs() {
        JsonArray a = Client.net.jp.parse(logs).getAsJsonArray();
        List<Log> l = new ArrayList<>();
        for(JsonElement s : a)
            l.add(Client.net.gson.fromJson(s.getAsString(), Log.class));
        return l;
    }

    public void setLogs(List<Log> logs) {
        JsonArray arr = new JsonArray();
        for(Log a : logs){
            arr.add(Client.net.gson.toJson(a, Log.class));
        }
        this.logs = arr.toString();
    }
    public List<Log> getNotes() {
        JsonArray a = Client.net.jp.parse(notes).getAsJsonArray();
        List<Log> l = new ArrayList<>();
        for(JsonElement s : a)
            l.add(Client.net.gson.fromJson(s.getAsString(), Log.class));
        return l;
    }

    public void setNotes(List<Log> notes) {
        JsonArray arr = new JsonArray();
        for(Log a : notes){
            arr.add(Client.net.gson.toJson(a, Log.class));
        }
        this.notes = arr.toString();
    }
}

