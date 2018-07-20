package com.example.lyantorres.prodo;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lyantorres.prodo.dataModels.User;
import com.example.lyantorres.prodo.fragments.login.ForgotPasswordFragment;
import com.example.lyantorres.prodo.fragments.login.LoginFragment;
import com.example.lyantorres.prodo.fragments.login.RegisterFragment;

public class LoginActivity extends AppCompatActivity implements LoginFragment.LoginFragmentInterface, RegisterFragment.RegisterFragmentInterface, ForgotPasswordFragment.ForgotPasswordFragmentInterface{

    SharedPreferences mPreferences = getPreferences(MODE_PRIVATE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportFragmentManager().beginTransaction().replace(R.id.login_frame, LoginFragment.newInstance(this)).commit();
    }

    // ============= LOGIN INTERFACE METHODS =============
    @Override
    public void loginWasPressed(User _user) {

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
    public void registerWasPressed(User _user) {

    }

    // ============= FORGOT PASSWORD INTERFACE METHODS =============
    @Override
    public void sendEmailWasPressed() {
        getSupportFragmentManager().popBackStackImmediate();
    }
}
