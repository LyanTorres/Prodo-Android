package com.example.lyantorres.prodo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lyantorres.prodo.dataModels.User;
import com.example.lyantorres.prodo.fragments.login.ForgotPasswordFragment;
import com.example.lyantorres.prodo.fragments.login.LoginFragment;
import com.example.lyantorres.prodo.fragments.login.RegisterFragment;

public class LoginActivity extends AppCompatActivity implements LoginFragment.LoginFragmentInterface, RegisterFragment.RegisterFragmentInterface, ForgotPasswordFragment.ForgotPasswordFragmentInterface{

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
    public void loginWasPressed() {
        startStoresActivity();
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
    public void userHasRegistered() {
        startStoresActivity();
    }

    // ============= FORGOT PASSWORD INTERFACE METHODS =============
    @Override
    public void sendEmailWasPressed() {
        getSupportFragmentManager().popBackStackImmediate();
    }
}
