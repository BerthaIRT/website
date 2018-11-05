package com.ua.cs495_f18.berthaIRT;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReportLogObject {

    private String text;
    private String oldItem;
    private String newItem;
    private String timestamp;
    private String admin;

    /**
     * ReportLogObject Initializer
     * @param t is the return of one of the functions below
     */
    public ReportLogObject(String t) {
        this.text = t;
        this.oldItem = "";
        this.newItem = "";
        this.timestamp = new SimpleDateFormat("MM/dd/yy hh:mm a", Locale.getDefault()).format(new Date());

        //TODO set their admin name if they are an admin
        this.admin = "";
    }

    public static String newReportCreated() {
        return "Report created.";
    }

    public static String reportAssigned(String admin) {
        return "Report assigned to " + admin.toUpperCase() + ".";
    }

    public static String reportAccepted() {
        return "Report accepted.";
    }

    public static String reportStatusUpdated() {
        return "Report status updated.";
    }

    public static String reportDetailsUpdated() {
        return "Report details updated.";
    }

    public static String reportNotesUpdated() {
        return "Report notes updated.";
    }

    public static String reportMediaUpdated() {
        return "Report media updated.";
    }

    public static String reportNewMessage() {
        return "Report message received.";
    }

    public String getText() {
        return text;
    }

    public String getOldItem() {
        return oldItem;
    }

    public String getNewItem() {
        return newItem;
    }

    public String getAdmin() {
        return admin;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setOldItem(String item) {
        oldItem = item;
    }

    public void setNewItem(String item) {
        newItem = item;
    }

    public void setAdmin(String name) {
        admin =  name;
    }
}
