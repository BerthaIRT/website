package com.universityofalabama.cs495f2018.berthaIRT;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

/*    public static String getDate(String timestamp) {
        return timestamp.substring(0, 8);
    }

    public static String getTime(String timestamp) {
        return timestamp.substring(9,17);
    }*/

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
        return new SimpleDateFormat("MM/dd/yy hh:mm a").format(d);
    }

}