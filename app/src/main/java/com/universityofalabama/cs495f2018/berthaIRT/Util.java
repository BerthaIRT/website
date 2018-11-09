package com.universityofalabama.cs495f2018.berthaIRT;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Util {

    public interface DialogOnClickInterface {
        void buttonClickListener();
    }

    public interface DialogInputOnClickInterface {
        void buttonClickListener(String s);
    }

    public interface DialogMultiSelectCheckboxOnClickInterface {
        void buttonClickListener(List<String> list);
    }

    //Simple dialog with OK button
    public static void showOkDialog(Context ctx, String title, String text, DialogOnClickInterface listener){
        LayoutInflater flater = ((AppCompatActivity) ctx).getLayoutInflater();
        View v = flater.inflate(R.layout.dialog_general, null);

        v.findViewById(R.id.generaldialog_button_ok).setVisibility(View.VISIBLE);
        v.findViewById(R.id.generaldialog_button_yes).setVisibility(View.GONE);
        v.findViewById(R.id.generaldialog_button_no).setVisibility(View.GONE);

        ((TextView) v.findViewById(R.id.generaldialog_alt_text)).setText(text);
        if(title == null) v.findViewById(R.id.generaldialog_alt_title).setVisibility(View.GONE);
        else ((TextView) v.findViewById(R.id.generaldialog_alt_title)).setText(title);

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setView(v);
        AlertDialog dialog = builder.create();

        v.findViewById(R.id.generaldialog_button_ok).setOnClickListener(x -> {
            if (listener != null) listener.buttonClickListener();
            dialog.dismiss();
        });

        dialog.show();
    }

    //Two-button dialog with listeners
    public static void showYesNoDialog(Context ctx, String title, String text, String yesButton, String noButton, DialogOnClickInterface yesListener, DialogOnClickInterface noListener){
        LayoutInflater flater = ((AppCompatActivity) ctx).getLayoutInflater();
        View v = flater.inflate(R.layout.dialog_general, null);

        v.findViewById(R.id.generaldialog_button_ok).setVisibility(View.GONE);
        v.findViewById(R.id.generaldialog_button_yes).setVisibility(View.VISIBLE);
        ((TextView) v.findViewById(R.id.generaldialog_alt_yes)).setText(yesButton);
        v.findViewById(R.id.generaldialog_button_no).setVisibility(View.VISIBLE);
        ((TextView) v.findViewById(R.id.generaldialog_alt_no)).setText(noButton);


        ((TextView) v.findViewById(R.id.generaldialog_alt_text)).setText(text);
        if(title == null) v.findViewById(R.id.generaldialog_alt_title).setVisibility(View.GONE);
        else ((TextView) v.findViewById(R.id.generaldialog_alt_title)).setText(title);

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setView(v);
        AlertDialog dialog = builder.create();

        v.findViewById(R.id.generaldialog_button_yes).setOnClickListener(x -> {
            yesListener.buttonClickListener();
            dialog.dismiss();
        });
        v.findViewById(R.id.generaldialog_button_no).setOnClickListener(x->{
            if(noListener != null) noListener.buttonClickListener();
            dialog.dismiss();
        });
        dialog.show();
    }

    public static void showInputDialog(Context ctx, String label, String text, String btn, DialogInputOnClickInterface listener) {
        LayoutInflater flater = ((AppCompatActivity) ctx).getLayoutInflater();
        View v = flater.inflate(R.layout.dialog_general_input, null);

        ((TextView) v.findViewById(R.id.dialog_generalinput_alt_label)).setText(label);

        if(text == null)
            v.findViewById(R.id.dialog_generalinput_alt_text).setVisibility(View.GONE);
        else
            ((TextView) v.findViewById(R.id.dialog_generalinput_alt_text)).setText(text);

        ((TextView) v.findViewById(R.id.dialog_generalinput_alt_button)).setText(btn);

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setView(v);
        AlertDialog dialog = builder.create();

        v.findViewById(R.id.dialog_generalinput_button).setOnClickListener(x -> {
            if (listener != null)
                listener.buttonClickListener(((EditText) v.findViewById(R.id.dialog_generalinput_input)).getText().toString());
            dialog.dismiss();
        });

        v.findViewById(R.id.dialog_generalinput_close).setOnClickListener(x -> dialog.dismiss());

        dialog.show();
    }

    public static void showSelectCategoriesDialog (Context ctx, List<Boolean> checkedItems, List<String> items, DialogMultiSelectCheckboxOnClickInterface listener) {
        LayoutInflater flater = ((AppCompatActivity) ctx).getLayoutInflater();
        View v = flater.inflate(R.layout.checkbox_view_recycler, null);

        RecyclerView rvTest = v.findViewById(R.id.rec_view);
        CheckBoxAdapter cbAdapter = new CheckBoxAdapter(ctx,items,checkedItems);
        rvTest.setLayoutManager(new LinearLayoutManager(ctx));
        rvTest.setAdapter(cbAdapter);

        //TODO Actually get the selected Items from cbAdapter

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx,R.style.AppCompatAlertDialogStyle);
        builder.setView(v);
        builder.setPositiveButton("OK", (dialog, which) -> {
            if(listener != null) {
                List<String> selectedItems = new ArrayList<>();
                selectedItems = cbAdapter.getCheckedItems();
                listener.buttonClickListener(selectedItems);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.setNeutralButton("Clear All", null);
        AlertDialog dialog = builder.create();

/*        v.findViewById(R.id.dialog_generalinput_button).setOnClickListener(x -> {
            if (listener != null)
                listener.buttonClickListener();
            dialog.dismiss();
        });*/

        dialog.show();
    }

    public static List<Boolean> getPreChecked(List<String> items, List<String> selected) {
        List<Boolean> checked = new ArrayList<>();
        for(int i = 0; i < items.size(); i++) {
            checked.add(false);
        }

        //read through the array and see if it matches with the items
        for(int i = 0; i < items.size(); i++) {
            for(int j = 0; j < selected.size(); j++) {
                if(items.get(i).equals(selected.get(j)))
                    checked.set(i,true);
            }
        }
        System.out.println(checked.size());
        return checked;
    }
    public static class WaitDialog{
        TextView message;
        AlertDialog dialog;
        public WaitDialog(Context ctx){
            LayoutInflater flater = ((AppCompatActivity) ctx).getLayoutInflater();
            View v = flater.inflate(R.layout.dialog_wait, null);
            message = v.findViewById(R.id.waitdialog_alt_text);
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setView(v);
            dialog = builder.create();
        }
    }

    //Returns a comma-delimited string
    public static String listToString(List<String> l){
        if(l.size() == 0) return "N/A";
        StringBuilder s = new StringBuilder();
        for (String str : l)
            s.append(str).append(", ");
        return s.substring(0, s.length()-2);
    }

    public static void writeToUserfile(Context c, JsonObject j) {
        try {
            new File(c.getFilesDir(), "user.dat");
            FileOutputStream fos = c.openFileOutput("user.dat", Context.MODE_PRIVATE);
            fos.write(j.toString().getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JsonObject readFromUserfile(Context c) {
        try {
            new File(c.getFilesDir(), "user.dat");
            InputStreamReader isr = new InputStreamReader(c.openFileInput("user.dat"));
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String s;
            while ((s = br.readLine()) != null)
                sb.append(s).append("\n");
            br.close();
            return Client.net.jp.parse(sb.toString()).getAsJsonObject();
        } catch (IOException e) {
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

    public static String getDate(String timestamp) {
        return timestamp.substring(0, 8);
    }

    public static String getTime(String timestamp) {
        return timestamp.substring(9,17);
    }


}
