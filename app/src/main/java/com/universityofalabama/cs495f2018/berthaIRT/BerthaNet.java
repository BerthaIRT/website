package com.universityofalabama.cs495f2018.berthaIRT;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.Serializable;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class BerthaNet {
    static final String ip = "http://54.236.113.200/";

    JsonParser jp;
    Gson gson;

    String clientKey;
    RequestQueue netQ;
    KeyPair clientRSAKeypair;
    Cipher rsaEncrypter;
    Cipher rsaDecrypter;
    Cipher aesEncrypter;
    Cipher aesDecrypter;
    Context ctx;

    public BerthaNet(Context c){
        ctx = c;
        jp = new JsonParser();
        gson = new Gson();
        netQ = Volley.newRequestQueue(c);

        try{
            rsaEncrypter = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            rsaDecrypter = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            aesEncrypter = Cipher.getInstance("AES/CBC/PKCS5Padding");
            aesDecrypter = Cipher.getInstance("AES/CBC/PKCS5Padding");
        }
        catch (Exception e){
            System.out.println( "Unable to initialize cipher instances!");
        }

        //Creates an RSA keypair and attaches it to client instance.
        try{
            KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
            keygen.initialize(2048);
            clientRSAKeypair = keygen.generateKeyPair();
            rsaEncrypter.init(Cipher.ENCRYPT_MODE, clientRSAKeypair.getPublic());
            rsaDecrypter.init(Cipher.DECRYPT_MODE, clientRSAKeypair.getPrivate());
        }
        catch (Exception e){
            System.out.println( "Unable to initialize client RSA key!");
        }
        sendRSAPublicKey();
    }

    public interface NetSendInterface {
        void onResult(String response);
    }

    //Encodes RSA keypair to hex string and sends to server
    //Unencrypted so far so we will use netSend
    public void sendRSAPublicKey(){
        String hexEncodedKey = Util.asHex(clientRSAKeypair.getPublic().getEncoded());

        Map<String, String> q = new HashMap<String, String>(){
            {
                put("publicKey", hexEncodedKey);
            }
        };

        netSend("keyexchange/rsa", q, (r) -> {receiveClientKey(r);});
    }

    //Receives our unique client key
    //We do not need to get the public RSA key of the server since we are not sending in that direction
    //Unencrypted so far so we will use netSend
    public void receiveClientKey(String serverResponse){
        clientKey = serverResponse;
        //Now that we have the public key of the server and our client key,
        //we need to request our AES key using our client key

        Map<String, String> q = new HashMap<String, String>(){
            {
                put("clientKey", clientKey);
            }
        };
        netSend("keyexchange/aes", q, (r) -> {recieveAESKey(r);});
    }

    public void recieveAESKey(String serverResponse){
        //the AES key is sent as a json object with two parts
        //both the key and initialization vectors are encrypted with RSA
        //so after we securely get the AES key we have no need for RSA
        try {
            JsonObject r = jp.parse(serverResponse).getAsJsonObject();
            String hexKey = r.get("key").getAsString();
            String hexIv = r.get("iv").getAsString();
            byte[] decodedKey = Util.fromHexString(hexKey);
            byte[] decryptedKey = rsaDecrypter.doFinal(decodedKey);
            byte[] decodedIv = Util.fromHexString(hexIv);
            byte[] decryptedIv = rsaDecrypter.doFinal(decodedIv);

            IvParameterSpec iv = new IvParameterSpec(decryptedIv);
            SecretKeySpec spec = new SecretKeySpec(decryptedKey, "AES");

            aesEncrypter.init(Cipher.ENCRYPT_MODE, spec, iv);
            aesDecrypter.init(Cipher.DECRYPT_MODE, spec, iv);
        }
        catch (Exception e){
            System.out.println( "Unable to initialize AES ciphers!");
        }
        doEncryptionTest();
    }

    public void doEncryptionTest(){
        String testString = "bertha";
        secureSend("keyexchange/test", testString, (r) -> {
            if(r.equals("secure")){
                System.out.println("Security established.");
                Toast.makeText(ctx, "Secure connection established.", Toast.LENGTH_LONG).show();
                ctx.startActivity(new Intent(ctx, NewUserActivity.class));
            }
        });
    }

    public void checkIfLoggedIn() {
        JsonObject jay = Util.readFromUserfile(ctx);
        if(jay != null){
            Client.currentUser = jay.get("username").getAsString();

            Client.net.secureSend("signin", jay.toString(), (r) -> {
                System.out.println(r);
                if (r.equals("NEW_PASSWORD_REQUIRED"))
                    ctx.startActivity(new Intent(ctx, StudentMainActivity.class));
            });
        }
        else
            ctx.startActivity(new Intent(ctx, NewUserActivity.class));
    }

    public void netSend(String path, final Map<String, String> params, final NetSendInterface callback){
        StringRequest req = new StringRequest(Request.Method.PUT, ip.concat(path), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onResult(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ctx, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getParams(){
                Map<String, String> m = params;
                if(m == null)
                    m = new HashMap<>();
                return m;
            }
        };
        netQ.add(req);
    }

    public void secureSend(String path, final Serializable params, final NetSendInterface callback) {
        NetSendInterface wrapper = new NetSendInterface() {
            @Override
            public void onResult(String response) {
                //Result will be hex encoded and AES encrypted
                try {
                    byte[] encrypted = Util.fromHexString(response);
                    String decrypted = new String(aesDecrypter.doFinal(encrypted));
                    //Do the original callback
                    callback.onResult(decrypted);
                } catch (Exception e) {
                    System.out.println( "Unable to decrypt server response!");
                    e.printStackTrace();
                }
            }
        };
        //Encrypt the data
        byte[] encrypted = new byte[0];
        try {
            encrypted = aesEncrypter.doFinal(params.toString().getBytes());
        } catch (Exception e) {
            System.out.println( "Unable to encrypt data packet!");
            e.printStackTrace();
        }
        String encoded = Util.asHex(encrypted);

        //Attach it to clientKey
        Map<String, String> q = new HashMap<String, String>() {
            {
                put("clientKey", clientKey);
                put("data", encoded);
            }
        };
        //Send as normal, with the wrapper as callback
        netSend(path, q, wrapper);
    }
}
