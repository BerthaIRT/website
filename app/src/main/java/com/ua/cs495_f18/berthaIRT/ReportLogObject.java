package com.ua.cs495_f18.berthaIRT;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReportLogObject {

    private String text;
    private String oldItem;
    private String newItem;
    private String date;
    private String time;
    private String admin;

    /**
     * ReportLogObject Initializer
     * @param t is the return of one of the functions below
     */
    public ReportLogObject(String t) {
        this.text = t;
        this.oldItem = "";
        this.newItem = "";
        this.date = new SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(new Date());
        this.time = new SimpleDateFormat("hh:mm tt", Locale.getDefault()).format(new Date());

        //TODO set their admin name if they are an admin
        this.admin = "";
    }

    public static String newReportCreated() {
        return "Report CREATED.";
    }

    public static String reportAssigned(String admin) {
        return "Report ASSIGNED TO " + admin.toUpperCase() + ".";
    }

    public static String reportAccepted() {
        return "Report ACCEPTED.";
    }

    public static String reportStatusUpdated() {
        return "Report STATUS updated.";
    }

    public static String reportDetailsUpdated() {
        return "Report DETAILS updated.";
    }

    public static String reportNotesUpdated() {
        return "Report NOTES updated.";
    }

    public static String reportMediaUpdated() {
        return "Report MEDIA updated.";
    }

    public static String reportNewMessage() {
        return "Report MESSAGE recieved.";
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

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
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
