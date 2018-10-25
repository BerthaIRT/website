package com.ua.cs495_f18.berthaIRT;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import com.google.gson.JsonObject;

public class AdminCreateGroupActivity extends AppCompatActivity {
    private EditText et1, et2;
    private Button buttonCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_creategroup);

        et1 = findViewById(R.id.input_new_group_name);
        et2 = findViewById(R.id.input_new_group_email);
        buttonCreate = findViewById(R.id.button_create_new_group);
        buttonCreate.setOnClickListener(v -> actionCreateNewGroup());

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

        et1.addTextChangedListener(mWatcher);
        et2.addTextChangedListener(mWatcher);
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
        Client.net.secureSend("admin/creategroup", jay.toString(), (r)->{
            if(r.equals("AIGHT LOL")) {
                StaticUtilities.showSimpleAlert(AdminCreateGroupActivity.this, "Check Your Inbox", "An email has been sent to " + sEmail + " with login credentials and further instructions.");
                startActivity(new Intent(AdminCreateGroupActivity.this, AdminLoginActivity.class));
                finish();
            }
        });
    }

    //Makes Create Button unclickable and transparent until at least 1 character has been inputted in both fields.
    private void validateInput() {
        if ((TextUtils.isEmpty(et1.getText())) || (TextUtils.isEmpty(et2.getText()))){
            buttonCreate.setAlpha(.5f);
            buttonCreate.setEnabled(false);
        }
        else if(!(StaticUtilities.isEmailValid(et2.getText().toString()))){
            buttonCreate.setAlpha(.5f);
            buttonCreate.setEnabled(false);
        }
        else{
            buttonCreate.setAlpha(1);
            buttonCreate.setEnabled(true);
        }
    }
}