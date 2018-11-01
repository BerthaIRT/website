package com.ua.cs495_f18.berthaIRT;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;


public class UnregisteredPortalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unregistered_portal);
        Button buttonJoin = findViewById(R.id.button_accesscode_join);
        buttonJoin.setOnClickListener(v -> actionJoinGroup());

        TextView buttonHelp = findViewById(R.id.button_accesscode_help);
        buttonHelp.setOnClickListener(v -> actionShowHelp());

        Button buttonAdmin = findViewById(R.id.button_goto_adminlogin);
        buttonAdmin.setOnClickListener(v -> actionAdminGate());

        Button loginStudent = findViewById(R.id.button_TEMP_LOGIN);
        loginStudent.setOnClickListener(v -> Client.net.checkIfLoggedIn());
    }

    private void actionJoinGroup(){
        StaticUtilities.hideSoftKeyboard(UnregisteredPortalActivity.this);
        final EditText codeInput = findViewById(R.id.input_accesscode);
        String input = codeInput.getText().toString();
        final TextView errorMessageSlot  = findViewById(R.id.alt_accesscode_error);

        Client.net.secureSend("user/lookup", input, (r)->{
            switch (r) {
                case "CLOSED":
                    errorMessageSlot.setText(getResources().getString(R.string.message_error_closed));
                    errorMessageSlot.setVisibility(View.VISIBLE);
                    return;
                case "NONE":
                    errorMessageSlot.setText(getResources().getString(R.string.message_error_nonexistant));
                    errorMessageSlot.setVisibility(View.VISIBLE);
                    return;
                default:
                    errorMessageSlot.setVisibility(View.INVISIBLE);
                    break;
            }

            AlertDialog.Builder b = new AlertDialog.Builder(UnregisteredPortalActivity.this);
                    b.setCancelable(true);
                    b.setTitle("Confirm");
                    b.setMessage("Are you a student at " + r + "?");
                    b.setPositiveButton("Yes", (dialog, which) -> completeJoinGroup(codeInput.getText().toString()));
                    b.setNegativeButton("No",
                            (dialog, which) -> codeInput.setText(""));
                    AlertDialog confirmationDialog = b.create();
                    confirmationDialog.show();
                }
        );
    }

    private void completeJoinGroup(String groupCode) {
        Client.net.secureSend("user/join", groupCode, (r)->{
            JsonObject jay = Client.net.jp.parse(r).getAsJsonObject();
            StaticUtilities.writeToFile(UnregisteredPortalActivity.this,"user", jay.toString());
            Client.net.secureSend("signin", jay.toString(), (rr) -> {
                if (rr.equals("NEW_PASSWORD_REQUIRED")){
                    startActivity(new Intent(this, UserPortalActivity.class));
                    finish();
                }
            });
        });
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
