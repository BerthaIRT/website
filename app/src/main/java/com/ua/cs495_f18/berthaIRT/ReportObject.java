package com.ua.cs495_f18.berthaIRT;

import java.util.ArrayList;
import java.util.List;

public class ReportObject {
    private String reportId;
    private String date;
    private String time;
    private String status;
    private String location;
    private String threatLevel;
    private String description;
    private String notes;
    private String media;
    private List <String> keyTags;
    private List <String> categories;
    private List <String> adminsAssigned;

    public ReportObject(String reportId, List<String> keyTags, String date, String time, String status, String location, String threatLevel,
                        String description, String notes, String media, List<String> categories, List<String> adminsAssigned) {
        this.reportId = reportId;
        this.keyTags = keyTags;
        this.date = date;
        this.time = time;
        this.status = status;
        this.location = location;
        this.threatLevel = threatLevel;
        this.description = description;
        this.notes = notes;
        this.media = media;
        this.categories = categories;
        this.adminsAssigned = adminsAssigned;
    }

    public String getReportId() {
        return reportId;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public String getLocation() {
        return location;
    }

    public String getThreatLevel() {
        return threatLevel;
    }

    public String getDescription() {
        return description;
    }

    public String getNotes() {
        return notes;
    }

    public String getMedia() {
        return media;
    }

    public  List<String> getKeyTags() {
        return keyTags;
    }

    public List<String> getCategories() {
        return categories;
    }

    public List<String> getAdminsAssigned() {
        return adminsAssigned;
    }

    public String getKeyTagsString() {
        return StaticUtilities.getStringBuilder(getKeyTags()).toString();
    }

    public String getCategoriesString() {
        return StaticUtilities.getStringBuilder(getCategories()).toString();
    }

    public String getAdminsAssignedString() {
            return StaticUtilities.getStringBuilder(getAdminsAssigned()).toString();
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setThreatLevel(String threatLevel) {
        this.threatLevel = threatLevel;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public void setKeyTags(List<String> keyTags) {
        this.keyTags = keyTags;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public void setAdminsAssigned(List<String> adminsAssigned) {
        this.adminsAssigned = adminsAssigned;
    }
}
