package com.example.lyantorres.prodo.fragments.mainScreens;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lyantorres.prodo.R;
import com.example.lyantorres.prodo.dataModels.Content;
import com.example.lyantorres.prodo.dataModels.Device;
import com.squareup.picasso.Picasso;

public class DeviceSettingsFragment extends Fragment {

    private static Context mContext;
    private static Device mDevice;
    private static DeviceSettingsFragmentInterface mInterface;

    public DeviceSettingsFragment() {
        // Required empty public constructor
    }

    public static DeviceSettingsFragment newInstance(Context _context, Device _device) {

        mContext = _context;
        mDevice = _device;

        Bundle args = new Bundle();

        DeviceSettingsFragment fragment = new DeviceSettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof DevicesGridFragment.DeviceGridFragmentInterface){
            mInterface = (DeviceSettingsFragmentInterface) context;
        }
    }

    public interface DeviceSettingsFragmentInterface{
        void deleteDevice(String _deviceId);
        void uploadVideoSelected();
        void switchVideoWasSelected(Device _device);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.device_settings_menu, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_device_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getActivity() != null){
            TextView name = getActivity().findViewById(R.id.name_DS_TV);
            ImageView preview = getActivity().findViewById(R.id.preview_DS_IV);
            WebView wv = getActivity().findViewById(R.id.webview_DS_WV);
            Button switchVideo = getActivity().findViewById(R.id.switchvideo_DS_BTN);
            Button uploadVideo = getActivity().findViewById(R.id.upload_DS_BTN);
            Switch play = getActivity().findViewById(R.id.switchlive_DS_S);
            TextView cp = getActivity().findViewById(R.id.current_DS_TV);

            play.setEnabled(false);

            name.setText(mDevice.getmName());

            if(mDevice.getmCurrentContent().getmThumbnailLink() != null && !mDevice.getmCurrentContent().getmThumbnailLink().isEmpty() ){
                Picasso.with(mContext).load(mDevice.getmCurrentContent().getmThumbnailLink()).into(preview);
            }

            if(mDevice.getmCurrentContent().getmName()!= null && !mDevice.getmCurrentContent().getmName().isEmpty()){
                cp.setText("Currently playing: "+ mDevice.getmCurrentContent().getmName());
            } else {
                cp.setText("Not playing anything");
            }

            switchVideo.setOnClickListener(switchVideoSelected);
            uploadVideo.setOnClickListener(uploadVideoSelected);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.device_settings_delete){

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Deleting: "+mDevice.getmName()+"");
            builder.setMessage("Are you sure you want to delete this device? ");
            builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(mInterface != null){
                        mInterface.deleteDevice(mDevice.get_id());
                    }
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.create().show();
        }

        return super.onOptionsItemSelected(item);
    }

    Button.OnClickListener switchVideoSelected = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(mDevice.getmContent() != null) {
                if (mInterface != null) {
                    mInterface.switchVideoWasSelected(mDevice);
                }
            } else {
                Toast.makeText(mContext, "There is no previous uploads to choose from.", Toast.LENGTH_LONG).show();
            }
        }
    };

    Button.OnClickListener uploadVideoSelected = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mInterface != null){
                mInterface.uploadVideoSelected();
            }
        }
    };

}
