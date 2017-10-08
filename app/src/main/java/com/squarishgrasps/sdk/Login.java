package com.squarishgrasps.sdk;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by nzpetter on 08.10.2017.
 */

public class Login extends AsyncTask<String, String, Boolean> {

    private final Activity activity;
    private final String URL = "https://squarish-grasps.000webhostapp.com/";
    private String response;
    private String username;
    private String password;


    public Login(Activity activity, String username, String password) {
        this.username = username;
        this.password = password;
        this.activity = activity;
        this.response = "";
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (aBoolean) {
            Toast.makeText(activity, this.response, Toast.LENGTH_LONG).show();
            this.activity.finish();
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            JSONObject credentials = new JSONObject();
            credentials.put("username", this.username);
            credentials.put("password", this.password);

            URL url = new URL(this.URL);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("POST");

            conn.setDoOutput(true);
            conn.setDoInput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            writer.write(credentials.toString());
            writer.flush();
            writer.close();

            conn.connect();

            int connectionStatus = conn.getResponseCode();

            if (connectionStatus == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while ((line = br.readLine()) != null) {
                    this.response += line;
                }

                Log.d("response", this.response);

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
