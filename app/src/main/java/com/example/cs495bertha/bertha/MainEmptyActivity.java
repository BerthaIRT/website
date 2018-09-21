package com.example.cs495bertha.bertha;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

public class MainEmptyActivity extends AppCompatActivity {

    private static final String FILE_NAME = "UUID.txt";
    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_empty);

        Intent activityIntent;

        //TODO you can comment deleteUUID() in/out to make a fresh run without a UUID
        deleteUUID();
        //TODO you can comment deleteUUID() in/out to make a fresh run without a UUID

        //See if this is the first time a user is logging in
        //by checking for UUID existing
        if (existUUID())
            activityIntent = new Intent(this, ReportMainActivity.class);
        else {
            //writes a unique ID to a text file in internal storage
            saveUUID(UUID.randomUUID().toString());
            Toast.makeText(this, "UUID is: " + readUUID(), Toast.LENGTH_LONG).show();
            activityIntent = new Intent(this, MainActivity.class);
        }
        startActivity(activityIntent);
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
            //write the UUID to the file
            //TODO Remove before release
            Toast.makeText(this, "Saved " + uuid + " to " + getFilesDir() + "/" + FILE_NAME, Toast.LENGTH_LONG).show();
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