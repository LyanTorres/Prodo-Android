package com.example.lyantorres.prodo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lyantorres.prodo.R;
import com.example.lyantorres.prodo.dataModels.Content;
import com.example.lyantorres.prodo.dataModels.Device;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ContentAdapter extends BaseAdapter {

    private static Context mContext;
    private static ArrayList<Content> mContent;

    public ContentAdapter(Context _context, ArrayList<Content> _content){
        mContent = _content;
        mContext = _context;
    }

    @Override
    public int getCount() {
        return mContent.size();
    }

    @Override
    public Object getItem(int position) {
        return mContent.get(position);
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

        Content content = mContent.get(position);

        ImageView preview = convertView.findViewById(R.id.contentPreview_C_IV);
        TextView name = convertView.findViewById(R.id.contentName_S_TV);

        name.setText(content.getmName());
        Picasso.with(mContext).load(content.getmThumbnailLink()).into(preview);

        return convertView;
    }
}
