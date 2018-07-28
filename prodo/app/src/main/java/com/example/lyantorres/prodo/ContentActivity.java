package com.example.lyantorres.prodo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.lyantorres.prodo.commsWithServer.PostDataAsyncTask;
import com.example.lyantorres.prodo.dataModels.Content;
import com.example.lyantorres.prodo.dataModels.Device;
import com.example.lyantorres.prodo.dataModels.User;
import com.example.lyantorres.prodo.fragments.mainScreens.ContentGridViewFragment;

import java.util.ArrayList;

public class ContentActivity extends AppCompatActivity implements ContentGridViewFragment.ContentGridViewFragmentInterface, PostDataAsyncTask.PostDataAsyncTaskInterface {

    ProgressDialog mProgressDialog;
    Device mDevice;
    User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_content);

        Intent intent = getIntent();

        if(intent != null){
            mDevice = (Device) intent.getSerializableExtra("DEVICE");
            mUser = (User) intent.getSerializableExtra("USER");
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, ContentGridViewFragment.newInstance(this, mDevice.getmContent())).commit();
    }

    private void showIndeterminateProgressDialog(String _title, String _message){
        mProgressDialog =  ProgressDialog.show(this,_title,
                _message, true);
    }

    @Override
    public void contentSelected(Content _content) {
        showIndeterminateProgressDialog("", "Updating your device's video. Please wait...");
        String[] body = new String[] {"/publish", "{\"contentLink\":\""+_content.getmContentLink()+"\",\"deviceId\":\""+mDevice.get_id()+"\",\"contentId\":\""+_content.get_id()+"\",\"contentName\":\""+_content.getmName()+"\",\"contentThumbnail\":\""+_content.getmThumbnailLink()+"\"}"};
        PostDataAsyncTask task = PostDataAsyncTask.newInstance(this, mUser);
        task.execute(body);
    }

    @Override
    public void dataPosted(String _results) {
        mProgressDialog.dismiss();
        Toast.makeText(this, "Your device's video has been updated.", Toast.LENGTH_SHORT);
        finish();
    }

    @Override
    public void dataWasNotPosted() {
        mProgressDialog.dismiss();
        Toast.makeText(this, "Something went wrong updating your device's video.", Toast.LENGTH_SHORT);
        finish();
    }
}
