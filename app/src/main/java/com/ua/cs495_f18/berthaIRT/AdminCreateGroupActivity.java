package com.ua.cs495_f18.berthaIRT;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import com.google.gson.JsonObject;

public class AdminCreateGroupActivity extends AppCompatActivity {
    private EditText etName, etEmail;
    private Button bCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_creategroup);

        etName = findViewById(R.id.input_creategroup_name);
        etEmail = findViewById(R.id.input_creategroup_email);
        bCreate = findViewById(R.id.button_creategroup_create);
        bCreate.setOnClickListener(v -> actionCreateNewGroup());

        validateInput();

        TextWatcher mWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateInput();
            }
            @Override
            public void afterTextChanged(Editable s) { }
        };

        etName.addTextChangedListener(mWatcher);
        etEmail.addTextChangedListener(mWatcher);
    }

    private void actionCreateNewGroup(){
        StaticUtilities.hideSoftKeyboard(AdminCreateGroupActivity.this);
        final String sEmail = etEmail.getText().toString();
        final String sName = etName.getText().toString();
        JsonObject jay = new JsonObject();
        jay.addProperty("email", sEmail);
        jay.addProperty("name", sName);
        Client.net.secureSend("admin/creategroup", jay.toString(), (r)->{
            if(r.equals("AIGHT LOL")) {
                StaticUtilities.showSimpleAlert(AdminCreateGroupActivity.this, "Check Your Inbox", "An email has been sent to " + sEmail + " with login credentials and further instructions.", (d, w) ->
                        finish());
            }
        });
    }

    //Makes Create Button unclickable and transparent until at least 1 character has been inputted in both fields.
    private void validateInput() {
        if ((TextUtils.isEmpty(etName.getText())) || (TextUtils.isEmpty(etEmail.getText()))){
            bCreate.setAlpha(.5f);
            bCreate.setEnabled(false);
        }
        else if(!(StaticUtilities.isEmailValid(etEmail.getText().toString()))){
            bCreate.setAlpha(.5f);
            bCreate.setEnabled(false);
        }
        else{
            bCreate.setAlpha(1);
            bCreate.setEnabled(true);
        }
    }
}