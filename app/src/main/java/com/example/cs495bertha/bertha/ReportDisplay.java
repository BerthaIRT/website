package com.example.cs495bertha.bertha;

public class ReportDisplay {
    private String reportId, keyTags, time, date, status; // add time later

    public ReportDisplay(String reportId, String keyTags, String date, String time, String status) {
        this.reportId = reportId;
        this.keyTags = keyTags;
        this.date = date;
        this.time = time;
        this.status = status;

    }

    public String getReportId() {
        return reportId;
    }

    public String getKeyTags() {
        return keyTags;
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

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public void setKeyTags(String keyTags) {
        this.keyTags = keyTags;
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

}
