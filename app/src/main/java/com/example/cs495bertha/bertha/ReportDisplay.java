package com.example.cs495bertha.bertha;

public class ReportDisplay {
    private int reportId;
    private String keyTags, date, status; // add time later

    public int getReportId() {
        return reportId;
    }

    public String getKeyTags() {
        return keyTags;
    }

    public String getDate() {
        return date;
    }



    public String getStatus() {
        return status;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public void setKeyTags(String keyTags) {
        this.keyTags = keyTags;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public void setStatus(String status) {
        this.status = status;
    }

    public ReportDisplay(int reportId, String keyTags, String date, String time, String status) {
        this.reportId = reportId;
        this.keyTags = keyTags;
        this.date = date;
        this.status = status;

    }
}
