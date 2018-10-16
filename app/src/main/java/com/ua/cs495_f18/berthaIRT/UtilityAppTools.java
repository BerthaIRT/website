package com.ua.cs495_f18.berthaIRT;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.util.Random;

public class UtilityAppTools {
    final static int ACCESS_CODE_LENGTH = 6;
    final static String ACCESS_CODE_CHARS = "0123456789ABCDEF";
    final static int TEMP_PASS_LENGTH = 8;
    final static String TEMP_PASS_CHARS = "0123456789ABCDEF";


    public static String assignAccessCode(){
        StringBuilder sb = new StringBuilder();
        while (sb.length() < ACCESS_CODE_LENGTH)
            sb.append(ACCESS_CODE_CHARS.charAt((int) (new Random().nextFloat() * ACCESS_CODE_CHARS.length())));
        return sb.toString();
    }

    public static String assignTemporaryPassword(){
        StringBuilder sb = new StringBuilder();
        while (sb.length() < ACCESS_CODE_LENGTH)
            sb.append(ACCESS_CODE_CHARS.charAt((int) (new Random().nextFloat() * ACCESS_CODE_CHARS.length())));
        return sb.toString();
    }

    public static void showSimpleAlert(Context context, String title, String text){
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
}
