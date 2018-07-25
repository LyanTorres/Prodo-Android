package com.example.lyantorres.prodo.dataModels;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable{

    private String _id;
    private String mEmail;
    private String mToken;
    private ArrayList<String> mStores = new ArrayList<>();
    private String mPrefUserKey = "currentUser";

    public User(){

    }

    public void updateUser(String _json, Activity _activity){

        try {
            JSONObject jsonObj = new JSONObject(_json);
            Log.i("===== PRODO =====", " ========== \n updateUser: USER: "+jsonObj+" \n ==========");

            _id = jsonObj.getString("_id");
            mEmail = jsonObj.getString("email");
            mToken = jsonObj.getString("token");

            JSONArray stores = jsonObj.getJSONArray("stores");
            for(int i = 0; i < stores.length(); i ++){
                mStores.add(stores.getString(i));
            }

            this.saveCurrentUser(_activity);
            Log.i("===== PRODO =====", " ========== \n updateUser: USER: "+this.getmToken()+" \n ==========");

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("===== PRODO =====", " ========== \n updateUser: SOMETHING WENT WRONG WHEN PARSING USER JOSN \n ==========");
        }

    }

    public void saveCurrentUser(Activity _activity){
        SharedPreferences preferences = _activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(this);
        prefsEditor.putString(mPrefUserKey, json);
        prefsEditor.commit();
    }

    public User getCurrentUser(Activity _activity){
        SharedPreferences preferences = _activity.getPreferences(Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString(mPrefUserKey, "");
        User obj = gson.fromJson(json, User.class);

        if(obj == null) {
            return null;
        }

        this.mEmail = obj.mEmail;
        this.mToken = obj.mToken;
        this.mStores = obj.mStores;
        this._id = obj._id;

        return this;
    }

    public String get_id() {
        return _id;
    }

    public String getmEmail() {
        return mEmail;
    }

    public String getmToken() {
        return mToken;
    }

    public ArrayList<String> getmStores() {
        return mStores;
    }

    public String getmPrefUserKey() {
        return mPrefUserKey;
    }
}
