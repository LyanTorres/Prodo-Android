package com.example.lyantorres.prodo.commsWithServer;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.lyantorres.prodo.dataModels.User;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class DeleteDataAsyncTask extends AsyncTask<String, String, String> {


    private HttpsURLConnection mUrlConnection;
    private static Context mContext;
    private static DeleteAsyncTaskInterface mInterface;
    private static User mUser;

    public interface DeleteAsyncTaskInterface{
        void dataWasDeleted();
    }

    public static DeleteDataAsyncTask newInstance(Context _context, User _user) {

        mContext = _context;
        mUser = _user;

        if(mContext instanceof DeleteAsyncTaskInterface){
            mInterface = (DeleteAsyncTaskInterface) mContext;
        }

        Bundle args = new Bundle();
        DeleteDataAsyncTask fragment = new DeleteDataAsyncTask();
        return fragment;
    }

    @Override
    protected String doInBackground(String... strings) {
        String urlRoot = "https://polar-bayou-64862.herokuapp.com";
        URL url = null;
        StringBuilder result = new StringBuilder();

        try {
            url = new URL(urlRoot+strings[0]);

            mUrlConnection = (HttpsURLConnection) url.openConnection();
            mUrlConnection.setDoOutput(true);
            mUrlConnection.setRequestMethod("DELETE");
            mUrlConnection.setRequestProperty("Content-Type", "application/json");

            if(mUser != null){
                mUrlConnection.setRequestProperty("X-Auth", mUser.getmToken());
            }

            String body = strings[1];

            OutputStream os = mUrlConnection.getOutputStream();
            os.write(body.getBytes());
            os.flush();

            if (mUrlConnection.getResponseCode() != 200) {
                Log.i("===== PRODO =====", " ========== \n doInBackground: ===== Failed : HTTP error code : "
                        + mUrlConnection.getResponseCode() + " \n ==========");
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    (mUrlConnection.getInputStream())));

            String dataString;

            while((dataString = reader.readLine()) != null){
                result.append(dataString);
            }

            if(3 == strings.length){
                String token = mUrlConnection.getHeaderField("X-Auth");
                JSONObject jsonResults = new JSONObject(result.toString());

                jsonResults.put("token", token);

                result = new StringBuilder(jsonResults.toString());
                Log.i("===== PRODO =====", "========== \n doInBackground: results: " + result +" \n ==========");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        } finally {
            mUrlConnection.disconnect();
            Log.i("===== PRODO =====", "========== \n doInBackground: results: " + result +" \n ==========");
        }

        return result.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(mInterface != null){
            mInterface.dataWasDeleted();
        }
    }
}
