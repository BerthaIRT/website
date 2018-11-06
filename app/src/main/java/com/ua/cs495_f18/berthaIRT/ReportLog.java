package com.ua.cs495_f18.berthaIRT;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReportLog {

    public String text;
    public String oldItem;
    public String newItem;
    public String timestamp;
    public String admin;

    /**
     * ReportLog Initializer
     * @param t is the return of one of the functions below
     */
    public ReportLog(String t) {
        text = t;
        oldItem = "";
        newItem = "";
        timestamp = new SimpleDateFormat("MM/dd/yy hh:mm a", Locale.getDefault()).format(new Date());

        //TODO set their admin name if they are an admin
        admin = "";
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
}
