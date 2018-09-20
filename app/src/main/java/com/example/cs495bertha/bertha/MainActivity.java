package com.example.cs495bertha.bertha;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btnJoin, btnAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //pop up for school page
        btnJoin = (Button) findViewById(R.id.btn_join);

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                //TODO add string for school
                String schoolName = "XXX";
                builder.setTitle("Is " + schoolName + " correct?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO add new activity since confirm is hit
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO cancel is hit. just close pop-up
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //Go to view for admin login activity
        btnAdmin = (Button) findViewById(R.id.btn_admin);

        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
    }
}
