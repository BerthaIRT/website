package com.ua.cs495_f18.berthaIRT;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportObject {
    public String reportId;
    public String submittedDate;
    public String submittedTime;
    public String date;
    public String time;
    public String status;
    public String location;
    public String threatLevel;
    public String description;
    public String notes;
    public String media;
    public String assignedTo;
    public List<String> keywords;
    public List<String> categories;
    public List<Message> messages;
    public List<ReportLog> reportLogs;

    public ReportObject(String i, String d, String t, List<String> c) {
        reportId = i;
        date = "N/A";
        time = "N/A";
        status = "Open";
        location = "N/A";
        threatLevel = t;
        description = d;
        notes = "";
        media = "N/A";
        assignedTo = "N/A";
        keywords = new ArrayList<>();
        categories = c;
        messages = new ArrayList<>();
        reportLogs = new ArrayList<>();

        submittedDate = new SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(new Date());
        submittedTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
    }
}