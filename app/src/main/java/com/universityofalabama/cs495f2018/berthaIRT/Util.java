package com.universityofalabama.cs495f2018.berthaIRT;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.universityofalabama.cs495f2018.berthaIRT.dialog.WaitDialog;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Util {

    //Returns a comma-delimited string
    public static String listToString(List<String> l){
        if(l.size() == 0) return "N/A";
        StringBuilder s = new StringBuilder();
        for (String str : l)
            s.append(str).append(", ");
        return s.substring(0, s.length() - 2);
    }

    public static void writeToUserfile(Context ctx, JsonObject j) {
        try {
            new File(ctx.getFilesDir(), "user.dat");
            FileOutputStream fos = ctx.openFileOutput("user.dat", Context.MODE_PRIVATE);
            fos.write(j.toString().getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JsonObject readFromUserfile(Context ctx) {
        try {
            new File(ctx.getFilesDir(), "user.dat");
            InputStreamReader isr = new InputStreamReader(ctx.openFileInput("user.dat"));
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String s;
            while ((s = br.readLine()) != null)
                sb.append(s).append("\n");
            br.close();
            return Client.net.jp.parse(sb.toString()).getAsJsonObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public  interface ValidateInterface{
        void validate();
    }

    public static TextWatcher validator(ValidateInterface iv) {
        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                iv.validate();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        return tw;
    }

    public static boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPasswordValid(String password) {
        return password != null && password.length() >= 6;
//        return password != null &&
//                password.length() >= 6 &&
//                password.length() <= 50 &&
//                password.matches(".*[A-Za-z].*") &&
//                password.matches(".*[0-9\\~\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)_+\\{\\}\\[\\]\\?<>|_].*");
    }

    //Used by BerthaNet to serialize base-64 encoded keys
    public static String asHex(byte buf[]) {
        StringBuilder strbuf = new StringBuilder(buf.length * 2);
        for (byte aBuf : buf) {
            if (((int) aBuf & 0xff) < 0x10) {
                strbuf.append("0");
            }
            strbuf.append(Long.toString((int) aBuf & 0xff, 16));
        }
        return strbuf.toString();
    }

    public static byte[] fromHexString(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static List<Boolean> getPreChecked(List<String> items, List<String> selected) {
        List<Boolean> checked = new ArrayList<>();
        for(int i = 0; i < items.size(); i++)
            checked.add(false);

        if(selected == null)
            return checked;

        //read through the array and see if it matches with the items
        for(int i = 0; i < items.size(); i++) {
            for(int j = 0; j < selected.size(); j++) {
                if(items.get(i).equals(selected.get(j)))
                    checked.set(i,true);
            }
        }
        return checked;
    }

    public static String formatTimestamp(long time){
        Date d = new Date(time);
        return new SimpleDateFormat("MM/dd/yy hh:mma").format(d);
    }

    public static String justGetTheFuckingTime(long time){
        Date d = new Date(time);
        return new SimpleDateFormat("hh:mma").format(d);
    }

    public static String formatDatestamp(long time){
        Date d = new Date(time);
        return new SimpleDateFormat("MM/dd/yy").format(d);
    }

    //Generates a 16-character password from charSet
    public static String generateRandomPassword() {
        char[] charSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQURSTUVWXYZ1234567890!@#$%^&*()[]{}/".toCharArray();
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < 16; i++)
            s.append(charSet[new Random().nextInt(charSet.length)]);
        return s.toString();
    }

//    public static void makeDummieReports(Context ctx, int num){
//        final String[] categoryItems = ctx.getResources().getStringArray(R.array.category_item);
//        List<String> temp = new ArrayList<>();
//        Collections.addAll(temp, categoryItems);
//        List<String> cats = new ArrayList<>();
//
//        //List of Strings for New/Open/Closed/Resolved
//        List<String> statuses = new ArrayList<>();
//        statuses.add("New");
//        statuses.add("Open");
//        statuses.add("Closed");
//        statuses.add("Resolved");
//
//        //List of Strings for tags
//        List<String> tagList = new ArrayList<>();
//        tagList.add("Billy");
//        tagList.add("Jill");
//        tagList.add("John");
//        tagList.add("Jack");
//        List<String> tagList1 = new ArrayList<>();
//
//        for(int i = 0, j = 0; i < num; i++,j+=43200) {
//            cats.add(temp.get(new Random().nextInt(temp.size()-1)));
//            tagList1.add(tagList.get(new Random().nextInt(tagList.size()-1)));
//            Report newReport = new Report();
//            newReport.setThreatLevel(((Integer) new Random().nextInt(4)).toString());
//            newReport.setDescription("I ate Chocolate");
//            newReport.setLocation("SchoolYard.");
//            newReport.setIncidentTimeStamp((System.currentTimeMillis() + j));
//            newReport.setCategories(cats);
//            newReport.setStatus(statuses.get(new Random().nextInt(statuses.size() - 1)));
//            newReport.setTags(tagList1);
//            Client.reportMap.put(newReport.reportID, newReport);
//            /*WaitDialog dialog = new WaitDialog(ctx);
//            dialog.show();
//            dialog.setMessage("Sending report...");
//            String jayReport = Client.net.gson.toJson(newReport);
//            Client.net.secureSend(ctx, "/report/new", jayReport, r->{
//                Client.activeReport = Client.net.gson.fromJson(r, Report.class);
//                dialog.dismiss();
//            });*/
//            cats.remove(0);
//            tagList1.remove(0);
//        }
//    }
}