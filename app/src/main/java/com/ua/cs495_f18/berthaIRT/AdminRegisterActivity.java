package com.ua.cs495_f18.berthaIRT;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;

public class AdminRegisterActivity extends AppCompatActivity {
    private Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);

        register = findViewById(R.id.button_admin_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionCompleteRegistration();
            }
        });
    }

    private void actionCompleteRegistration() {
        String email = ((EditText) findViewById(R.id.input_register_admin_email)).getText().toString();
        String password = ((EditText) findViewById(R.id.input_register_admin_password)).getText().toString();

        CognitoUserAttributes attributes = new CognitoUserAttributes();
        attributes.addAttribute("email", email);
        attributes.addAttribute("given_name", ((EditText) findViewById(R.id.input_register_admin_name)).getText().toString());

        SignUpHandler callback = new SignUpHandler() {
            @Override
            public void onSuccess(CognitoUser user, boolean signUpConfirmationState, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
                Toast.makeText(AdminRegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AdminRegisterActivity.this, AdminLoginActivity.class));
                finish();
            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(AdminRegisterActivity.this, exception.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        CognitoController.pool.signUpInBackground(email, password, attributes, null, callback);
    }
}
