package com.example.lyantorres.prodo.fragments.mainScreens;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lyantorres.prodo.dataModels.Store;
import com.example.lyantorres.prodo.R;

import java.util.ArrayList;


public class StoresListViewFragment extends ListFragment{


    private static Context mContext;
    private static StoreListFragmentInterface mInterface;
    private static ArrayList<Store> mStores;

    public StoresListViewFragment() {
        // Required empty public constructor
    }

    interface StoreListFragmentInterface{
        void storeWasSelected(String _storeId, ArrayList<String> _devices);
    }

    public static StoresListViewFragment newInstance() {
        StoresListViewFragment fragment = new StoresListViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StoreListFragmentInterface){
            mInterface = (StoreListFragmentInterface) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: FINISH SETUP FOR DISPLAYING STORES
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stores_list_view, container, false);
    }
}
