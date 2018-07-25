package com.example.lyantorres.prodo.dataModels;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Store {

    private String _id;
    private String mName;
    private String mStoreId;
    private ArrayList<String> mDevices = new ArrayList<>();

    public Store(JSONObject _storeJSONobj){

        try {

            mStoreId = _storeJSONobj.getString("_id");
            mName = _storeJSONobj.getString("name");
            mStoreId = _storeJSONobj.getString("storeId");

            JSONArray devicesJson = _storeJSONobj.getJSONArray("devices");

            for (int i = 0; i < devicesJson.length(); i ++) {
                //mDevices.add( new Device(devicesJson.get(i)));
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

    public String getmStoreId() {
        return mStoreId;
    }

    public void setmStoreId(String _storeId) {
        this.mStoreId = _storeId;
    }

    public ArrayList<String> getmDevices() {
        return mDevices;
    }

    public void setmDevices(ArrayList<String> _devices) {
        this.mDevices = _devices;
    }
}
