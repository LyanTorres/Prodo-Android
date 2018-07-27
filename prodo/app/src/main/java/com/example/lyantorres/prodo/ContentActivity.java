package com.example.lyantorres.prodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lyantorres.prodo.dataModels.Content;
import com.example.lyantorres.prodo.dataModels.Device;
import com.example.lyantorres.prodo.fragments.mainScreens.ContentGridViewFragment;

import java.util.ArrayList;

public class ContentActivity extends AppCompatActivity implements ContentGridViewFragment.ContentGridViewFragmentInterface {


    Device mDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_content);

        Intent intent = getIntent();

        if(intent != null){
            mDevice = (Device) intent.getSerializableExtra("DEVICE");
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, ContentGridViewFragment.newInstance(this, mDevice.getmContent())).commit();
    }

    @Override
    public void contentSelected(Content _content) {

        //TODO: PUSH UP CONTENT TO SERVER
    }
}
