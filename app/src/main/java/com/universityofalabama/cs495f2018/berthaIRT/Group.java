package com.universityofalabama.cs495f2018.berthaIRT;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

//@DynamoDBTable(tableName="group")
public class Group
{
    String groupID;
    String name;
    String status;
    int baseReportID;
    int baseStudentID;
    String alerts;
    List<String> admins;

    public Group(String groupID, String name){
        this.groupID = groupID;
        this.name = name;
        status = "Open";
        baseReportID = 1000;
        baseStudentID = 1000;
        alerts = "[]";
        admins = new ArrayList<>();
    }

    //@DynamoDBHashKey(attributeName="groupID")
    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Alert> getAlerts() {
        JsonArray a = Client.net.jp.parse(alerts).getAsJsonArray();
        List<Alert> l = new ArrayList<>();
        for(JsonElement s : a)
            l.add(Client.net.gson.fromJson(s.getAsString(), Alert.class));
        return l;
    }

    public void setAlerts(List<Alert> alerts) {
        JsonArray arr = new JsonArray();
        for(Alert a : alerts){
            arr.add(Client.net.gson.toJson(a, Alert.class));
        }
        this.alerts = arr.toString();
    }

    public List<String> getAdmins() {
        return admins;
    }

    public void setAdmins(List<String> admins) {
        this.admins = admins;
    }

    public int getBaseReportID() {
        return baseReportID;
    }

    public void setBaseReportID(int baseReportID) {
        this.baseReportID = baseReportID;
    }

    public int getBaseStudentID() {
        return baseStudentID;
    }

    public void setBaseStudentID(int baseStudentID) {
        this.baseStudentID = baseStudentID;
    }
}
