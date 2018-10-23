package com.ua.cs495_f18.berthaIRT;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


public class UnregisteredPortalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unregistered_portal);
        final Button buttonJoin = (Button) findViewById(R.id.button_join);
        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                actionJoinGroup();
            }
        });

        final TextView buttonHelp = (TextView) findViewById(R.id.label_access_code_help);
        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                actionShowHelp();
            }
        });

        final Button buttonAdmin = (Button) findViewById(R.id.button_to_admin_login);
        buttonAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                actionAdminGate();
            }
        });
    }

    private void actionJoinGroup(){
        StaticUtilities.hideSoftKeyboard(UnregisteredPortalActivity.this);
        final EditText codeInput = findViewById(R.id.input_access_code);
        String input = codeInput.getText().toString();
        final TextView errorMessageSlot  = findViewById(R.id.label_access_code_error);
        startActivity(new Intent(UnregisteredPortalActivity.this, UserPortalActivity.class));
        finish();

//        if (response.equals("CLOSED")){
//            errorMessageSlot.setText(getResources().getString(R.string.message_error_closed));
//            errorMessageSlot.setVisibility(View.VISIBLE);
//            return;
//        }
//        else if (response.equals("NONE")){
//            errorMessageSlot.setText(getResources().getString(R.string.message_error_nonexistant));
//            errorMessageSlot.setVisibility(View.VISIBLE);
//            return;
//        }
//        else errorMessageSlot.setVisibility(View.INVISIBLE);
//
//        AlertDialog.Builder b = new AlertDialog.Builder(UnregisteredPortalActivity.this);
//        b.setCancelable(true);
//        b.setTitle("Confirm");
//        b.setMessage("Are you a student at " + response + "?");
//        b.setPositiveButton("Yes",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        startActivity(new Intent(UnregisteredPortalActivity.this,UserPortalActivity.class));
//                        //don't allow the app to go back
//                        finish();
//                    }
//                });
//        b.setNegativeButton("No",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        codeInput.setText("");
//                    }
//                });
//        AlertDialog confirmationDialog = b.create();
//        confirmationDialog.show();
    }

    private void actionShowHelp(){
        StaticUtilities.hideSoftKeyboard(UnregisteredPortalActivity.this);

        AlertDialog.Builder b = new AlertDialog.Builder(UnregisteredPortalActivity.this);
        b.setCancelable(true);
        b.setTitle("Access Code");
        b.setMessage("Your access code ensures that your reports are seen only by the right staff at your school.  Your name and other revealing details are never required unless you choose to provide them.  Ask your teachers or administrators for your school's code.");
        b.setPositiveButton("OK", null);
        AlertDialog confirmationDialog = b.create();
        confirmationDialog.show();
    }

    private void actionAdminGate(){
        startActivity(new Intent(UnregisteredPortalActivity.this, AdminLoginActivity.class));
    }


}
