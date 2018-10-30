package com.ua.cs495_f18.berthaIRT;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.ua.cs495_f18.berthaIRT.Fragment.AdminAllReportsFragment;
import com.ua.cs495_f18.berthaIRT.Fragment.AdminOpenReportsFragment;
import com.ua.cs495_f18.berthaIRT.Fragment.AdminRequiresActionFragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
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

    public static void showSimpleAlert(Context context, String title, String text, DialogInterface.OnClickListener f) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(text);
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", f);
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

    public static String listToString(List<String> l){
        if(l.size() == 0) return "N/A";
        String s = "";
        for (String str : l)
            s = s + str + ", ";
        return s.substring(0, s.length()-2);
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

    public  interface ValidateInterface{
        void validate();
    }


    public static TextWatcher validater(ValidateInterface iv) {
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
}