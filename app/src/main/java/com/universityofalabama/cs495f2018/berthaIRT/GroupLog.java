package com.universityofalabama.cs495f2018.berthaIRT;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GroupLog {

    public String text;
    public String oldItem;
    public String newItem;
    public String timestamp;
    public String admin;

    public GroupLog(String t) {
        text = t;
        oldItem = "";
        newItem = "";
        timestamp = new SimpleDateFormat("MM/dd/yy hh:mm a", Locale.getDefault()).format(new Date());

        //TODO figure out if they are admin and set name
        admin = "";
    }

    public static String adminCreated(String admin) {
        return admin + " created as admin.";
    }

    public static String adminRemoved(String admin) {
        return admin + " removed.";
    }

    public static String adminSignedIn(String admin) {
        return admin + " signed in";
    }

    public static String adminPasswordReset(String admin) {
        return admin + " requested a password change.";
    }

    public static String groupNameUpdated(String name) {
        return "Group name updated to " + name + ".";
    }

    public static String groupLogoUpdated() {
        return "Group logo updated.";
    }

    public static String registrationStatusChange(String status) {
        return "Registration now " + status.toLowerCase() + ".";
    }
}
