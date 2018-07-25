package com.example.lyantorres.prodo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.lyantorres.prodo.commsWithServer.GetDataAsyncTask;
import com.example.lyantorres.prodo.commsWithServer.PostDataAsyncTask;
import com.example.lyantorres.prodo.dataModels.Store;
import com.example.lyantorres.prodo.dataModels.User;
import com.example.lyantorres.prodo.fragments.mainScreens.StoresListViewFragment;

import java.util.ArrayList;

public class StoresActivity extends AppCompatActivity implements StoresListViewFragment.StoreListFragmentInterface, PostDataAsyncTask.PostDataAsyncTaskInterface, GetDataAsyncTask.GetAsyncTaskInterface {

    private User mUser;
    private ArrayList<Store> mStores;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUser = new User();
        Intent intent = getIntent();

        if(intent != null && intent.hasExtra(mUser.getmPrefUserKey())){
            mUser = (User) intent.getSerializableExtra(mUser.getmPrefUserKey());
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, StoresListViewFragment.newInstance(this)).commit();
    }

    private void getStores(String[] _body){
        showIndeterminateProgressDialog( "", "Loading your stores. Please wait...");
        GetDataAsyncTask task = GetDataAsyncTask.newInstance(this, mUser);
        task.execute(_body);
    }

    private void showIndeterminateProgressDialog(String _title, String _message){
        mProgressDialog =  ProgressDialog.show(this,_title,
                _message, true);
    }


    // ============= STORE LIST INTERFACE METHODS =============
    @Override
    public void storeWasSelected(String _storeId, ArrayList<String> _devices) {

    }

    @Override
    public void storeWasAdded(String[] _body) {
        showIndeterminateProgressDialog( "", "Adding your new store. Please wait...");
        PostDataAsyncTask task = PostDataAsyncTask.newInstance(this, mUser);
        task.execute(_body);
    }

    // ============= POST DATA ASYNC INTERFACE METHODS =============
    @Override
    public void dataPosted(String _results) {
        mProgressDialog.dismiss();
        String[] body = new String[] {"/stores", "{}"};
        getStores(body);
    }

    @Override
    public void dataWasNotPosted() {
        mProgressDialog.dismiss();
        String[] body = new String[] {"/stores", "{}"};
        getStores(body);
    }

    // ============= GET DATA ASYNC INTERFACE METHODS =============
    @Override
    public void dataWasFetched(String _results) {
        mProgressDialog.dismiss();
        Log.i("PRODO", "dataWasFetched: "+_results+"");
    }

    @Override
    public void dataWasNotFetched() {
        mProgressDialog.dismiss();

    }
}
