package com.universityofalabama.cs495f2018.berthaIRT;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Report {
    public String reportId;
    public String creationTimestamp;
    public String lastActionTimestamp;
    public String incidentTimeStamp;
    public String status;
    public String location;
    public String threatLevel;
    public String description;
    public String notes;
    public String media;
    public List<String> assignedTo;
    public List<String> tags;
    public List<String> categories;
    public List<Message> messages;
    public List<Log> logs;


    public Report(String id, String description, String threat, List<String> categories) {
        reportId = id;
        creationTimestamp = new SimpleDateFormat("MM/dd/yy hh:mm a", Locale.getDefault()).format(new Date());
        lastActionTimestamp = new SimpleDateFormat("MM/dd/yy hh:mm a", Locale.getDefault()).format(new Date());
        incidentTimeStamp = "";
        status = "Open";
        location = "";
        threatLevel = threat;
        this.description = description;
        notes = "";
        media = "";
        assignedTo = new ArrayList<>();
        tags = new ArrayList<>();
        this.categories = categories;
        messages = new ArrayList<>();
        logs = new ArrayList<>();
    }
}
