package com.example.lyantorres.prodo.fragments.mainScreens;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.lyantorres.prodo.adapters.DevicesAdapter;
import com.example.lyantorres.prodo.dataModels.Device;
import com.example.lyantorres.prodo.R;

import java.util.ArrayList;

public class DevicesGridFragment extends Fragment {

    private static Context mContext;
    private static DeviceGridFragmentInterface mInterface;
    private static ArrayList<Device> mDevices;

    public DevicesGridFragment() {
        // Required empty public constructor
    }

    public interface DeviceGridFragmentInterface{
        void deviceWasSelected(Device _device);
    }

    public static DevicesGridFragment newInstance(Context _context, ArrayList<Device> _devices) {

        mContext = _context;
        mDevices = _devices;

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
        return inflater.inflate(R.layout.fragment_devices_grid, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getActivity() != null){

            GridView gv = getActivity().findViewById(R.id.grid_DGV_GV);
            TextView empty = getActivity().findViewById(R.id.nodevices_DGV_TV);

            if(mDevices.size() == 0) {
                empty.setVisibility(View.VISIBLE);
            } else {
                empty.setVisibility(View.INVISIBLE);
                DevicesAdapter adapter = new DevicesAdapter(mContext, mDevices);
                gv.setAdapter(adapter);
                gv.setOnItemClickListener(deviceClicked);
            }
        }
    }

    GridView.OnItemClickListener deviceClicked = new GridView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(mInterface != null){
                mInterface.deviceWasSelected(mDevices.get(position));
            }
        }
    };
}
