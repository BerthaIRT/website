package com.ua.cs495_f18.berthaIRT;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
        map.put("1111111",new ReportObject("1111111", "Bullying", date, time, "Open"));
        map.put("3333333", new ReportObject("3333333", "Cheating", date, time, "Open"));
        map.put("6124511", new ReportObject("6124511", "Cyberbullying", date, time, "Open"));
        map.put("1111110",new ReportObject("1111110", "Bullying", date, time, "Open"));
        map.put("3333330", new ReportObject("3333330", "Cheating", date, time, "Open"));
        map.put("6124510", new ReportObject("6124510", "Cyberbullying", date, time, "Open"));
        map.put("1111100",new ReportObject("1111100", "Bullying", date, time, "Open"));
        map.put("3333300", new ReportObject("3333300", "Cheating", date, time, "Open"));
        map.put("6124500", new ReportObject("6124500", "Cyberbullying", date, time, "Open"));
    }
}
