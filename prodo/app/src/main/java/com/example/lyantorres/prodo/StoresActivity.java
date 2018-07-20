package com.example.lyantorres.prodo;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lyantorres.prodo.dataModels.Store;
import com.example.lyantorres.prodo.dataModels.User;
import com.example.lyantorres.prodo.fragments.mainScreens.StoresListViewFragment;

import java.util.ArrayList;

public class StoresActivity extends AppCompatActivity {

    private User mUser;
    private ArrayList<Store> mStores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, StoresListViewFragment.newInstance()).commit();
    }
}
