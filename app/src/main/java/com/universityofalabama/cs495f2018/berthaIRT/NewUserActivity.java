
package com.universityofalabama.cs495f2018.berthaIRT;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.util.Log;

import com.google.gson.JsonObject;

public class NewUserActivity extends AppCompatActivity {
    EditText etAccessCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newuser);

        etAccessCode = findViewById(R.id.newuser_input_accesscode);

        CardView bJoin = findViewById(R.id.newuser_button_join);
        bJoin.setOnClickListener(v -> actionConfirmJoin());

        CardView bAdmin = findViewById(R.id.newuser_button_adminlogin);
        bAdmin.setOnClickListener(v -> startActivity(new Intent(NewUserActivity.this, AdminLoginActivity.class)));
    }

    private void actionConfirmJoin() {
        LayoutInflater flater = getLayoutInflater();
        View v = flater.inflate(R.layout.dialog_student_confirmsignup, null);

        Client.net.secureSend("user/lookup", etAccessCode.getText().toString(), r->{
            switch(r){
                case "CLOSED":
                    Util.showOkDialog(NewUserActivity.this, "Registration Closed", "The group you are trying to join is currently closed for registration.",null);
                    etAccessCode.setText("");
                    return;
                case "NONE":
                    Util.showOkDialog(NewUserActivity.this, "No Such Institution", "The access code you have entered is not valid.", null);
                    etAccessCode.setText("");
                    return;
                default:
                    break;
            }
            Util.showYesNoDialog(NewUserActivity.this, "Confirm", "Are you a student at " + r + "?", "Yes", "No", this::actionJoinGroup, null);
        });
    }

    private void actionJoinGroup() {
        Client.net.secureSend("user/join", etAccessCode.getText().toString(), r->{
            JsonObject jay = Client.net.jp.parse(r).getAsJsonObject();
            Util.writeToUserfile(NewUserActivity.this, jay);
            Client.net.secureSend("signin", jay.toString(), (rr) -> {
                if (rr.equals("NEW_PASSWORD_REQUIRED")){ //returned because students never set their password
                    startActivity(new Intent(this, StudentMainActivity.class));
                    finish();
                }
            });
        });
    }
}
