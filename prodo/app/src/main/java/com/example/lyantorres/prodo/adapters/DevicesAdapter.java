package com.example.lyantorres.prodo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lyantorres.prodo.R;
import com.example.lyantorres.prodo.dataModels.Device;
import com.example.lyantorres.prodo.dataModels.Store;
import com.squareup.picasso.Picasso;

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
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.device_grid_item, parent, false);
        }

        Device device = mDevices.get(position);

        ImageView preview = convertView.findViewById(R.id.devicePreview_DC_IV);
        TextView name = convertView.findViewById(R.id.deviceName_TV);

        name.setText(device.getmName());

        if(device.getmCurrentContent() != null) {
            Picasso.with(mContext).load(device.getmCurrentContent().getmThumbnailLink()).into(preview);
        }

        return convertView;
    }
}
