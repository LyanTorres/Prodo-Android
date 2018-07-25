package com.example.lyantorres.prodo.commsWithServer;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.lyantorres.prodo.dataModels.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class GetDataAsyncTask extends AsyncTask<String, String, String> {

    private HttpURLConnection mUrlConnection;
    private static Context mContext;
    private static GetAsyncTaskInterface mInterface;
    private static User mUser;

    public interface GetAsyncTaskInterface {
        void dataWasFetched(String _results);
        void dataWasNotFetched();
    }

    public static GetDataAsyncTask newInstance(Context _context, User _user) {

        mContext = _context;
        mUser = _user;
        if(_context instanceof GetAsyncTaskInterface){
            mInterface = (GetAsyncTaskInterface) _context;
        }
        Bundle args = new Bundle();
        GetDataAsyncTask fragment = new GetDataAsyncTask();
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
            mUrlConnection.setRequestProperty("X-Auth", mUser.getmToken());
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
            Log.i("===== PRODO =====", "========== \n doInBackground: results: " + result +" \n ==========");
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
                mInterface.dataWasNotFetched();
            }
        } else {
            if (mInterface != null) {
                mInterface.dataWasFetched(_results);
            }
        }
    }
}
