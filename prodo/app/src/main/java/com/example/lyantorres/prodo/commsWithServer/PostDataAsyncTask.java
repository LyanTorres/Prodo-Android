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
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class PostDataAsyncTask extends AsyncTask<String, String, String>  {

    private HttpsURLConnection mUrlConnection;
    private static Context mContext;
    private static PostDataAsyncTaskInterface mInterface;
    private static User mUser;

    public static PostDataAsyncTask newInstance(Context _context, User _user) {

        mContext = _context;
        mUser = _user;
        if(_context instanceof PostDataAsyncTaskInterface){
            mInterface = (PostDataAsyncTaskInterface) _context;
        }

        Bundle args = new Bundle();
        PostDataAsyncTask fragment = new PostDataAsyncTask();
        return fragment;
    }

    public interface PostDataAsyncTaskInterface{
        void dataPosted(String _results);
        void dataWasNotPosted();
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
        mUrlConnection.setRequestMethod("POST");
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
    protected void onPostExecute(String _results) {
        super.onPostExecute(_results);

        if(_results == null){
            if(mInterface != null){
                mInterface.dataWasNotPosted();
            }
        } else {
            if (mInterface != null) {
                mInterface.dataPosted(_results);
            }
        }
    }
}
