package com.ua.cs495_f18.berthaIRT;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;

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

        CognitoUserAttributes attributes = new CognitoUserAttributes();
        //attributes.addAttribute("email", email);
        attributes.addAttribute("given_name", ((EditText) findViewById(R.id.input_editprofile__name)).getText().toString());


//        GenericHandler callback = new GenericHandler() {
//            @Override
//            public void onSuccess() {
//                Toast.makeText(AdminRegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(AdminRegisterActivity.this, AdminLoginActivity.class));
//                finish();
//            }
//
//            @Override
//            public void onFailure(Exception exception) {
//                Toast.makeText(AdminRegisterActivity.this, exception.toString(), Toast.LENGTH_SHORT).show();
//            }
//        };
//
//        CognitoController.pool.signUpInBackground(email, password, attributes, null, callback);
    }
}
