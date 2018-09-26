package com.example.cs495bertha.bertha;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.view.Menu;
import android.widget.Toast;

public class AdminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        //TODO Change this so that it gets the right one
        String schoolCode = "XXX-XXX";
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.editProfile) {
            startActivity(new Intent(AdminMainActivity.this,AdminEditProfileActivity.class));
        }
        else if (item.getItemId() == R.id.myCode) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AdminMainActivity.this);

            //TODO Look up string for school using code
            String schoolCode = "XXX";
            builder.setTitle("Your School Code Is: ");
            builder.setMessage(schoolCode);
            builder.setPositiveButton("OK",null);
            AlertDialog dialog = builder.show();
            TextView messageView = (TextView)dialog.findViewById(android.R.id.message);
            messageView.setGravity(Gravity.CENTER);
        }
        else if (item.getItemId() == R.id.menuLogout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AdminMainActivity.this);

            builder.setTitle("Are you sure you want to Logout?");
            builder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(AdminMainActivity.this,AdminLoginActivity.class));
                    //don't allow the app to go back
                    finish();
                }
            });
            builder.setNegativeButton(android.R.string.cancel, null);
            AlertDialog dialog = builder.show();
            TextView messageView = (TextView)dialog.findViewById(android.R.id.message);
            messageView.setGravity(Gravity.CENTER);
        }
        else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
