package com.example.cs495bertha.bertha;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    Button btnJoin, btnAdmin;
    EditText etCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        joinSchool();
        adminLogin();
    }

    private void joinSchool() {
        btnJoin = (Button) findViewById(R.id.btnJoin);
        etCode = (EditText) findViewById(R.id.txtCode);

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the school code
                String schoolCode = etCode.getText().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);

                //TODO Look up string for school using code
                String schoolName = "XXX";
                builder.setTitle("Is " + schoolName + " correct?");
                builder.setMessage("You entered " + schoolCode + " as the code");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(MainActivity.this,ReportMainActivity.class));
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        etCode.setText("");
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void adminLogin() {
        //Go to view for admin login activity
        btnAdmin = (Button) findViewById(R.id.btnAdmin);

        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
    }
}
