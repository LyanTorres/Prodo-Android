package com.example.lyantorres.prodo.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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
        // TODO: Setup so that it populates the cells for you
        return null;
    }
}
