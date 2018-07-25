package com.example.lyantorres.prodo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.lyantorres.prodo.commsWithServer.PostDataAsyncTask;
import com.example.lyantorres.prodo.dataModels.User;
import com.example.lyantorres.prodo.fragments.login.ForgotPasswordFragment;
import com.example.lyantorres.prodo.fragments.login.LoginFragment;
import com.example.lyantorres.prodo.fragments.login.RegisterFragment;
import com.example.lyantorres.prodo.helpers.FeedbackUtility;

public class LoginActivity extends AppCompatActivity implements LoginFragment.LoginFragmentInterface, RegisterFragment.RegisterFragmentInterface, ForgotPasswordFragment.ForgotPasswordFragmentInterface, PostDataAsyncTask.PostDataAsyncTaskInterface{

    String mAction = "";
    String mLoginAction = "LOGIN";
    String mRegisterAction = "REGISTER";
    String mForgotAction = "FORGOT";
    FeedbackUtility mFeedback = new FeedbackUtility();
    ProgressDialog mProgressDialog;
    User mUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportFragmentManager().beginTransaction().replace(R.id.login_frame, LoginFragment.newInstance(this)).commit();

        mUser = new User();
        mUser = mUser.getCurrentUser(this);

        if(mUser != null){
            Log.i("===== PRODO =====", "onCreate: " + mUser.getmEmail() + "");
            startStoresActivity();
        }
    }

    private void startStoresActivity(){
        mUser = new User();
        Intent intent = new Intent(this, StoresActivity.class);
        intent.putExtra(mUser.getmPrefUserKey(), mUser.getCurrentUser(this));
        startActivity(intent);
        finish();
    }

    private void showIndeterminateProgressDialog(String _title, String _message){
        mProgressDialog =  ProgressDialog.show(this,_title,
                _message, true);
    }

    // ============= LOGIN INTERFACE METHODS =============
    @Override
    public void loginWasPressed(String[] _body) {
        showIndeterminateProgressDialog( "", "Logging in. Please wait...");
        PostDataAsyncTask task = PostDataAsyncTask.newInstance(this, null);
        task.execute(_body);
        mAction = mLoginAction;
    }

    @Override
    public void registerWasPressed() {
        getSupportFragmentManager().beginTransaction().add(R.id.login_frame, RegisterFragment.newInstance(this)).addToBackStack("register").commit();
    }

    @Override
    public void forgotPasswordWasPressed() {
        getSupportFragmentManager().beginTransaction().add(R.id.login_frame, ForgotPasswordFragment.newInstance(this)).addToBackStack("forgot").commit();
    }

    // ============= REGISTER INTERFACE METHODS =============
    @Override
    public void userHasRegistered(String[] _body) {
        showIndeterminateProgressDialog( "", "Creating your new account. Please wait...");
        PostDataAsyncTask task = PostDataAsyncTask.newInstance(this, null);
        task.execute(_body);
        mAction = mRegisterAction;
    }

    // ============= FORGOT PASSWORD INTERFACE METHODS =============
    @Override
    public void sendEmailWasPressed(String[] _body) {
        showIndeterminateProgressDialog( "", "Sending reset password email. Please wait...");
        PostDataAsyncTask task = PostDataAsyncTask.newInstance(this, null);
        task.execute(_body);
        mAction = mForgotAction;
    }


    // ============= ASYNC TASK METHODS =============
    @Override
    public void dataPosted(String _results) {
        mProgressDialog.dismiss();
        mUser = new User();

        if(mAction.equals(mLoginAction)) {
            mUser.updateUser(_results, this);
            startStoresActivity();
        } else if (mAction == mRegisterAction){
            mUser.updateUser(_results, this);
            startStoresActivity();
        } else {
            mFeedback.showToastWith(this, "An email to reset your password has been sent.", Toast.LENGTH_SHORT);
            getSupportFragmentManager().popBackStackImmediate();
        }
    }

    @Override
    public void dataWasNotPosted() {
        mProgressDialog.dismiss();

        if(mAction.equals(mLoginAction)) {
            mFeedback.showToastWith(this, "Something went wrong logging in.", Toast.LENGTH_SHORT);
        } else if (mAction == mRegisterAction){
            mFeedback.showToastWith(this, "Something went wrong registering the new account.", Toast.LENGTH_SHORT);
        } else {
            mFeedback.showToastWith(this, "There is no account registered with this email.", Toast.LENGTH_SHORT);
        }
    }
}
