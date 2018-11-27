package com.ua.cs495f2018.berthaIRT;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.widget.EditText;

import com.ua.cs495f2018.berthaIRT.dialog.OkDialog;
import com.ua.cs495f2018.berthaIRT.dialog.YesNoDialog;

public class NewUserActivity extends AppCompatActivity {
    //Access code textfield
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

    @SuppressLint("InflateParams")
    private void actionConfirmJoin() {
        getLayoutInflater().inflate(R.layout.dialog_student_confirmsignup, null);

        try{
            Client.userGroupID = Integer.valueOf(etAccessCode.getText().toString());
        }
        catch (NumberFormatException e){
            etAccessCode.setText("");
            etAccessCode.setError("Invalid access code.");
            return;
        }
        //Look up group name and status, without having to be signed in (that's why netSend is used)
        Client.net.lookupGroup(this, ()->{
            //If group doesn't exist, response won't be a JSON
            if(Client.userGroupName.equals("NONE")){
                etAccessCode.setText("");
                etAccessCode.setError("Invalid access code.");
                return;
            }

            //Registration is closed
            if(Client.userGroupStatus.equals("Closed")){
                new OkDialog(NewUserActivity.this, "Registration Closed", "The group you are trying to join is currently closed for registration.",null).show();
                etAccessCode.setText("");
                return;
            }
            //Open for registration
            new YesNoDialog(NewUserActivity.this, "Confirm", "Are you a student at " + Client.userGroupName + "?", new Interface.YesNoHandler() {
                @Override
                public void onYesClicked() { actionJoinGroup(); }
                @Override
                public void onNoClicked() { }
            }).show();
        });
    }

    //After student confirms institution, finalize signup.
    //Server will generate a new Cognito user with the format "Student-(GroupID)-(StudentID)"
    //This username is returned from the server and is used for student login from now on
    //Since the password is randomized upon first login, it's ok to set each new user's password to be the same thing
    private void actionJoinGroup() {
        Client.net.netSend(this, "/group/join/student", etAccessCode.getText().toString(), r->
                //r will be the new student username string
                Client.net.performLogin(NewUserActivity.this, r, "BeRThAfirsttimestudent", false, x->{
                    //Login successful and details stored - launch main activity
                    if (x.equals("AUTHENTICATED")) {
                        startActivity(new Intent(NewUserActivity.this, StudentMainActivity.class));
                        finish();
                    }
        }));
    }
}