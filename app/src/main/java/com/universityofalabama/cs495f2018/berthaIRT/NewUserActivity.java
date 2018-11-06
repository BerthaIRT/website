
package com.universityofalabama.cs495f2018.berthaIRT;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

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

//        Client.net.secureSend("user/lookup", etAccessCode.getText().toString(), r->{
//            switch(r){
//                case "CLOSED":
//                    Util.showDialog(NewUserActivity.this, "Registration Closed", "The group you are trying to join is currently closed for registration.");
//                    etAccessCode.setText("");
//                    return;
//                case "NONE":
//                    Util.showDialog(NewUserActivity.this, "No Such Institution", "The access code you have entered is not valid.");
//                    etAccessCode.setText("");
//                    return;
//            }
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setView(v);
//            AlertDialog dialog = builder.create();
//
//            v.findViewById(R.id.confirmsignup_button_no).setOnClickListener(x -> dialog.dismiss());
//            v.findViewById(R.id.confirmsignup_button_yes).setOnClickListener(x -> actionJoinGroup());
//        });
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(v);
            AlertDialog dialog = builder.create();

            v.findViewById(R.id.confirmsignup_button_no).setOnClickListener(x -> dialog.dismiss());
            v.findViewById(R.id.confirmsignup_button_yes).setOnClickListener(x -> actionJoinGroup());
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
