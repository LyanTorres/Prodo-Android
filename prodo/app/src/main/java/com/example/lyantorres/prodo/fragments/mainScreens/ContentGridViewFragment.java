package com.example.lyantorres.prodo.fragments.mainScreens;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.lyantorres.prodo.R;
import com.example.lyantorres.prodo.adapters.ContentAdapter;
import com.example.lyantorres.prodo.dataModels.Content;
import com.example.lyantorres.prodo.dataModels.Device;

import java.util.ArrayList;

public class ContentGridViewFragment extends Fragment {

    private static Context mContext;
    private static ContentGridViewFragmentInterface mInterface;
    private static ArrayList<Content> mContent;

    public ContentGridViewFragment() {
        // Required empty public constructor
    }

    public static ContentGridViewFragment newInstance(Context _context, ArrayList<Content> _content) {

        mContent = _content;
        mContext = _context;

        Bundle args = new Bundle();

        ContentGridViewFragment fragment = new ContentGridViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof ContentGridViewFragmentInterface){
            mInterface = (ContentGridViewFragmentInterface) context;
        }
    }

    public interface ContentGridViewFragmentInterface{
        void contentSelected(Content _content);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_content_grid_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getActivity() != null){

            GridView gv = getActivity().findViewById(R.id.content_C_GV);
            TextView empty = getActivity().findViewById(R.id.empty_S_TV);

            if(mContent.size() == 0 ){
                empty.setVisibility(View.VISIBLE);
            } else {
                empty.setVisibility(View.INVISIBLE);
                ContentAdapter adapter = new ContentAdapter(mContext, mContent);
                gv.setAdapter(adapter);
                gv.setOnItemClickListener(contentSelected);
            }
        }
    }

    GridView.OnItemClickListener contentSelected = new GridView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final int i = position;
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Switching video");
            builder.setMessage("Are you sure you want to change the content on your device to the newly selected content?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(mInterface != null){
                        mInterface.contentSelected(mContent.get(i));
                    }
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.create().show();
        }
    };
}
