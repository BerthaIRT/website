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
}
