package com.ua.cs495_f18.berthaIRT;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.gson.JsonObject;

public class AdminCreateGroupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_creategroup);

        Button buttonCreate = findViewById(R.id.button_create_new_group);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                actionCreateNewGroup();
            }
        });
    }

    private void actionCreateNewGroup(){
        StaticUtilities.hideSoftKeyboard(AdminCreateGroupActivity.this);
        final EditText inputEmail = findViewById(R.id.input_new_group_email);
        EditText inputName = findViewById(R.id.input_new_group_name);
        final String sEmail = inputEmail.getText().toString();
        final String sName = inputName.getText().toString();
        JsonObject jay = new JsonObject();
        jay.addProperty("email", sEmail);
        jay.addProperty("name", sName);
        Client.net.secureSend("admin/newgroup", jay.toString(), (r)->{
            StaticUtilities.showSimpleAlert(AdminCreateGroupActivity.this, "Check Your Inbox", "An email has been sent to " + sEmail + " with login credentials and further instructions.");
            startActivity(new Intent(AdminCreateGroupActivity.this, AdminLoginActivity.class));
        });
    }
}