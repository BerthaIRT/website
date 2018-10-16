package com.ua.cs495_f18.berthaIRT;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.services.cognitoidentityprovider.model.AdminCreateUserResult;

public class AdminInviteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_invite);

        final Button buttonSubmitKey = (Button) findViewById(R.id.button_admin_submitkey);
        buttonSubmitKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                actionSubmitKey();
            }
        });
    }

    private void actionSubmitKey(){
        UtilityInterfaceTools.hideSoftKeyboard(AdminInviteActivity.this);
        final EditText inputEmail = findViewById(R.id.input_admin_invite_email); //check
        EditText inputConfirm = findViewById(R.id.input_admin_invite_confirm); //TODO

        final ForgotPasswordHandler resetPasswordCallback = new ForgotPasswordHandler() {
            @Override
            public void onSuccess() {
                AlertDialog.Builder b = new AlertDialog.Builder(AdminInviteActivity.this);
                b.setTitle("Success");
                b.setMessage("An email has been sent to " + inputEmail + ".  The new administrator will log in with the supplied credentials.");
                b.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(AdminInviteActivity.this, AdminPortalActivity.class));
                                finish();
                            }
                        });
                AlertDialog confirmationDialog = b.create();
                confirmationDialog.show();
            }

            @Override
            public void getResetCode(ForgotPasswordContinuation continuation) {
                String codeSentHere = continuation.getParameters().getDestination();
                continuation.setPassword(UtilityAppTools.assignTemporaryPassword());

                // Set the code to verify
                //continuation.setVerificationCode(code);
                continuation.continueTask();
            }
            public void onFailure(Exception exception) {
                Toast.makeText(AdminInviteActivity.this, exception.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        SignUpHandler newUserCallback = new SignUpHandler(){
            @Override
            public void onSuccess(CognitoUser user, boolean signUpConfirmationState, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
                user.forgotPasswordInBackground(resetPasswordCallback);
            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(AdminInviteActivity.this, exception.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        String tPass = UtilityAppTools.assignTemporaryPassword();
        CognitoUserAttributes blankAttribs = new CognitoUserAttributes();
        blankAttribs.addAttribute("given_name", "N/A");
        blankAttribs.addAttribute("tempPassword", tPass);
        blankAttribs.addAttribute("email", inputEmail.getText().toString());
        CognitoController.pool.signUpInBackground(inputEmail.getText().toString(), tPass, blankAttribs, null, newUserCallback);
    }
}