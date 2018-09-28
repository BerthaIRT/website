package com.ua.cs495_f18.berthaIRT;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AdminEnterKeyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_enterkey);

        final Button buttonSubmitKey = (Button) findViewById(R.id.button_admin_submitkey);
        buttonSubmitKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                actionSubmitKey();
            }
        });
    }

    private String validateKey(String inputCode, String inputKey){
        if(inputCode.equals("123456") && inputKey.equals("654321")) {
            return "Saban Prepatory Academy for Football Sciences";
        }
        return "INVALID";
    }

    private void actionSubmitKey(){
        UtilityInterfaceTools.hideSoftKeyboard(AdminEnterKeyActivity.this);
        final EditText inputCode = (EditText) findViewById(R.id.input_admin_accesscode);
        final EditText inputKey = (EditText) findViewById(R.id.input_admin_key);
        String institution = validateKey(inputCode.getText().toString(), inputKey.getText().toString());
        if(institution.equals("INVALID")) {
            TextView errorMessage = (TextView) findViewById(R.id.message_invalid_adminkey);
            errorMessage.setVisibility(View.VISIBLE);
            inputCode.setText("");
            inputKey.setText("");
        }
        else{
            AlertDialog.Builder b = new AlertDialog.Builder(AdminEnterKeyActivity.this);
            b.setCancelable(true);
            b.setTitle("Success");
            b.setMessage("Your administration key has been validated for " + institution + ".");
            b.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(AdminEnterKeyActivity.this, AdminEditActivity.class));
                            finish();
                        }
                    });
            AlertDialog confirmationDialog = b.create();
            confirmationDialog.show();
        }
    }
}