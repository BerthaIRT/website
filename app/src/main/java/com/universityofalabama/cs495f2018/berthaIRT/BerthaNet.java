package com.universityofalabama.cs495f2018.berthaIRT;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
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
    static final String ip = "http://10.0.0.185:6969";

    public JsonParser jp;
    public Gson gson;

    String clientKey;
    RequestQueue netQ;
    KeyPair clientRSAKeypair;
    Cipher rsaDecrypter;
    Cipher aesEncrypter;
    Cipher aesDecrypter;

    public CognitoUserPool pool;
    CognitoUserSession session = null;
    
    static WaitDialog dialog;

    public BerthaNet(Context c) {
        jp = new JsonParser();
        gson = new Gson();
        netQ = Volley.newRequestQueue(c);

        AWSMobileClient.getInstance().initialize(c).execute();
        AWSConfiguration awsConfiguration = new AWSConfiguration(c);
        if (IdentityManager.getDefaultIdentityManager() == null) {
            final IdentityManager identityManager = new IdentityManager(c, awsConfiguration);
            identityManager.signOut();
            IdentityManager.setDefaultIdentityManager(identityManager);
        }
        pool = new CognitoUserPool(c, awsConfiguration);
        if (pool.getCurrentUser() != null) pool.getCurrentUser().signOut();
    }

    public interface NetSendInterface {
        void onResult(String response);
    }

    public void netSend(Context ctx, String path, final String body, final NetSendInterface callback) {
        StringRequest req = new StringRequest(Request.Method.PUT, ip.concat(path), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onResult(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ctx, error.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println(error.getMessage());
            }
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

    //performs login.  after this is called JWTs will be attached to Authentication header in HTTP request
    //after this is performed AES encryption is used
    public void performLogin(Context ctx, String username, String password, boolean isAdmin, NetSendInterface callback) {
        AuthenticationHandler handler = new AuthenticationHandler() {
            @Override
            public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
                Client.net.session = userSession;
                dialog.setMessage("Establishing secure connection...");
                try {
                    rsaDecrypter = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                    aesEncrypter = Cipher.getInstance("AES/CBC/PKCS5Padding");
                    aesDecrypter = Cipher.getInstance("AES/CBC/PKCS5Padding");
                } catch (Exception e) {
                    dialog.dismiss();
                    System.out.println("Unable to initialize cipher instances!");
                    callback.onResult("ENCRYPTION_FAILURE");
                }

                //Creates an RSA keypair
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

                //Attaches RSA keypair to cognito attribute so it can be verified on our webservice
                System.out.println("GETTING DETAILS...");
                pool.getCurrentUser().getDetailsInBackground(new GetDetailsHandler() {
                    @Override
                    public void onSuccess(CognitoUserDetails cognitoUserDetails) {
                        Client.currentUserName = cognitoUserDetails.getAttributes().getAttributes().get("name");
                        Client.currentUserGroupID = cognitoUserDetails.getAttributes().getAttributes().get("custom:groupID");
                        CognitoUserAttributes attribs = new CognitoUserAttributes();
                        String keyString = Util.asHex(clientRSAKeypair.getPublic().getEncoded());
                        attribs.addAttribute("custom:rsaPublicKey", keyString);
                        pool.getCurrentUser().updateAttributesInBackground(attribs, new UpdateAttributesHandler() {
                            @Override
                            public void onSuccess(List<CognitoUserCodeDeliveryDetails> attributesVerificationList) {
                                //Now the user is logged in and RSA public key is updated in user attributes
                                //So now we need to use that to obtain an AES key from the webservice
                                recieveAESKey(ctx, callback);
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

            @Override
            public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
                authenticationContinuation.setAuthenticationDetails(new AuthenticationDetails(username, password, null));
                authenticationContinuation.continueTask();
            }

            @Override
            public void getMFACode(MultiFactorAuthenticationContinuation continuation) {
                continuation.continueTask();
            }

            @Override
            public void authenticationChallenge(ChallengeContinuation continuation) {
                //if student is logging in for the first time
                if (!isAdmin) {
                    String rPassword = generateRandomPassword();
                    JsonObject jay = new JsonObject();
                    jay.addProperty("username", username);
                    jay.addProperty("password", rPassword);
                    Util.writeToUserfile(ctx, jay);
                    continuation.setChallengeResponse("NEW_PASSWORD", rPassword);
                    continuation.setChallengeResponse("USERNAME", username);
                    continuation.continueTask();
                }
                Client.startOnDashboard = true;
                //new password required
                LayoutInflater flater = ((AppCompatActivity) ctx).getLayoutInflater();
                View v = flater.inflate(R.layout.dialog_admin_completesignup, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setView(v);
                AlertDialog dialog = builder.create();

                v.findViewById(R.id.completesignup_button_confirm).setOnClickListener(x -> {
                    continuation.setChallengeResponse("NEW_PASSWORD", ((EditText) v.findViewById(R.id.completesignup_input_password)).getText().toString());
                    continuation.setChallengeResponse("USERNAME", username);
                    continuation.setChallengeResponse(CognitoServiceConstants.CHLG_PARAM_USER_ATTRIBUTE_PREFIX + "name", ((EditText) v.findViewById(R.id.completesignup_input_name)).getText().toString());
                    dialog.dismiss();
                    continuation.continueTask();
                });

                dialog.show();
            }

            @Override
            public void onFailure(Exception exception) {
                dialog.dismiss();
                System.out.println(exception.getMessage());
                callback.onResult("INVALID_CREDENTIALS");
            }
        };
        if (pool.getCurrentUser() != null) pool.getCurrentUser().signOut();
        dialog = new WaitDialog(ctx);
        dialog.show();
        dialog.setMessage("Validating credentials...");
        pool.getUser(username).getSessionInBackground(handler);
    }

    public String generateRandomPassword() {
        char[] charSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQURSTUVWXYZ1234567890!@#$%^&*()[]{}/".toCharArray();
        String s = "";
        for (int i = 0; i < 16; i++)
            s = s + charSet[new Random().nextInt(charSet.length)];
        return s;
    }

    public void recieveAESKey(Context ctx, NetSendInterface callback) {
        //should only be called after JWTs are available since the public key attribute is needed to encrypt
        //the AES key is sent as a json object with two parts
        //both the key and initialization vectors are encrypted with RSA
        //so after we securely get the AES key we have no need for RSA
        netSend(ctx, "/keys/aes", "", r -> {
            System.out.println(r);
            try {
                JsonObject jay = jp.parse(r).getAsJsonObject();
                String encodedKey = jay.get("key").getAsString();
                String encodedIv = jay.get("iv").getAsString();
                byte[] decodedKey = Util.fromHexString(encodedKey);
                byte[] decodedIv = Util.fromHexString(encodedIv);
                byte[] decryptedKey = rsaDecrypter.doFinal(decodedKey);
                byte[] decryptedIv = rsaDecrypter.doFinal(decodedIv);

                IvParameterSpec iv = new IvParameterSpec(decryptedIv);
                SecretKeySpec spec = new SecretKeySpec(decryptedKey, "AES");

                aesEncrypter.init(Cipher.ENCRYPT_MODE, spec, iv);
                aesDecrypter.init(Cipher.DECRYPT_MODE, spec, iv);
                doEncryptionTest(ctx, callback);
            } catch (Exception e) {
                System.out.println("Unable to initialize AES ciphers!");
                e.printStackTrace();
                callback.onResult("ENCRYPTION_FAILURE");
            }
        });
    }

    public void doEncryptionTest(Context ctx, NetSendInterface callback) {
        String testString = "bertha";
        secureSend(ctx,"/keys/test", testString, r -> {
            if (r.equals("success")) {
                System.out.println("Security established.");
                callback.onResult("SECURE");
            }
        });
    }

    public void secureSend(Context ctx, String path, final Serializable params, final NetSendInterface callback) {
        NetSendInterface wrapper = new NetSendInterface() {
            @Override
            public void onResult(String response) {
                //Result will be base64 encoded and AES encrypted
                try {
                    byte[] encrypted = Util.fromHexString(response);
                    String decrypted = new String(aesDecrypter.doFinal(encrypted));
                    //Do the original callback
                    callback.onResult(decrypted);
                } catch (Exception e) {
                    System.out.println("Unable to decrypt server response!");
                    e.printStackTrace();
                    callback.onResult("ENCRYPTION_FAILURE");
                }
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
        String encoded = Util.asHex(encrypted);
        //Send as normal, with the wrapper as callback
        netSend(ctx, path, encoded, wrapper);
    }
}


