package com.ua.cs495_f18.berthaIRT;

import com.ua.cs495_f18.berthaIRT.Client;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GroupLogObject {

    private String text;
    private String oldItem;
    private String newItem;
    private String date;
    private String time;
    private String admin;

    public GroupLogObject (String t) {
        this.text = t;
        this.oldItem = "";
        this.newItem = "";
        this.date = new SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(new Date());
        this.time = new SimpleDateFormat("hh:mm tt", Locale.getDefault()).format(new Date());

        //TODO figure out if they are admin and set name
        this.admin = "";
    }

    public static String adminCreated(String admin) {
        return admin.toUpperCase() + " created.";
    }

    public static String adminRemoved(String admin) {
        return admin.toUpperCase() + " removed.";
    }

    public static String adminSignedIn(String admin) {
        return admin.toUpperCase() + "signed in";
    }

    public static String adminPasswordReset(String admin) {
        return admin.toUpperCase() + " request password change.";
    }

    public static String groupNameUpdated(String name) {
        return "Group name updated to " + name.toUpperCase() + ".";
    }

    public static String groupLogoUpdated() {
        return "Group logo updated.";
    }

    public static String registrationStatusChange(String status) {
        return "Registration was " + status + ".";
    }

    public void setOldItem(String oldItem) {
        this.oldItem = oldItem;
    }

    public void setNewItem(String newItem) {
        this.newItem = newItem;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
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

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getAdmin() {
        return admin;
    }
}
