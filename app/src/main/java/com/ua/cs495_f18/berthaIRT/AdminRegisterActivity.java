package com.ua.cs495_f18.berthaIRT;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AdminRegisterActivity extends AppCompatActivity {
    private Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_editprofile);

        register = findViewById(R.id.button_editprofile_update);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionCompleteRegistration();
            }
        });
    }

    private void actionCompleteRegistration() {
        //String email = ((EditText) findViewById(R.id.input_editprofile__email)).getText().toString();
        String password = ((EditText) findViewById(R.id.input_editprofile__password)).getText().toString();
    }
}
