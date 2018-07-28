package com.example.lyantorres.prodo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
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
    private String mCurrentlySelectedDeviceId = "";
    private String mNewlyUploadedVideoUrl = "";
    private String mGetContentId = "GET CONTENT ID";
    private String mPublishVideo = "PUBLISH VIDEO";
    private Uri mFile;
    private String mFileName="";
    private String mThumbnailLink;
    private String mContentLink;

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

        if(data != null) {
            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.custom_add_video, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setView(promptsView);

            final EditText menuNameET = (EditText) promptsView.findViewById(R.id.menu_name);
            final Uri dataF = data.getData();

            alertDialogBuilder
                    .setTitle("Add a menu")
                    .setMessage("Please enter a name for your new menu. Ex. Early Bird Special")
                    .setCancelable(true)
                    .setPositiveButton("ADD", null)
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            final AlertDialog alertDialog = alertDialogBuilder.create();

            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    Button okBTN = ((AlertDialog) dialog).getButton(alertDialog.BUTTON_POSITIVE);
                    okBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String name = menuNameET.getText().toString();

                            if (!name.isEmpty()) {
                                mFile = dataF;
                                mFileName = name;
                                getContentId();
                                alertDialog.dismiss();
                            }

                            if (name.isEmpty()) {
                                menuNameET.setError("Please do not leave this blank");
                            }
                        }
                    });
                }
            });

            alertDialog.show();
        }

        if(requestCode == 101){
            getDevices();
        }
    }

    private void getContentId() {
        mAction = mGetContentId;
        String[] contentIdBody = {"/content", "{\"deviceId\":\"" + mCurrentlySelectedDeviceId + "\"}"};
        PostDataAsyncTask task = PostDataAsyncTask.newInstance(this, mUser);
        task.execute(contentIdBody);
        showIndeterminateProgressDialog("", "Uploading your video. Please wait...");
    }

    private void publishContentToServer(final Content _content){
        StorageReference storage = FirebaseStorage.getInstance().getReference();
        Uri file = mFile;
        StorageReference riversRef = storage.child(mCurrentlySelectedDeviceId+"/"+_content.get_id()+"/video.mp4");
        uploadThumbnailForVideo(_content);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        mNewlyUploadedVideoUrl = downloadUrl.toString();
                        showIndeterminateProgressDialog("","Uploading your video. Please wait...");
                        String[] body = new String[] {"/publish", "{\"contentLink\":\""+mNewlyUploadedVideoUrl+"\",\"deviceId\":\""+mCurrentlySelectedDeviceId+"\",\"contentId\":\""+_content.get_id()+"\",\"contentName\":\""+mFileName+"\",\"contentThumbnail\":\""+mThumbnailLink+"\"}"};
                        try {
                            JSONObject obj = new JSONObject(body[1]);
                            Log.i("===== PRODO =====", "dataPosted: "+obj+" ");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        PostDataAsyncTask task = PostDataAsyncTask.newInstance(getApplicationContext(), mUser);
                        task.execute(body);
                        mAction = mPublishVideo;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Something went wrong uploading your video.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void uploadThumbnailForVideo(Content _content){
        final Content content = _content;
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.getContentResolver().query(mFile, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        StorageReference storage = FirebaseStorage.getInstance().getReference();
        final StorageReference riversRef = storage.child(mCurrentlySelectedDeviceId+"/"+content.get_id()+"/thumbnail/thumbnail.png");


        riversRef.putFile(Uri.parse(picturePath))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        content.setmThumbnailLink(riversRef.getDownloadUrl().toString());
                    }
                });

        mThumbnailLink = content.getmThumbnailLink();
    }

    private void showIndeterminateProgressDialog(String _title, String _message){
        mProgressDialog =  ProgressDialog.show(this,_title,
                _message, true);
    }

    // ============= DEVICE GRID FRAGMENT INTERFACE METHODS =============
    @Override
    public void deviceWasSelected(Device _device) {
        mCurrentlySelectedDeviceId = _device.get_id();
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
        intent.putExtra("USER", mUser);
        startActivityForResult(intent, 101);
    }

    // ============= GET DATA ASYNC INTERFACE METHODS =============
    @Override
    public void dataWasFetched(String _results) {
        mProgressDialog.dismiss();

        if(mAction == mGetDevices) {
            try {
                JSONArray devices = new JSONArray(_results);
                mDevices = new ArrayList<>();
                for (int i = 0; i < devices.length(); i++) {
                    JSONObject device = devices.getJSONObject(i);
                    mDevices.add(new Device(device));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e){
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
            mProgressDialog.dismiss();
            Toast.makeText(this,"Your device has been added", Toast.LENGTH_SHORT).show();
            getDevices();
        } else if(mAction == mDeleteDevice){
            mProgressDialog.dismiss();
            Toast.makeText(this,"Your device has been deleted", Toast.LENGTH_SHORT).show();
            getDevices();
        } else if(mAction == mGetContentId){
            try {
                JSONObject jsonObj = new JSONObject(_results);
                Content content = new Content(jsonObj);
                publishContentToServer(content);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if(mAction == mPublishVideo){
            mProgressDialog.dismiss();
            Toast.makeText(this, "Your new video has been published", Toast.LENGTH_SHORT).show();
            getDevices();
        }

        mProgressDialog.dismiss();
    }

    @Override
    public void dataWasNotPosted() {
        mProgressDialog.dismiss();
        if(mAction == mAddDevice){
            Toast.makeText(this,"Something went wrong while adding your device.", Toast.LENGTH_LONG).show();
        } else if(mAction == mDeleteDevice){
            Toast.makeText(this,"Something went wrong while deleting your device.", Toast.LENGTH_LONG).show();
        } else if(mAction == mPublishVideo){
            Toast.makeText(this,"Something went wrong while publishing your video.", Toast.LENGTH_LONG).show();
        } else if (mAction == mGetContentId) {
            Toast.makeText(this,"Something went wrong while uploading content..", Toast.LENGTH_LONG).show();
        }
    }
}
