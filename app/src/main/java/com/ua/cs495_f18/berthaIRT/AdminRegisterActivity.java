package com.ua.cs495_f18.berthaIRT;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminRegisterActivity extends AppCompatActivity {
    private Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);
        register = (Button) findViewById(R.id.button_admin_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionCompleteRegistration();
            }
        });
    }

    private void actionCompleteRegistration() {
        startActivity(new Intent(AdminRegisterActivity.this, AdminPortalActivity.class));
        finish();
    }
}
