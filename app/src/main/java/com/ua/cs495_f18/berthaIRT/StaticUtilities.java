package com.ua.cs495_f18.berthaIRT;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.inputmethod.InputMethodManager;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class StaticUtilities {
    //public static String ip = "http://10.0.0.85/";
    public static String ip = "http://18.215.233.192/";
    public static RequestQueue rQ;
    public static Gson gson;
    public static CognitoUserPool pool;
    // ------ INTERFACE FUNCTIONS ------

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

    public static void hideSoftKeyboard(Activity activity) {
    InputMethodManager inputMethodManager =
            (InputMethodManager) activity.getSystemService(
                    Activity.INPUT_METHOD_SERVICE);
    inputMethodManager.hideSoftInputFromWindow(
            activity.getCurrentFocus().getWindowToken(), 0);
    }

    // ------ NETWORK FUNCTIONS ------
    public static void initNetwork(Context ctx){
        rQ = Volley.newRequestQueue(ctx);
        gson = new GsonBuilder().create();
        pool = new CognitoUserPool(ctx, "us-east-1_1abyUmkI0", "2rn0vgeukeugrvc4shmh9i4imu", "16t9950ttm4dgmd69lrt9581a6plpa6i9ovm5nndfauiot2239t8");
    }
}
