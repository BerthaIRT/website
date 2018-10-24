package com.ua.cs495_f18.berthaIRT;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

public class ReportMap {

    private HashMap<String, ReportObject> map;

    public ReportMap() {
        this.map = new HashMap<>();
    }

    public HashMap<String, ReportObject> getHashMap() {
        return this.map;
    }

    public void setHashMap (HashMap map) {
        this.map = map;
    }

    public void populateHashMap() {
        String date = new SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("hh:mm", Locale.getDefault()).format(new Date());
        List<String> s = new ArrayList<>();
        map.put("1111111",new ReportObject("1111111", makeListString("Bullying"), date, time, "Open", "Gym", "1", "N/A", "N/A", " ", makeListString("Bullying"), makeListString("John Doe")));
        map.put("3333333", new ReportObject("3333333", makeListString("Cheating"), date, time, "Open", "Gym", "1", "N/A", "N/A", " ", makeListString("Bullying"), makeListString("John Doe")));
        map.put("6124511", new ReportObject("6124511", makeListString("Cyberbullying"), date, time, "Open", "Gym", "1", "N/A", "N/A", " ", makeListString("Bullying"), makeListString("John Doe")));
        map.put("1111110",new ReportObject("1111110", makeListString("Bullying"), date, time, "Open", "Gym", "1", "N/A", "N/A", " ", makeListString("Bullying"), makeListString("John Doe")));
        map.put("3333330", new ReportObject("3333330", makeListString("Cheating"), date, time, "Open", "Gym", "1", "N/A", "N/A", " ", makeListString("Bullying"), makeListString("John Doe")));
        map.put("6124510", new ReportObject("6124510", makeListString("Cyberbullying"), date, time, "Open", "Gym", "1", "N/A", "N/A", " ", makeListString("Bullying"), makeListString("John Doe")));
        map.put("1111100",new ReportObject("1111100", makeListString("Bullying"), date, time, "Open", "Gym", "1", "N/A", "N/A", " ", makeListString("Bullying"), makeListString("John Doe")));
        map.put("3333300", new ReportObject("3333300", makeListString("Cheating"), date, time, "Open", "Gym", "1", "N/A", "N/A", " ", makeListString("Bullying"), makeListString("John Doe")));
        map.put("6124500", new ReportObject("6124500", makeListString("Cyberbullying"), date, time, "Open", "Gym", "1", "N/A", "N/A", " ", makeListString("Bullying"), makeListString("John Doe")));
    }

    //needed just for now
    private List<String> makeListString(String s) {
        List<String> list = new ArrayList<>();
        list.add(s);
        return list;
    }
}
