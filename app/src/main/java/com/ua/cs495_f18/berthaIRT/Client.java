package com.ua.cs495_f18.berthaIRT;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client {
    public static BerthaNet net;
    public static boolean adminForceNewPassword = false;
    public static String currentUser;
    public static List<String> adminList;
    public static HashMap<String, ReportObject> reportMap;
    public static ReportObject activeReport;

    public static void updateReportMap(){
        reportMap = new HashMap<>();
        net.secureSend("report/getall", null, (r)->{
            JsonObject jay = net.jp.parse(r).getAsJsonObject();
            for(Map.Entry<String, JsonElement> e : jay.entrySet())
                reportMap.put(e.getKey(), net.gson.fromJson(e.getValue().getAsString(), ReportObject.class));
        });
    }

    public static boolean updateReportMap(ReportObject reportObject) {
        //has to be atomic because it's updated in the lambda
        AtomicBoolean status = new AtomicBoolean(false);

        //updates that value in the reportMap with the new object
        reportMap.put(reportObject.reportId, reportObject);

        //Sends the report
        JsonObject jay = new JsonObject();
        jay.addProperty("id", reportObject.reportId);
        jay.addProperty("data", Client.net.gson.toJson(reportObject));

        Client.net.secureSend("report/update", jay.toString(), (rr)->{
            if(rr.equals("ALL GOOD HOMIE")){
                status.set(true);
            }
        });
        return status.get();
    }
}
