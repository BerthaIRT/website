package com.universityofalabama.cs495f2018.berthaIRT;

import java.util.ArrayList;
import java.util.List;

public class Report {
    public String reportId = "";
    public String creationTimestamp = "";
    public String lastActionTimestamp = "";
    public String incidentTimeStamp = "";
    public String status = "";
    public String location = "";
    public String threatLevel = "";
    public String description = "";
    public String notes = "";
    public String media = "";
    public List<String> assignedTo = new ArrayList<>();
    public List<String> tags = new ArrayList<>();
    public List<String> categories = new ArrayList<>();
    public List<Message> messages = new ArrayList<>();
    public List<Log> logs = new ArrayList<>();
}
