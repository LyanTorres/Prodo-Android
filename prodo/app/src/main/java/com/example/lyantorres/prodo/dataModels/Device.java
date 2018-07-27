package com.example.lyantorres.prodo.dataModels;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Device implements Serializable{
    private String _id;
    private String mName;
    private ArrayList<Content> mContent;
    private Content mCurrentContent;

    public Device (JSONObject _deviceJSON){

        try {
            _id = _deviceJSON.getString("_id");
            mName = _deviceJSON.getString("name");
            JSONObject jsonObj = _deviceJSON.getJSONObject("currentContent");
            mCurrentContent = new Content(jsonObj);

            JSONArray contentJSON = _deviceJSON.getJSONArray("content");

            for (int i = 0; i < contentJSON.length(); i ++){
                JSONObject obj = contentJSON.getJSONObject(i);
                mContent.add(new Content(obj));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String _name) {
        this.mName = _name;
    }

    public ArrayList<Content> getmContent() {
        return mContent;
    }

    public Content getmCurrentContent() {
        return mCurrentContent;
    }
}
