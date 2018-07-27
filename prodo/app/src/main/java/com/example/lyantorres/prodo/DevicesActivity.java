package com.example.lyantorres.prodo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.lyantorres.prodo.commsWithServer.GetDataAsyncTask;
import com.example.lyantorres.prodo.dataModels.Content;
import com.example.lyantorres.prodo.dataModels.Device;
import com.example.lyantorres.prodo.dataModels.User;
import com.example.lyantorres.prodo.fragments.mainScreens.ContentGridViewFragment;
import com.example.lyantorres.prodo.fragments.mainScreens.DeviceSettingsFragment;
import com.example.lyantorres.prodo.fragments.mainScreens.DevicesGridFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DevicesActivity extends AppCompatActivity implements GetDataAsyncTask.GetAsyncTaskInterface, DevicesGridFragment.DeviceGridFragmentInterface, DeviceSettingsFragment.DeviceSettingsFragmentInterface {

    private ArrayList<Device> mDevices = new ArrayList<>();
    private User mUser;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);

        Intent intent = getIntent();
        if(intent != null) {
            String storeId = intent.getStringExtra("STOREID");
            ArrayList<String> devicesString = intent.getStringArrayListExtra("DEVICES");
            mUser = (User) intent.getSerializableExtra("USER");

            showIndeterminateProgressDialog("", "Getting your devices. Please wait...");
            String[] body = new String[] {"/"+storeId+"/devices", "{}"};
            GetDataAsyncTask task = GetDataAsyncTask.newInstance(this, mUser);
            task.execute(body);
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.device_frame, DevicesGridFragment.newInstance(this, mDevices)).commit();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //TODO: receive the selected video from gallery
    }

    private void showIndeterminateProgressDialog(String _title, String _message){
        mProgressDialog =  ProgressDialog.show(this,_title,
                _message, true);
    }

    // ============= DEVICE GRID FRAGMENT INTERFACE METHODS =============
    @Override
    public void deviceWasSelected(Device _device) {
        getSupportFragmentManager().beginTransaction().replace(R.id.device_frame, DeviceSettingsFragment.newInstance(this, _device)).addToBackStack("device").commit();
    }


    // ============= DEVICE SETTINGS FRAGMENT INTERFACE METHODS =============
    @Override
    public void deleteDevice(String _deviceId) {

    }

    @Override
    public void uploadVideoSelected() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(intent, 101);
    }

    @Override
    public void switchVideoWasSelected(Device _device) {
        Intent intent = new Intent(this,ContentActivity.class);
        intent.putExtra("DEVICE", _device);
        startActivity(intent);
    }

    // ============= GET DATA ASYNC INTERFACE METHODS =============
    @Override
    public void dataWasFetched(String _results) {
        Log.i("===== PRODO =====", "========== \n dataWasFetched: "+_results+" \n ==========");
        mProgressDialog.dismiss();

        try {
            JSONArray devices = new JSONArray(_results);

            mDevices.clear();
            for(int i = 0; i < devices.length(); i ++){
                JSONObject device = devices.getJSONObject(i);
                mDevices.add(new Device(device));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.device_frame, DevicesGridFragment.newInstance(this, mDevices)).commit();
    }

    @Override
    public void dataWasNotFetched() {

    }

}
