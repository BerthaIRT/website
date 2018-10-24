package com.ua.cs495_f18.berthaIRT;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaticUtilities {

    public static void showSimpleAlert(Context context, String title, String text) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(text);
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static String asHex(byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        for (int i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10) {
                strbuf.append("0");
            }
            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
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

    public static boolean validPassword(String password) {
        return password != null &&
                password.length() >= 8 &&
                password.length() <= 50 &&
                password.matches(".*[A-Za-z].*") &&
                password.matches(".*[0-9\\~\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)_+\\{\\}\\[\\]\\?<>|_].*");
    }

    public static boolean isPasswordValid(String password) {
        return password != null &&
                password.length() >= 8 &&
                password.length() <= 50 &&
                password.matches(".*[A-Za-z].*") &&
                password.matches(".*[0-9\\~\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)_+\\{\\}\\[\\]\\?<>|_].*");
    }

    //returns a StringBuilder of the list of strings
    public static StringBuilder getStringBuilder(List<String> string) {
        StringBuilder sb = new StringBuilder();
        String prefix = "";
        for (int i=0; i<string.size(); i++) {
            sb.append(prefix);
            prefix = ", ";
            //makes the last thing have an and
            if (i == string.size() - 2)
                prefix = ", and ";
            if (string.size() == 2)
                prefix = " and ";
            sb.append(string.get(i));
        }
        return sb;
    }

    //returns a list of the individual checked items
    public static List<String> getStringList(boolean[] checkedItems, String[] items) {
        List<String> sCheckedItems = new ArrayList<>();
        for (int i=0; i<checkedItems.length; i++) {
            if (checkedItems[i]) {
                sCheckedItems.add(items[i]);
            }
        }
        return sCheckedItems;
    }

    //Function that parses a string based off of commas into an array
    public static String[] getStringArray(String s) {
        return s.split("\\s*,\\s*|\\s*,\\s*and\\s*|\\s*and\\s*");
    }

    public static void writeToFile(Context c, String file, String s) {
        try {
            new File(c.getFilesDir(), file);
            FileOutputStream fos = c.openFileOutput(file, Context.MODE_PRIVATE);
            fos.write(s.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFromFile(Context c, String file) {
        try {
            new File(c.getFilesDir(), file);
            InputStreamReader isr = new InputStreamReader(c.openFileInput(file));
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String s;
            while ((s = br.readLine()) != null)
                sb.append(s).append("\n");
            br.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}