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
import com.example.lyantorres.prodo.dataModels.Store;
import com.example.lyantorres.prodo.dataModels.User;
import com.example.lyantorres.prodo.fragments.mainScreens.ProfileFragment;
import com.example.lyantorres.prodo.fragments.mainScreens.StoresListViewFragment;
import com.example.lyantorres.prodo.fragments.onboarding.OnboardingDevices;
import com.example.lyantorres.prodo.fragments.onboarding.OnboardingIntro;
import com.example.lyantorres.prodo.helpers.FeedbackUtility;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class StoresActivity extends AppCompatActivity implements StoresListViewFragment.StoreListFragmentInterface, PostDataAsyncTask.PostDataAsyncTaskInterface,
        GetDataAsyncTask.GetAsyncTaskInterface, ProfileFragment.ProfileFragmentInterface, DeleteDataAsyncTask.DeleteAsyncTaskInterface, OnboardingIntro.OnboardingIntroInterface, OnboardingDevices.OnBoardingDevicesInterface {

    private User mUser;
    private ArrayList<Store> mStores = new ArrayList<>();
    ProgressDialog mProgressDialog;
    private String mAction = "";
    private String mStoreAction = "STORE";
    private FeedbackUtility mFeedback = new FeedbackUtility();
    private String mForgotAction = "FORGOT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUser = new User();
        Intent intent = getIntent();



        if(intent != null && intent.hasExtra(mUser.getmPrefUserKey())){
            mUser = (User) intent.getSerializableExtra(mUser.getmPrefUserKey());

            if(intent.getAction() == "REGISTERED"){
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, OnboardingIntro.newInstance()).addToBackStack("ob").commit();
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, StoresListViewFragment.newInstance(this, mStores)).commit();

                String[] body = new String[] {"/stores"};
                getStores(body);
            }
        }

    }

    private void getStores(String[] _body){
        showIndeterminateProgressDialog( "", "Loading your stores. Please wait...");
        GetDataAsyncTask task = GetDataAsyncTask.newInstance(this, mUser);
        task.execute(_body);
        mAction = mStoreAction;
    }

    private void showIndeterminateProgressDialog(String _title, String _message){
        mProgressDialog =  ProgressDialog.show(this,_title,
                _message, true);
    }


    // ============= STORE LIST INTERFACE METHODS =============
    @Override
    public void storeWasSelected(String _storeId, int _i) {
        Intent intent = new Intent(this,DevicesActivity.class);
        intent.putExtra("STOREID", _storeId);
        intent.putStringArrayListExtra("DEVICES",mStores.get(_i).getmDevices());
        intent.putExtra("USER", mUser);
        startActivity(intent);
    }

    @Override
    public void storeWasAdded(String[] _body) {
        showIndeterminateProgressDialog( "", "Adding your new store. Please wait...");
        PostDataAsyncTask task = PostDataAsyncTask.newInstance(this, mUser);
        task.execute(_body);
        mAction = mStoreAction;
    }

    @Override
    public void profileWasPressed() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, ProfileFragment.newInstance(this, mUser, mStores)).addToBackStack("profile").commit();
    }


    // ============= PROFILE FRAGMENT INTERFACE METHODS =============
    @Override
    public void logoutWasPressed() {
        Intent intent  = new Intent(this,LoginActivity.class);
        intent.setAction("LOGOUT");
        startActivity(intent);
        finish();
    }

    @Override
    public void resetEmailWasPressed() {
        String[] body = new String[] {"/forgot", "{\"email\": \""+mUser.getmEmail()+"\"}"};
        showIndeterminateProgressDialog( "", "Sending reset password email. Please wait...");
        PostDataAsyncTask task = PostDataAsyncTask.newInstance(this, null);
        task.execute(body);
        mAction = mForgotAction;
    }

    @Override
    public void storeWasDeleted(Store store) {
        getSupportFragmentManager().popBackStackImmediate();

        String[] body = new String[] {"/store/"+store.get_id()+"", "{}"};
        DeleteDataAsyncTask task = DeleteDataAsyncTask.newInstance(this, mUser);
        task.execute(body);
    }

    // ============= POST DATA ASYNC INTERFACE METHODS =============
    @Override
    public void dataPosted(String _results) {
        mProgressDialog.dismiss();

        if(mAction == mStoreAction) {
            String[] body = new String[]{"/stores", "{}"};
            getStores(body);
        } if (mAction == mForgotAction){
            mFeedback.showToastWith(this, "An email to reset your password has been sent.", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void dataWasNotPosted() {
        mProgressDialog.dismiss();

        if (mAction == mStoreAction){
            mFeedback.showToastWith(this, "Something went wrong adding your store.", Toast.LENGTH_SHORT);
        }else if(mAction == mForgotAction){
            mFeedback.showToastWith(this, "An email to reset your password has been sent.", Toast.LENGTH_SHORT);
        }

        if(mAction != mForgotAction){
            String[] body = new String[] {"/stores"};
            getStores(body);
        }

    }

    // ============= GET DATA ASYNC INTERFACE METHODS =============
    @Override
    public void dataWasFetched(String _results) {
        mProgressDialog.dismiss();

        if(mAction == mStoreAction) {
            mStores.clear();
            try {
                JSONArray storesJSON = new JSONArray(_results);
                for (int i = 0; i < storesJSON.length(); i++) {
                    mStores.add(new Store(storesJSON.getJSONObject(i)));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, StoresListViewFragment.newInstance(this, mStores)).commit();
    }

    @Override
    public void dataWasNotFetched() {
        mProgressDialog.dismiss();

        if (mAction == mStoreAction){
            mFeedback.showToastWith(this, "Something went wrong getting your stores.", Toast.LENGTH_SHORT);
        }
    }

    // ============= DELETE DATA ASYNC INTERFACE METHODS =============
    @Override
    public void dataWasDeleted() {
        mProgressDialog.dismiss();
        mFeedback.showToastWith(this, "The store was deleted.", Toast.LENGTH_SHORT);

        String[] body = new String[] {"/stores"};
        getStores(body);
    }


    // ============= ONBOARDING INTERFACE METHODS =============

    @Override
    public void nextWasPressed1() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, OnboardingDevices.newInstance()).addToBackStack("ob2").commit();
    }

    @Override
    public void skipWasPressed() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, StoresListViewFragment.newInstance(this, mStores)).commit();
    }

    @Override
    public void finishWasPressed() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, StoresListViewFragment.newInstance(this, mStores)).commit();
    }
}
