package com.example.lyantorres.prodo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.lyantorres.prodo.commsWithServer.DeleteDataAsyncTask;
import com.example.lyantorres.prodo.commsWithServer.GetDataAsyncTask;
import com.example.lyantorres.prodo.commsWithServer.PostDataAsyncTask;
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

public class DevicesActivity extends AppCompatActivity implements GetDataAsyncTask.GetAsyncTaskInterface, DevicesGridFragment.DeviceGridFragmentInterface, DeviceSettingsFragment.DeviceSettingsFragmentInterface,
        PostDataAsyncTask.PostDataAsyncTaskInterface{

    private ArrayList<Device> mDevices = new ArrayList<>();
    private User mUser;
    ProgressDialog mProgressDialog;
    private String mStoreId;
    private String mAction = "";
    private String mAddDevice = "ADD";
    private String mGetDevices = "DEVICES";
    private String mDeleteDevice = "DELETE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);

        Intent intent = getIntent();
        if(intent != null) {
            mStoreId= intent.getStringExtra("STOREID");
            ArrayList<String> devicesString = intent.getStringArrayListExtra("DEVICES");
            mUser = (User) intent.getSerializableExtra("USER");

            getDevices();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.device_frame, DevicesGridFragment.newInstance(this, mDevices, mStoreId)).commit();
    }


    private void getDevices(){
        showIndeterminateProgressDialog("", "Getting your devices. Please wait...");
        String[] body = new String[] {"/"+mStoreId+"/devices", "{}"};
        GetDataAsyncTask task = GetDataAsyncTask.newInstance(this, mUser);
        task.execute(body);
        mAction = mGetDevices;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //TODO: receive the selected video from gallery

        Log.i("===== PRODO =====", "========== \n onActivityResult: "+data+" \n ========== ");
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

    @Override
    public void addDevice(String[] _body) {
        showIndeterminateProgressDialog("","Adding your device. Please wait...");
        PostDataAsyncTask task = PostDataAsyncTask.newInstance(this,mUser);
        task.execute(_body);
        mAction = mAddDevice;
    }


    // ============= DEVICE SETTINGS FRAGMENT INTERFACE METHODS =============
    @Override
    public void deleteDevice(String _deviceId) {
        showIndeterminateProgressDialog("","Deleting your device. Please wait...");
        String[] body = new String[] {"/device/"+_deviceId+"", "{}"};
        DeleteDataAsyncTask task = DeleteDataAsyncTask.newInstance(this,mUser);
        task.execute(body);
        mAction = mDeleteDevice;
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

        if(mAction == mGetDevices) {

            try {
                JSONArray devices = new JSONArray(_results);

                mDevices.clear();
                for (int i = 0; i < devices.length(); i++) {
                    JSONObject device = devices.getJSONObject(i);
                    mDevices.add(new Device(device));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.device_frame, DevicesGridFragment.newInstance(this, mDevices, mStoreId)).commit();
    }

    @Override
    public void dataWasNotFetched() {
        mProgressDialog.dismiss();
        if(mAction == mGetDevices) {
            Toast.makeText(this,"Something went wrong while getting your devices.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void dataPosted(String _results) {
        mProgressDialog.dismiss();
        if(mAction == mAddDevice){
            Toast.makeText(this,"Your device has been added", Toast.LENGTH_SHORT).show();
        } else if(mAction == mDeleteDevice){
            Toast.makeText(this,"Your device has been deleted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void dataWasNotPosted() {
        mProgressDialog.dismiss();
        if(mAction == mAddDevice){
            Toast.makeText(this,"Something went wrong while adding your device.", Toast.LENGTH_LONG).show();
        } else if(mAction == mDeleteDevice){
            Toast.makeText(this,"Something went wrong while deleting your device.", Toast.LENGTH_LONG).show();
        }
    }
}
