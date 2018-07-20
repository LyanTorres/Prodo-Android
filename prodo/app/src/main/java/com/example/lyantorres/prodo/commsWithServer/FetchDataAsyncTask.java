package com.example.lyantorres.prodo.commsWithServer;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchDataAsyncTask extends AsyncTask<String, String, String> {

    private HttpURLConnection mUrlConnection;
    private static Context mContext;
    private static AsyncTaskInterface mInterface;

    public interface AsyncTaskInterface {
        void dataWasFetched(String _results);
    }

    public static FetchDataAsyncTask newInstance(Context _context) {

        mContext = _context;

        if(_context instanceof AsyncTaskInterface){
            mInterface = (AsyncTaskInterface) _context;
        }
        Bundle args = new Bundle();
        FetchDataAsyncTask fragment = new FetchDataAsyncTask();
        return fragment;
    }

    @Override
    protected String doInBackground(String... strings) {
        String urlRoot = "https://polar-bayou-64862.herokuapp.com";
        URL url = null;
        StringBuilder result = new StringBuilder();

        try {
            url = new URL(urlRoot+strings[0]);

            mUrlConnection = (HttpURLConnection) url.openConnection();
            mUrlConnection.setRequestMethod("GET");
            mUrlConnection.setRequestProperty("Content-Type", "application/json");

            if (mUrlConnection.getResponseCode() != 200) {
                throw new RuntimeException(" ===== Failed : HTTP error code : "
                        + mUrlConnection.getResponseCode() + " =====");
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
            mInterface.dataWasFetched(_results);
        }
    }
}
