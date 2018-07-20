package com.example.lyantorres.prodo.fragments.mainScreens;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lyantorres.prodo.dataModels.Device;
import com.example.lyantorres.prodo.R;

import java.util.ArrayList;

public class DevicesGridFragment extends ListFragment {

    private static Context mContext;
    private static DeviceGridFragmentInterface mInterface;
    private static ArrayList<Device> mDevices;

    public DevicesGridFragment() {
        // Required empty public constructor
    }

    interface DeviceGridFragmentInterface{
        void deviceWasSelected(Device _device);
    }

    public static DevicesGridFragment newInstance() {
        Bundle args = new Bundle();
        DevicesGridFragment fragment = new DevicesGridFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof DeviceGridFragmentInterface){
            mInterface = (DeviceGridFragmentInterface) context;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_devices_grid, container, false);
    }

}
