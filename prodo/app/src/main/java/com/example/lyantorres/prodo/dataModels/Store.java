package com.example.lyantorres.prodo.dataModels;

import java.util.ArrayList;

public class Store {

    private String _id;
    private String mName;
    private String mStoreId;
    private ArrayList<String> mDevices;

    public Store(String _id, String _name, String _storeId, ArrayList<String> _devices){
        this._id = _id;
        mName = _name;
        mStoreId= _storeId;
        mDevices = _devices;
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
