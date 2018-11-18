package com.universityofalabama.cs495f2018.berthaIRT;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

//@DynamoDBTable(tableName = "report")
public class Report {
    //Report ID.  Not exclusive between groups.
    public String reportID = "";

    //ID of the group the report is sent to
    //Used in combination with reportID to find reports in DB
    public String groupID = "";

    //Student ID who sent the report.
    //Needed to keep track of which reports to display to a student
    //Hidden on all reports when pulled by admin.
    public String studentID = "";

    //Server-generated timestamp of when the report was created.
    public long creationTimestamp = new Long(0);

    //Student-generated timestamp of when the incidient occured.
    public long incidentTimeStamp = new Long(0);

    //"New": Report has been created by a student and not yet set to OPEN by an administrator
    //"Open": An administrator has read and acknowledged the new report but has not assigned it
    //"Assigned": An administrator is assigned to be responsible for this open report
    //"Resolved": The incident has been resolved, and no further report updates are necessary
    //"Closed": The report is incomplete, not genuine, or has not been resolved. No further report updates are necessary
    public String status = "";

    //Student-generated optional location of incident
    public String location = "";

    //Scale from 1 to 5, gauged by the student
    public String threatLevel = "";

    //Manditory student description of incident
    public String description = "";

    //Email addresses of administrators assigned to monitor this report
    public List<String> assignedTo = new ArrayList<>();

    //Administrator-defined keywords
    public List<String> tags = new ArrayList<>();

    //Pre-defined keywords
    public List<String> categories = new ArrayList<>();

    //The following objects are really JsonArrays, but has to be a string to be stored as part of a Report object in the DB
    //The getter and setter functions are really important here as they handle converting to and from types
    public String messages = "[]";
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

    //Turn JsonArray String to a list of Log objects
    public List<Log> getMessages() {
        JsonArray m = Client.net.jp.parse(messages).getAsJsonArray();
        List<Log> l = new ArrayList<>();
        for(JsonElement s : m)
            l.add(Client.net.gson.fromJson(s.getAsString(), Log.class));
        return l;
    }

    //Turn a list of log objects to JsonArray
    public void setMessages(List<Log> messages) {
        JsonArray arr = new JsonArray();
        for(Log m : messages){
            arr.add(Client.net.gson.toJson(m, Log.class));
        }
        this.messages = arr.toString();
    }


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

