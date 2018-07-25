package com.example.lyantorres.prodo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lyantorres.prodo.R;
import com.example.lyantorres.prodo.dataModels.Store;

import java.util.ArrayList;

public class StoresAdapter extends BaseAdapter {

    private static Context mContext;
    private static ArrayList<Store> mStores;

    public StoresAdapter(Context _context, ArrayList<Store> _stores){
        mStores = _stores;
        mContext = _context;
    }

    @Override
    public int getCount() {
        return mStores.size();
    }

    @Override
    public Object getItem(int position) {
        return mStores.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.store_cell_item, parent, false);
        }

        Store store = mStores.get(position);

        TextView name = convertView.findViewById(R.id.storeName_SC_TV);
        TextView id = convertView.findViewById(R.id.storeIdentifier_SC_TV);
        TextView deviceCount = convertView.findViewById(R.id.deviceCount_SC_TV);

        name.setText(store.getmName());
        id.setText(store.getmStoreId());

        if(store.getmDevices().size() == 1){
            deviceCount.setText(store.getmDevices().size() + " Device");
        } else {
            deviceCount.setText(store.getmDevices().size() + " Devices");
        }
        return convertView;
    }
}
