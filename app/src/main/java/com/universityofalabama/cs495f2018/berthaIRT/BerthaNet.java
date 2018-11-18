package com.universityofalabama.cs495f2018.berthaIRT;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.UpdateAttributesHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.util.CognitoServiceConstants;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.universityofalabama.cs495f2018.berthaIRT.dialog.WaitDialog;

import java.io.Serializable;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class BerthaNet {
    static final String ip = "http://10.0.0.174:6969";

    //Utilities for converting objects to server-friendly JSONs
    public JsonParser jp;
    public Gson gson;

    //Volley RequestQueue
    RequestQueue netQ;

    //RSA keypair generated upon login and sent to Cognito
    KeyPair clientRSAKeypair;

    //Encrypter/Decrypters for secure HTTP
    //Valid for this session only and expires on application exit
    Cipher rsaDecrypter;
    Cipher aesEncrypter;
    Cipher aesDecrypter;

    //Cognito user pool.  Handles request security by itself
    public CognitoUserPool pool;

    //Stores JWKs and other security information
    CognitoUserSession session = null;

    //Declared here because it is passed between functions during login
    static WaitDialog dialog;

    public BerthaNet(Context c) {
        jp = new JsonParser();
        gson = new Gson();
        netQ = Volley.newRequestQueue(c);

        //Initialize AWS
        AWSMobileClient.getInstance().initialize(c).execute();

        //Configuration should be in /res/raw
        AWSConfiguration awsConfiguration = new AWSConfiguration(c);
        if (IdentityManager.getDefaultIdentityManager() == null) {
            final IdentityManager identityManager = new IdentityManager(c, awsConfiguration);
            identityManager.signOut();
            IdentityManager.setDefaultIdentityManager(identityManager);
        }
        pool = new CognitoUserPool(c, awsConfiguration);

        //If for some reason user obtains keys from last session, sign out / get rid of them.
        //If you don't sign out and try secure communication with the server the Client IDs wont match up and encryption fails
        if (pool.getCurrentUser() != null) pool.getCurrentUser().signOut();
    }

    //Basic interface for doing something with network response
    public interface NetSendInterface {
        void onResult(String response);
    }

    //Basic network HTTP request.
    //secureSend will call this function with strings already encrypted.
    //If the user is logged in, their JWT is attached to the Authentication header.
    //Only one string is sent as the body.  Up to calling functions to parse JSON / map values
    public void netSend(Context ctx, String path, final String body, final NetSendInterface callback) {
        StringRequest req = new StringRequest(Request.Method.PUT, ip.concat(path), callback::onResult, error -> {
            Toast.makeText(ctx, error.getMessage(), Toast.LENGTH_LONG).show();
            System.out.println(error.getMessage());
        }) {
            @Override
            public byte[] getBody(){
                return body.getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (session != null) {
                    Map<String, String> header = new HashMap<>();
                    header.put("Authentication", Util.asHex(session.getIdToken().getJWTToken().getBytes()));
                    return header;
                }
                return super.getHeaders();
            }
        };
        netQ.add(req);
    }

    //Secured network HTTP request.  Must be logged in with initialized ciphers.
    public void secureSend(Context ctx, String path, final String params, final NetSendInterface callback) {
        NetSendInterface wrapper = response -> {
            //Result will be hex-encoded for URL safety and encrypted with AES for security
            try {
                //Decode hex into bytes
                byte[] encrypted = Util.fromHexString(response);
                //Use cipher to decrypt bytes
                String decrypted = new String(aesDecrypter.doFinal(encrypted));
                System.out.println("Decrypted: " + decrypted);
                if(decrypted.equals("404")) callback.onResult("404");
                //Do the original callback with the decrypted string
                callback.onResult(decrypted);
            } catch (Exception e) {
                System.out.println("Unable to decrypt server response!");
                e.printStackTrace();
                callback.onResult("ENCRYPTION_FAILURE");
            }
        };
        //Encrypt the data
        byte[] encrypted = new byte[0];
        try {
            encrypted = aesEncrypter.doFinal(params.toString().getBytes());
        } catch (Exception e) {
            System.out.println("Unable to encrypt data packet!");
            e.printStackTrace();
            callback.onResult("ENCRYPTION_FAILURE");
        }
        //Data is byte code which won't translate well when sending over URL, so hex-encode it
        String encoded = Util.asHex(encrypted);
        //Send encrypted string as normal, with the wrapper as callback to decrypt response
        netSend(ctx, path, encoded, wrapper);
    }


    //Performs AWS Cognito login.
    //Occurs on both admin and student sign-in.
    public void performLogin(Context ctx, String username, String password, boolean isAdmin, NetSendInterface callback) {

        //Flow for Cognito sign-in
        AuthenticationHandler handler = new AuthenticationHandler() {
            @Override
            public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
                //Update keys
                Client.net.session = userSession;
                dialog.setMessage("Establishing secure connection...");
                try {
                    //Prepare the client's blank ciphers
                    rsaDecrypter = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                    aesEncrypter = Cipher.getInstance("AES/CBC/PKCS5Padding");
                    aesDecrypter = Cipher.getInstance("AES/CBC/PKCS5Padding");
                } catch (Exception e) {
                    dialog.dismiss();
                    System.out.println("Unable to initialize cipher instances!");
                    callback.onResult("ENCRYPTION_FAILURE");
                }

                //Create an RSA keypair and initialize rsaDecrypter with it
                try {
                    KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
                    keygen.initialize(2048);
                    clientRSAKeypair = keygen.generateKeyPair();
                    rsaDecrypter.init(Cipher.DECRYPT_MODE, clientRSAKeypair.getPrivate());
                } catch (Exception e) {
                    dialog.dismiss();
                    System.out.println("Unable to initialize client RSA key!");
                    callback.onResult("ENCRYPTION_FAILURE");
                }

                //Get details so we can update them
                pool.getCurrentUser().getDetailsInBackground(new GetDetailsHandler() {
                    @Override
                    public void onSuccess(CognitoUserDetails cognitoUserDetails) {
                        //If admin, set current name.  (NOT email)
                        Client.currentUserName = cognitoUserDetails.getAttributes().getAttributes().get("name");

                        //Make new attributes so we can update rsaPublicKey
                        CognitoUserAttributes attribs = new CognitoUserAttributes();
                        String keyString = Util.asHex(clientRSAKeypair.getPublic().getEncoded());
                        attribs.addAttribute("custom:rsaPublicKey", keyString);

                        //Cognito flow for updating attributes
                        pool.getCurrentUser().updateAttributesInBackground(attribs, new UpdateAttributesHandler() {
                            @Override
                            public void onSuccess(List<CognitoUserCodeDeliveryDetails> attributesVerificationList) {
                                //Now the user is logged in and RSA public key is updated in user attributes
                                //So now the server will look up the RSA key and encrypt a new AES key.
                                recieveAESKey(ctx, isAdmin, callback);
                            }

                            @Override
                            public void onFailure(Exception exception) {
                                dialog.dismiss();
                                System.out.println("FAILED TO UPDATE RSA PUBLIC KEY");
                                System.out.println(exception.getMessage());
                                callback.onResult("ENCRYPTION_FAILURE");
                            }
                        });
                    }

                    @Override
                    public void onFailure(Exception exception) {
                        dialog.dismiss();
                        System.out.println(exception.getMessage());
                        callback.onResult("ENCRYPTION_FAILURE");
                    }
                });

            }

            //Cognito always looks here to grab username and password
            @Override
            public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
                authenticationContinuation.setAuthenticationDetails(new AuthenticationDetails(username, password, null));
                authenticationContinuation.continueTask();
            }

            //Multi-Factor Authentication which we aren't using
            @Override
            public void getMFACode(MultiFactorAuthenticationContinuation continuation) {
                continuation.continueTask();
            }

            //Cognito goes here if user requires a new password to be set.
            //If user is an administrator, prompt to update details.
            //Otherwise generate a random password for the student and store it in userfile
            @Override
            public void authenticationChallenge(ChallengeContinuation continuation) {
                //if student is logging in for the first time
                if (!isAdmin) {
                    String rPassword = generateRandomPassword();
                    JsonObject jay = new JsonObject();
                    jay.addProperty("username", username);
                    jay.addProperty("password", rPassword);

                    //Stores login details to userfile.  If these details are modified they will NOT be able to login.
                    Util.writeToUserfile(ctx, jay);

                    //Update to newly generated password
                    continuation.setChallengeResponse("NEW_PASSWORD", rPassword);
                    continuation.setChallengeResponse("USERNAME", username);
                    continuation.continueTask();
                }
                else{
                    //Take the new admin to dashboard upon login
                    Client.startOnDashboard = true;
                    View v = ((AppCompatActivity) ctx).getLayoutInflater().inflate(R.layout.dialog_admin_completesignup, null);

                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    builder.setView(v);
                    AlertDialog dialog = builder.create();

                    //After Update Details dialog is closed, update attributes and continue Cognito login
                    v.findViewById(R.id.completesignup_button_confirm).setOnClickListener(x -> {
                        continuation.setChallengeResponse("NEW_PASSWORD", ((EditText) v.findViewById(R.id.completesignup_input_password)).getText().toString());
                        continuation.setChallengeResponse("USERNAME", username);
                        continuation.setChallengeResponse(CognitoServiceConstants.CHLG_PARAM_USER_ATTRIBUTE_PREFIX + "name", ((EditText) v.findViewById(R.id.completesignup_input_name)).getText().toString());
                        dialog.dismiss();
                        continuation.continueTask();
                    });

                    dialog.show();
                }
            }

            @Override
            public void onFailure(Exception exception) {
                dialog.dismiss();
                System.out.println(exception.getMessage());
                callback.onResult("INVALID_CREDENTIALS");
            }
        };
        //For some reason I have to put this here too
        if (pool.getCurrentUser() != null) pool.getCurrentUser().signOut();
        dialog = new WaitDialog(ctx);
        dialog.show();
        dialog.setMessage("Validating credentials...");

        //Log in the user
        pool.getUser(username).getSessionInBackground(handler);
    }

    //Generates a 16-character password from charSet
    public String generateRandomPassword() {
        char[] charSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQURSTUVWXYZ1234567890!@#$%^&*()[]{}/".toCharArray();
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < 16; i++)
            s.append(charSet[new Random().nextInt(charSet.length)]);
        return s.toString();
    }

    //We need to recieve an AES key from the server in order to encrypt our requests.
    //When this is called, an RSA key will have been made and updated to Cognito user attributes.
    //The server will use the username on the verified JWT that was given upon Cognito sign-in.
    //Using JWT claims, server will look up the RSA key in the user's attributes
    public void recieveAESKey(Context ctx, NetSendInterface callback) {
        //Nothing needs to be sent since all used info is included in JWT, automatically added in netSend
        netSend(ctx, "/keys/issue", "", r -> {
            try {
                //AES keys come in two parts, the key itself, and initialization vectors
                JsonObject jay = jp.parse(r).getAsJsonObject();
                String encodedKey = jay.get("key").getAsString();
                String encodedIv = jay.get("iv").getAsString();

                //Response is hex-encoded for URL safety, so decode to byte
                byte[] decodedKey = Util.fromHexString(encodedKey);
                byte[] decodedIv = Util.fromHexString(encodedIv);

                //Now use RSA cipher to decrypt the AES key
                byte[] decryptedKey = rsaDecrypter.doFinal(decodedKey);
                byte[] decryptedIv = rsaDecrypter.doFinal(decodedIv);

                //Use data to make a new SecretKeySpec
                IvParameterSpec iv = new IvParameterSpec(decryptedIv);
                SecretKeySpec spec = new SecretKeySpec(decryptedKey, "AES");

                //Initialize AES ciphers.  Will be used for all further secure communication.
                aesEncrypter.init(Cipher.ENCRYPT_MODE, spec, iv);
                aesDecrypter.init(Cipher.DECRYPT_MODE, spec, iv);

                //Lastly get the user's group and alerts.
                getUserGroup(ctx, callback);
            } catch (Exception e) {
                System.out.println("Unable to initialize AES ciphers!");
                e.printStackTrace();
                callback.onResult("ENCRYPTION_FAILURE");
            }
        });
    }

    //This authorized group lookup returns additional information such as alerts and admins.
    //Unauthorized lookup only grabs institution name / ID
    public void getUserGroup(Context ctx, NetSendInterface callback) {
        secureSend(ctx, "/group/lookup/auth", "", r -> {
            Client.userGroup = gson.fromJson(r, Group.class);
            dialog.dismiss();

            //Now the user is signed in with working keys, and their group alerts/details have been loaded.
            //Important to note that at this time reports are NOT pulled.
            callback.onResult("SECURE");
        });
    }

    //Request to pull reports from server and update reportMap.
    //Server decides which to grab from DB based on JWT information
    public void getGroupReports(Context ctx, NetSendInterface callback){
        secureSend(ctx, "/report/pull", "", r-> {
            Client.reportMap.clear();
            JsonArray jay = Client.net.jp.parse(r).getAsJsonArray();
            for (JsonElement reportElement : jay) {
                Report report = Client.net.gson.fromJson(reportElement.getAsString(), Report.class);
                Client.reportMap.put(report.getReportID(), report);
            }
            callback.onResult("OK");
        });
    }

}