package com.universityofalabama.cs495f2018.berthaIRT;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;


import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Util {

    public interface DialogOnClickInterface {
        void buttonClickListener();
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

    public static String showInputDialog(Context ctx, String label, String text, String btn, DialogOnClickInterface listener) {
        final String[] input = new String[1];
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
                listener.buttonClickListener();
            input[0] = ((EditText) v.findViewById(R.id.dialog_generalinput_input)).getText().toString();
            dialog.dismiss();
        });

        dialog.show();
        return input[0];
    }

    public static List<String> selectCategoriesDialog(Context ctx, String positive, DialogOnClickInterface listener){
        List<String> newCategories = new ArrayList<>();

        //TODO make a custom dialog box

        AlertDialog.Builder b = new AlertDialog.Builder(ctx);

        final String[] categoryItems = ctx.getResources().getStringArray(R.array.category_item);
        boolean[] checkedCategories = new boolean[categoryItems.length];

        b.setTitle("Select Categories");
        b.setCancelable(false);

        b.setMultiChoiceItems(categoryItems, checkedCategories, (dialog, position, isChecked) ->
                checkedCategories[position] = isChecked);

        b.setPositiveButton(positive, (dialogInterface, x) -> {
            for (int i = 0; i < checkedCategories.length; i++) {
                if (checkedCategories[i])
                    newCategories.add(categoryItems[i]);
            }
            //selects other if the user didn't pick one
            if (newCategories.size() == 0) {
                newCategories.add("Other");
            }
            if(listener != null)
                listener.buttonClickListener();
            dialogInterface.dismiss();
        });

        b.setNegativeButton("CANCEL", null);

        b.create().show();
        return newCategories;
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


}
