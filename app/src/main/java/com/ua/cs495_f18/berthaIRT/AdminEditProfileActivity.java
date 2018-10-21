package com.ua.cs495_f18.berthaIRT;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AdminEditProfileActivity extends AppCompatActivity {
    boolean forceNewPassword;
    boolean requireOldPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requireOldPassword = false;

        if(getIntent().getExtras() != null && getIntent().getExtras().getBoolean("isNewAdmin"))
            forceNewPassword = true;
        else
            forceNewPassword = false;
        setContentView(R.layout.activity_admin_editprofile);

        if(forceNewPassword)
            ((TextView) findViewById(R.id.subtitle_editprofile)).setVisibility(View.VISIBLE);

        View cardPassword = findViewById(R.id.layout_editprofile_password);
        cardPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                actionPasswordCard();
            }
        });

        Button buttonUpdate = findViewById(R.id.button_editprofile_update);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                actionUpdate();
            }
        });
    }

    private void actionPasswordCard() {
        TextView tvPassword = findViewById(R.id.label_editprofile_password);
        TextView tvNewPassword = findViewById(R.id.label_editprofile_new_password);
        TextView tvConfirm = findViewById(R.id.label_editprofile_new_password_confirm);
        EditText etPassword = findViewById(R.id.input_editprofile_password);
        EditText etNewPassword = findViewById(R.id.input_editprofile_new_password);
        EditText etConfirm = findViewById(R.id.input_editprofile_new_password_confirm);
        ImageView icon = findViewById(R.id.icon_editprofile_password);
        ImageView iconClose = findViewById(R.id.icon_editprofile_password_close);

        if(etPassword.getVisibility() == View.GONE){
            //To handle if the old password is required
            if (requireOldPassword) {
                tvPassword.setText("Old Password");
                tvNewPassword.setVisibility(View.VISIBLE);
                tvNewPassword.setText("New Password");
                etNewPassword.setVisibility(View.VISIBLE);
            }
            else
                tvPassword.setText("New Password");
            etPassword.setVisibility(View.VISIBLE);
            tvConfirm.setVisibility(View.VISIBLE);
            etConfirm.setVisibility(View.VISIBLE);
            icon.setVisibility(View.GONE);
            if(!forceNewPassword)
                iconClose.setVisibility(View.VISIBLE);
            return;
        }
        if(forceNewPassword)
            return;
        tvPassword.setText("Change Password");
        tvNewPassword.setVisibility(View.GONE);
        tvConfirm.setVisibility(View.GONE);
        etPassword.setVisibility(View.GONE);
        etNewPassword.setVisibility(View.GONE);
        etConfirm.setVisibility(View.GONE);
        icon.setVisibility(View.VISIBLE);
        iconClose.setVisibility(View.GONE);
    }

    private void actionUpdate() {
        String sName = ((EditText)findViewById(R.id.input_editprofile__name)).getText().toString();
        String sOldPassword = null;
        String sNewPassword;
        //to handle if the old password was inputted
        if (requireOldPassword) {
            sOldPassword = ((EditText)findViewById(R.id.input_editprofile_password)).getText().toString();
            sNewPassword = ((EditText)findViewById(R.id.input_editprofile_new_password)).getText().toString();
        }
        else
            sNewPassword = ((EditText)findViewById(R.id.input_editprofile_password)).getText().toString();

        String sConfirm = ((EditText)findViewById(R.id.input_editprofile_new_password_confirm)).getText().toString();

        if(sName.equals("")) {
            StaticUtilities.showSimpleAlert(this, "Blank Field", "You must provide a name.");
        }
        else if(!oldPasswordMatch(sOldPassword)) {
            StaticUtilities.showSimpleAlert(this, "Password Mismatch", "Old Password does not match");
        }
        else if(!sNewPassword.equals(sConfirm)){
            StaticUtilities.showSimpleAlert(this, "Password Mismatch", "Passwords do not match.");
        }
    }

    //TODO actually check
    private boolean oldPasswordMatch(String password) {
        if (password == null)
            return false;
        return true;
    }
}
