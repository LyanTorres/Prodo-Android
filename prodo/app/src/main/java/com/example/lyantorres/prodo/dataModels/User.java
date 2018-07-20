package com.example.lyantorres.prodo.dataModels;

import java.util.ArrayList;

public class User {

    private String _id;
    private String mEmail;
    private String mToken;
    private ArrayList<String> mStores;

    public User(String _id, String _email, String _token, ArrayList<String> _stores){
        this._id = _id;
        mEmail = _email;
        mToken = _token;
        mStores = _stores;
    }
}
