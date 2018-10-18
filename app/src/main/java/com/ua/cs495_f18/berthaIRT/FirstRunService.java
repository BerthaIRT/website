package com.ua.cs495_f18.berthaIRT;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

public class FirstRunService extends AppCompatActivity {

    //TODO: should extend Application...maybe

    private static final String FILE_NAME = "UUID.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO you can comment deleteUUID() in/out to make a fresh run without a UUID
        deleteUUID();
        //TODO you can comment deleteUUID() in/out to make a fresh run without a UUID

        launchFirstActivity();
    }

    private void launchFirstActivity() {
        //if there is a UUID
        if(existUUID()) {
            startActivity(new Intent(this, UnregisteredPortalActivity.class));
            finish();
        }
/*        else if (preferenceFileExist("Login")) {
            getLogin();
        }*/
        else{
            saveUUID(UUID.randomUUID().toString());
            startActivity(new Intent(this,UnregisteredPortalActivity.class));
            finish();
        }
    }

    public boolean preferenceFileExist(String fileName) {
        File f = new File(getApplicationContext().getApplicationInfo().dataDir + "/shared_prefs/" + fileName + ".xml");
        return f.exists();
    }

    private void getLogin() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("Login", MODE_PRIVATE);
        String username = sharedPreferences.getString("Unm",null);
        String password = sharedPreferences.getString("Psw",null);
        checkLogin(username,password);
    }

    private void checkLogin(String username, String password) {
        //if the login is null
        if(username == null && password == null) {
            saveUUID(UUID.randomUUID().toString());
            startActivity(new Intent(this,UnregisteredPortalActivity.class));
            finish();
        }

        //TODO not make this always true
        else {
            startActivity(new Intent(this, AdminPortalActivity.class));
            finish();
        }
    }

    private boolean existUUID() {
        FileInputStream fis = null;
        String str = null;
        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String uuid;
            while ((uuid = br.readLine()) != null) {
                sb.append(uuid).append("\n");
            }
            str = sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (str == null)
            return false;
        else
            return true;
    }

    public void saveUUID (String uuid) {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(uuid.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String readUUID () {
        FileInputStream fis = null;
        String str = null;
        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String uuid;
            while ((uuid = br.readLine()) != null) {
                sb.append(uuid).append("\n");
            }
            str = sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return str;
    }

    private void deleteUUID () {
        File dir = getFilesDir();
        File file = new File(dir, FILE_NAME);
        file.delete();
    }
}