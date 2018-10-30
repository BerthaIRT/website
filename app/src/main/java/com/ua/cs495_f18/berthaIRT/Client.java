package com.ua.cs495_f18.berthaIRT;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client {
    public static BerthaNet net;
    public static boolean adminForceNewPassword = false;
    public static String currentUser;
    public static List<String> adminList;
    public static HashMap<String, ReportObject> reportMap;
    public static ReportObject activeReport;

    public static void updateReportMap(){
        reportMap = new HashMap<>();
        net.secureSend("report/get", null, (r)->{
            JsonObject jay = net.jp.parse(r).getAsJsonObject();
            for(Map.Entry<String, JsonElement> e : jay.entrySet())
                reportMap.put(e.getKey(), net.gson.fromJson(e.getValue().getAsString(), ReportObject.class));
        });
    }
}
