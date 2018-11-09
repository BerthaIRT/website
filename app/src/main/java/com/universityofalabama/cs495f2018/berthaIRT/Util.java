package com.universityofalabama.cs495f2018.berthaIRT;

import android.content.Context;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Util {

    public interface DialogOnClickInterface {
        void onClick();
    }

    public interface DialogInputOnClickInterface {
        void onClick(String s);
    }

    public interface AddRemoveOnClickInterface {
        void onClick(List<String> newList);
    }

    public interface CheckboxOnClickInterface {
        void onClick(List<String> newList);
    }
    //Simple dialog with OK button
    public static void showOkDialog(Context ctx, String title, String text, DialogOnClickInterface listener){
        View v = ((AppCompatActivity) ctx).getLayoutInflater().inflate(R.layout.dialog_general, null);

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
            if (listener != null) listener.onClick();
            dialog.dismiss();
        });

        dialog.show();
    }

    //Two-button dialog with listeners
    public static void showYesNoDialog(Context ctx, String title, String text, String yesButton, String noButton, DialogOnClickInterface yesListener, DialogOnClickInterface noListener){
        View v = ((AppCompatActivity) ctx).getLayoutInflater().inflate(R.layout.dialog_general, null);

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
            yesListener.onClick();
            dialog.dismiss();
        });
        v.findViewById(R.id.generaldialog_button_no).setOnClickListener(x->{
            if(noListener != null) noListener.onClick();
            dialog.dismiss();
        });
        dialog.show();
    }

    public static void showInputDialog(Context ctx, String label, String text, String hint, String btn, DialogInputOnClickInterface listener) {
        View v = ((AppCompatActivity) ctx).getLayoutInflater().inflate(R.layout.dialog_general_input, null);

        ((TextView) v.findViewById(R.id.dialog_generalinput_alt_label)).setText(label);

        if(text == null)
            v.findViewById(R.id.dialog_generalinput_alt_text).setVisibility(View.GONE);
        else
            ((TextView) v.findViewById(R.id.dialog_generalinput_alt_text)).setText(text);

        ((TextView) v.findViewById(R.id.dialog_generalinput_alt_button)).setText(btn);

        if(hint == null) hint="";
        ((EditText) v.findViewById(R.id.dialog_generalinput_input)).setText(hint);

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setView(v);
        AlertDialog dialog = builder.create();

        v.findViewById(R.id.dialog_generalinput_button).setOnClickListener(x -> {
            if (listener != null)
                listener.onClick(((EditText) v.findViewById(R.id.dialog_generalinput_input)).getText().toString());
            dialog.dismiss();
        });

        v.findViewById(R.id.dialog_generalinput_close).setOnClickListener(x -> dialog.dismiss());

        dialog.show();
    }

    public static void showCheckboxDialog(Context ctx, List<Boolean> boolList, List<String> labelList, Util.CheckboxOnClickInterface onOkClicked){
        View v = ((AppCompatActivity) ctx).getLayoutInflater().inflate(R.layout.dialog_checkboxes, null);
        RecyclerView rv = v.findViewById(R.id.checkboxes_rv);

        //if you passed it null
        if(boolList == null) {
            boolList = new ArrayList<>();
            for(int i = 0; i < labelList.size(); i++)
                boolList.add(false);
        }

        CheckboxAdapter adapter = new CheckboxAdapter(ctx, labelList, boolList);
        rv.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setView(v);
        AlertDialog dialog = builder.create();

        v.findViewById(R.id.checkboxes_button_confirm).setOnClickListener(l -> {
            //if(onOkClicked != null)
                onOkClicked.onClick(adapter.getCheckedItems());
            dialog.dismiss();
        });

        v.findViewById(R.id.checkboxes_button_close).setOnClickListener(x -> dialog.dismiss());

        dialog.show();
    }

    public static void showAddRemoveDialog(Context ctx, List<String> listData, Util.AddRemoveOnClickInterface onOkClicked) {
        View v = ((AppCompatActivity) ctx).getLayoutInflater().inflate(R.layout.dialog_addremove, null);
        RecyclerView rv = v.findViewById(R.id.addremove_rv);

        //if you passed null for listData
        if(listData == null)
            listData = new ArrayList<>();

        AddRemoveAdapter adapter = new AddRemoveAdapter(ctx, listData);
        rv.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setView(v);
        AlertDialog dialog = builder.create();

        v.findViewById(R.id.addremove_button_add).setOnClickListener(l->{
            EditText et = v.findViewById(R.id.addremove_input);
            adapter.dataList.add(et.getText().toString());
            et.setText("");
            adapter.notifyDataSetChanged();
        });

        v.findViewById(R.id.addremove_button_confirm).setOnClickListener(l->{
            if(onOkClicked != null)
                onOkClicked.onClick(adapter.dataList);
            dialog.dismiss();
        });

        v.findViewById(R.id.addremove_button_close).setOnClickListener(x -> dialog.dismiss());

        dialog.show();
    }

    public static class WaitDialog{
        TextView message;
        AlertDialog dialog;
        public WaitDialog(Context ctx){
            View v = ((AppCompatActivity) ctx).getLayoutInflater().inflate(R.layout.dialog_wait, null);
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

    public static String getDate(String timestamp) {
        return timestamp.substring(0, 8);
    }

    public static String getTime(String timestamp) {
        return timestamp.substring(9,17);
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

    public static String formatTimestamp(String time){
        Long l = Long.parseLong(time);
        Date d = new Date(l);
        return new SimpleDateFormat("MM/DD/yyyy hh:mma").format(d);
    }

}