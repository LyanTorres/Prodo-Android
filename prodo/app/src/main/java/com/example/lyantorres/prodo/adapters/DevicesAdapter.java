package com.example.lyantorres.prodo.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.lyantorres.prodo.dataModels.Device;

import java.util.ArrayList;

public class DevicesAdapter extends BaseAdapter {

    private static Context mContext;
    private static ArrayList<Device> mDevices;

    public DevicesAdapter(Context _context, ArrayList<Device> _devices){
        mContext = _context;
        mDevices = _devices;
    }

    @Override
    public int getCount() {
        return mDevices.size();
    }

    @Override
    public Object getItem(int position) {
        return mDevices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO: setup so that it populates grid with data lol
        return null;
    }
}
