package com.ua.cs495_f18.berthaIRT;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;

public class AdminEditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_editprofile);

        updateFields();

        ImageView info = findViewById(R.id.button_password_requirements);
        info.setOnClickListener(v -> {
            TextView tvInfo = findViewById(R.id.subtitle_password_requirments);
            if(tvInfo.getVisibility() == View.GONE)
                tvInfo.setVisibility(View.VISIBLE);
            else
                tvInfo.setVisibility(View.GONE);
        });

        Button bUpdate = findViewById(R.id.button_editprofile_update);
        bUpdate.setOnClickListener(v -> actionUpdate());
    }

    private void updateFields() {
        Client.net.secureSend("admin/groupinfo", null, (r)->{
            JsonObject jay = Client.net.jp.parse(r).getAsJsonObject();
            ((TextView) findViewById(R.id.alt_editprofile_groupname)).setText(jay.get("name").getAsString());
            ((TextView) findViewById(R.id.alt_editprofile_groupid)).setText(jay.get("id").getAsString());
            ((TextView) findViewById(R.id.alt_editprofile_email)).setText(Client.currentUser);
        });
    }

    private void actionUpdate() {
        EditText etName = findViewById(R.id.input_editprofile_name);
        EditText etNewPassword = findViewById(R.id.input_editprofile_new_password);
        EditText etConfirm = findViewById(R.id.input_editprofile_new_password_confirm);
        String sName = etName.getText().toString();
        String sNewPassword = etNewPassword.getText().toString();
        String sConfirm = etConfirm.getText().toString();

        if(sName.equals(""))
            etName.setError("You must provide a name.");
        if(!StaticUtilities.isPasswordValid(sNewPassword)) {
            etNewPassword.setError("Your password must be at least 8 characters long and must contain at least a letter and either a number or special character.");
        }
        else if(!sNewPassword.equals(sConfirm)){
            etConfirm.setError("Passwords do not match.");
        }

        JsonObject jay = new JsonObject();
        jay.addProperty("name", sName);
        jay.addProperty("newpassword", sNewPassword);

        Client.net.secureSend("admin/updateinfo", jay.toString(), (r) -> onUpdateResponse(r));
    }

    private void onUpdateResponse(String r) {
        if(r.equals("ALL GOOD HOMIE")){
            StaticUtilities.showSimpleAlert(this, "Updated", "Your details have been updated.", (d, w)->{
                startActivity(new Intent(AdminEditProfileActivity.this, AdminPortalActivity.class));
                finish();
            });
        }
    }
}
