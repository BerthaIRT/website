package com.ua.cs495_f18.berthaIRT;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

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
    }
}