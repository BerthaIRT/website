package com.ua.cs495_f18.berthaIRT;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.UpdateAttributesHandler;

import java.util.List;
import java.util.Map;

public class AdminEditProfileActivity extends AppCompatActivity {
    boolean forceNewPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        TextView tvConfirm = findViewById(R.id.label_editprofile__confirm);
        EditText etPassword = findViewById(R.id.input_editprofile__password);
        EditText etConfirm = findViewById(R.id.input_editprofile__confirm);
        ImageView icon = findViewById(R.id.icon_editprofile_password);
        ImageView iconClose = findViewById(R.id.icon_editprofile_password_close);

        if(etPassword.getVisibility() == View.GONE){
            tvPassword.setText("New Password");
            tvConfirm.setVisibility(View.VISIBLE);
            etPassword.setVisibility(View.VISIBLE);
            etConfirm.setVisibility(View.VISIBLE);
            icon.setVisibility(View.GONE);
            if(!forceNewPassword) iconClose.setVisibility(View.VISIBLE);
            return;
        }
        if(forceNewPassword) return;
        tvPassword.setText("Change Password");
        tvConfirm.setVisibility(View.GONE);
        etPassword.setVisibility(View.GONE);
        etConfirm.setVisibility(View.GONE);
        icon.setVisibility(View.VISIBLE);
        iconClose.setVisibility(View.GONE);
    }

    private void actionUpdate() {
        EditText eName = findViewById(R.id.input_editprofile__name);
        EditText ePassword = findViewById(R.id.input_editprofile__password);
        EditText eConfirm = findViewById(R.id.input_editprofile__confirm);

        String sName = eName.getText().toString();
        String sPassword = ePassword.getText().toString();
        String sConfirm = eConfirm.getText().toString();

        if(sName.equals("")) {
            UtilityAppTools.showSimpleAlert(this, "Blank Field", "You must provide a name.");
        }
        else if(!sPassword.equals(sConfirm)){
            UtilityAppTools.showSimpleAlert(this, "Password Mismatch", "Passwords do not match.");
        }
        CognitoUserAttributes attrib = new CognitoUserAttributes();
        attrib.addAttribute("given_name", sName);
        attrib.addAttribute("email", CognitoController.userEmail);
        CognitoController.pool.getCurrentUser().updateAttributesInBackground(attrib, new UpdateAttributesHandler() {
            @Override
            public void onSuccess(List<CognitoUserCodeDeliveryDetails> attributesVerificationList) {
                UtilityAppTools.showSimpleAlert(AdminEditProfileActivity.this, "Success", "Updated details successfully.");
                startActivity(new Intent(AdminEditProfileActivity.this, AdminPortalActivity.class));
                finish();
            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(AdminEditProfileActivity.this, exception.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
