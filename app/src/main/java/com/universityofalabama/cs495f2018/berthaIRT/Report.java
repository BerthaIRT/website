package com.universityofalabama.cs495f2018.berthaIRT;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Report {
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
    public List<String> assignedTo;
    public List<String> keywords;
    public List<String> categories;

    public Report(String id, String description, String threat, List<String> categories) {
        reportId = id;
        date = "";
        time = "";
        status = "Open";
        location = "";
        threatLevel = threat;
        this.description = description;
        notes = "";
        media = "";
        assignedTo = new ArrayList<>();
        keywords = new ArrayList<>();
        this.categories = categories;

        submittedDate = new SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(new Date());
        submittedTime = new SimpleDateFormat("hh:mm", Locale.getDefault()).format(new Date());
    }
}
