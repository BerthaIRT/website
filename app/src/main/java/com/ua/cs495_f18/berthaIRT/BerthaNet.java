package com.ua.cs495_f18.berthaIRT;

import android.content.Context;
import android.util.Base64;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class BerthaNet {
    public static BerthaNet net;

    static final String ip = "http://52.91.110.19/";

    static RequestQueue netQ;

    static String clientID;
    static KeyPair clientRSAKeys;
    static RSAPublicKey serverRSAKey;
    static Cipher rsaEncoder;
    static Cipher rsaDecoder;

    static Cipher aesEncoder;
    static Cipher aesDecoder;

    static Toast errorToast;

    public static void init(Context c){
        netQ = Volley.newRequestQueue(c);
        errorToast = Toast.makeText(c, "", Toast.LENGTH_LONG);
        sendRSAKeys();
    }

    public static String asHex(byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        int i;

        for (i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10) {
                strbuf.append("0");
            }

            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }

        return strbuf.toString();
    }

    public static byte[] fromHexString(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public interface netQueryInterface{
        void onResult(String response);
    }

    public static void netQuery(String path, final netQueryInterface callback, final Map<String, String> params){
        System.out.println("\nSending query to server:\n" + params);
        StringRequest req = new StringRequest(Request.Method.PUT, ip.concat(path), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("\nReceived response from server:\n" + response);
                callback.onResult(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorToast.setText(error.getMessage());
                errorToast.show();
            }
        }){
            @Override
            public Map<String, String> getParams(){
                Map<String, String> m = params;
                if(m == null)
                    m = new HashMap<String, String>();
                if(clientID != null)
                    m.put("clientid", clientID);
                return m;
            }
        };
        netQ.add(req);
    }

    public static void sendRSAKeys(){
        //create RSA key and send to server
        try {
            KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
            keygen.initialize(2048);
            clientRSAKeys = keygen.generateKeyPair();
            rsaEncoder = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            rsaEncoder.init(Cipher.ENCRYPT_MODE, clientRSAKeys.getPublic());
            rsaDecoder = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            rsaDecoder.init(Cipher.DECRYPT_MODE, clientRSAKeys.getPrivate());
        }
        catch (Exception e){e.printStackTrace();}

        Map<String, String> q = new HashMap<String, String>(){
            {
                put("key", asHex(clientRSAKeys.getPublic().getEncoded()));
            }
        };
        netQuery("keyexchange/rsa", (r) -> {
            receiveRSAKeys(r);}, q);
    }

    private static void receiveRSAKeys(String publicKeyFromServer) {
        try {
            JsonParser jp = new JsonParser();
            JsonObject response = jp.parse(publicKeyFromServer).getAsJsonObject();
            clientID = response.get("clientid").getAsString();
            String hexStringKey = response.get("key").getAsString();
            byte[] byteKey = fromHexString(hexStringKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            serverRSAKey = (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(byteKey));

            netQuery("keyexchange/aes", (r) -> {
                recieveAESKey(r);}, null);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private static void recieveAESKey(String rsaEncodedAESFromServer){
        try{
            JsonParser jp = new JsonParser();
            JsonObject response = jp.parse(rsaEncodedAESFromServer).getAsJsonObject();
            String hexStringKey = response.get("key").getAsString();
            String hexIv = response.get("ivparams").getAsString();
            byte[] decodedKey = fromHexString(hexStringKey);
            byte[] decryptedKey = rsaDecoder.doFinal(decodedKey);
            byte[] decodedIv = fromHexString(hexIv);
            byte[] decryptedIv = rsaDecoder.doFinal(decodedIv);
            IvParameterSpec iv = new IvParameterSpec(decryptedIv);
            SecretKeySpec spec = new SecretKeySpec(decryptedKey, "AES");

            aesEncoder = Cipher.getInstance("AES/CBC/PKCS5Padding");
            aesDecoder = Cipher.getInstance("AES/CBC/PKCS5Padding");
            aesEncoder.init(Cipher.ENCRYPT_MODE, spec, iv);
            aesDecoder.init(Cipher.DECRYPT_MODE, spec, iv);
        } catch (Exception e) { e.printStackTrace(); }

        netQuery("keyexchange/test", (r) -> {
            decryptAES(r);}, null);
    }

    private static void decryptAES(String r){
        try{
            byte[] encrypted = fromHexString(r);
            String decrypted = new String(aesDecoder.doFinal(encrypted));
            System.out.println(decrypted);
        } catch (Exception e) { e.printStackTrace(); }
    }
}
