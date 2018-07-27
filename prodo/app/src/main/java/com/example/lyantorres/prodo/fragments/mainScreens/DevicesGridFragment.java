package com.example.lyantorres.prodo.fragments.mainScreens;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lyantorres.prodo.adapters.DevicesAdapter;
import com.example.lyantorres.prodo.dataModels.Device;
import com.example.lyantorres.prodo.R;
import com.example.lyantorres.prodo.dataModels.Store;

import java.util.ArrayList;

public class DevicesGridFragment extends Fragment {

    private static Context mContext;
    private static DeviceGridFragmentInterface mInterface;
    private static ArrayList<Device> mDevices;
    private static String mStore;

    public DevicesGridFragment() {
        // Required empty public constructor
    }

    public interface DeviceGridFragmentInterface{
        void deviceWasSelected(Device _device);
        void addDevice(String[] _body);
    }

    public static DevicesGridFragment newInstance(Context _context, ArrayList<Device> _devices, String _store) {

        mContext = _context;
        mDevices = _devices;
        mStore = _store;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.device_fragment_menu, menu);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getActivity() != null){

            GridView gv = getActivity().findViewById(R.id.grid_DGV_GV);
            ImageView empty = getActivity().findViewById(R.id.nodevices_DGV_IV);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.devices_menu_add){
            LayoutInflater li = LayoutInflater.from(getActivity());
            View promptsView = li.inflate(R.layout.custom_add_device, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setView(promptsView);

            final EditText deviceCode1ET = (EditText) promptsView.findViewById(R.id.alert_device_code1);
            final EditText deviceCode2ET = (EditText) promptsView.findViewById(R.id.alert_device_code3);
            final EditText name = (EditText) promptsView.findViewById(R.id.alert_device_name);

            alertDialogBuilder
                    .setTitle("Add a device")
                    .setMessage("Enter the device code you see on screen, then type in what you want to name this device.")
                    .setCancelable(true)
                    .setPositiveButton("ADD", null)
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            });
            final AlertDialog alertDialog = alertDialogBuilder.create();

            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    Button okBTN = ((AlertDialog)dialog).getButton(alertDialog.BUTTON_POSITIVE);
                    okBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String code1 = deviceCode1ET.getText().toString();
                            String code2 = deviceCode2ET.getText().toString();
                            String dname = name.getText().toString();

                            if(!code1.isEmpty() && !code2.isEmpty() && !dname.isEmpty()){

                                if(code1.length() == 4 && code2.length() == 4) {
                                    String[] body = new String[]{"/deviceLinked", "{\"name\":\"" + dname + "\",\"_store\":\"" + mStore + "\",\"key\":\"" + code1 + "-" + code2 + "\"}"};

                                    if (mInterface != null) {
                                        mInterface.addDevice(body);
                                    }

                                    alertDialog.dismiss();
                                } else {
                                    if(code1.length()< 4){
                                        deviceCode1ET.setError("Please type in the first 4 digits of your device code");
                                    }
                                    if(code2.length()< 4){
                                        deviceCode2ET.setError("Please type in the second 4 digits of your device code");
                                    }
                                }
                            }

                            if(dname.isEmpty()){
                                name.setError("Please do not leave this blank");
                            }

                            if(code1.isEmpty()){
                                deviceCode1ET.setError("please don't leave this blank");
                            }
                            if(code2.isEmpty()){
                                deviceCode2ET.setError("please don't leave this blank");
                            }
                        }
                    });
                }
            });

            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }
}
