package com.ua.cs495_f18.berthaIRT;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AdminEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit);

        final EditText lockedEmail = findViewById(R.id.input_edit_admin_email);
        lockedEmail.setFocusable(false);
        lockedEmail.setFocusableInTouchMode(false);
        lockedEmail.setClickable(false);
        lockedEmail.setText("nsaban@ua.edu");

        final Button buttonUpdate = findViewById(R.id.button_admin_update);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                actionUpdateAdminInfo();
            }
        });
    }

    private void actionUpdateAdminInfo(){
        UtilityInterfaceTools.hideSoftKeyboard(AdminEditActivity.this);
        //final EditText inputEmail = findViewById(R.id.input_edit_admin_email);
        final EditText inputName = findViewById(R.id.input_edit_admin_name);
        final EditText inputPassword = findViewById(R.id.input_edit_admin_password);
        final EditText inputConfirm = findViewById(R.id.input_edit_admin_confirm);
        //String sEmail = inputEmail.getText().toString();
        String sName = inputName.getText().toString();
        String sPass = inputPassword.getText().toString();
        String sConfirm = inputConfirm.getText().toString();
        boolean isComplete = true;

        //if(sEmail.equals("")){
        //    isComplete = false;
        //    findViewById(R.id.message_email_required).setVisibility(View.VISIBLE);
        //    inputConfirm.setText("");
        //}
        //else
        //    findViewById(R.id.message_email_required).setVisibility(View.INVISIBLE);

        if(sName.equals("")){
            isComplete = false;
            findViewById(R.id.message_name_required).setVisibility(View.VISIBLE);
            inputConfirm.setText("");
        }
        else
            findViewById(R.id.message_name_required).setVisibility(View.INVISIBLE);

        final TextView passwordErrorMessage = findViewById(R.id.message_password_required);
        if(sPass.equals("")){
            isComplete = false;
            passwordErrorMessage.setText(getResources().getString(R.string.message_required));
            passwordErrorMessage.setVisibility(View.VISIBLE);
            inputConfirm.setText("");
        }
        else if (!sPass.equals(sConfirm)) {
            isComplete = false;
            passwordErrorMessage.setText(getResources().getString(R.string.message_error_password_mismatch));
            findViewById(R.id.message_password_required).setVisibility(View.VISIBLE);
            inputPassword.setText("");
            inputConfirm.setText("");
        }
        else if (isComplete){
            Toast.makeText(AdminEditActivity.this, "Information for " + sName + " has been updated.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(AdminEditActivity.this, AdminPortalActivity.class));
            finish();
        }
    }
}
