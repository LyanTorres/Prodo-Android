package com.example.lyantorres.prodo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportFragmentManager().beginTransaction().replace(R.id.login_frame, LoginFragment.newInstance(this)).commit();

        User currentUser = new User().getCurrentUser(this);

        if(currentUser != null){
            startStoresActivity();
        }
    }

    private void startStoresActivity(){
        Intent intent = new Intent(this, StoresActivity.class);
        startActivity(intent);
        finish();
    }

    // ============= LOGIN INTERFACE METHODS =============
    @Override
    public void loginWasPressed(String[] _body) {
        PostDataAsyncTask task = PostDataAsyncTask.newInstance(this);
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
        PostDataAsyncTask task = PostDataAsyncTask.newInstance(this);
        task.execute(_body);
        mAction = mRegisterAction;
    }

    // ============= FORGOT PASSWORD INTERFACE METHODS =============
    @Override
    public void sendEmailWasPressed(String[] _body) {
        PostDataAsyncTask task = PostDataAsyncTask.newInstance(this);
        task.execute(_body);
        mAction = mForgotAction;
    }

    @Override
    public void dataPosted(String _results) {

        if(mAction.equals(mLoginAction)) {
            User user = new User();
            user.updateUser(_results, this);
            startStoresActivity();
        } else if (mAction == mRegisterAction){
            User user = new User();
            user.updateUser(_results, this);
            startStoresActivity();
        } else {
            mFeedback.showToastWith(this, "An email to reset your password has been sent.", Toast.LENGTH_SHORT);
            getSupportFragmentManager().popBackStackImmediate();
        }
    }

    @Override
    public void dataWasNotPosted() {

        if(mAction.equals(mLoginAction)) {
            mFeedback.showToastWith(this, "Something went wrong logging in.", Toast.LENGTH_SHORT);
        } else if (mAction == mRegisterAction){
            mFeedback.showToastWith(this, "Something went wrong registering the new account.", Toast.LENGTH_SHORT);
        } else {
            mFeedback.showToastWith(this, "There is no account registered with this email.", Toast.LENGTH_SHORT);
        }
    }
}
