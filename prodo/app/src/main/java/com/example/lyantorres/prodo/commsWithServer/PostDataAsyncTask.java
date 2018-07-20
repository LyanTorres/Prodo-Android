package com.example.lyantorres.prodo.commsWithServer;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

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

    public static FetchDataAsyncTask newInstance(Context _context) {

        mContext = _context;

        if(_context instanceof PostDataAsyncTaskInterface){
            mInterface = (PostDataAsyncTaskInterface) _context;
        }
        Bundle args = new Bundle();
        FetchDataAsyncTask fragment = new FetchDataAsyncTask();
        return fragment;
    }

    public interface PostDataAsyncTaskInterface{
        void dataPosted(String _results);
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
        if(mInterface != null){
            mInterface.dataPosted(_results);
        }
    }
}
