package com.example.lyantorres.prodo.dataModels;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;

public class User {

    private String _id;
    private String mEmail;
    private String mToken;
    private ArrayList<String> mStores;
    private String mPrefUserKey = "currentUser";

    public User(){

    }

    public void updateUser(String _id, String _email, String _token, ArrayList<String> _stores){
        this._id = _id;
        mEmail = _email;
        mToken = _token;
        mStores = _stores;
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
        return obj;
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
}
