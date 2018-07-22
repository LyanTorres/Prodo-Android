package com.example.lyantorres.prodo.fragments.login;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lyantorres.prodo.R;
import com.example.lyantorres.prodo.helpers.ConnectionUtility;
import com.example.lyantorres.prodo.helpers.FeedbackUtility;

public class LoginFragment extends Fragment{


    private static Context mContext;
    private static LoginFragmentInterface mInterface;
    private static ConnectionUtility mConnectionUtility = new ConnectionUtility();
    private static FeedbackUtility mFeedbackUtility = new FeedbackUtility();

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(Context _context) {
        mContext = _context;
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public interface LoginFragmentInterface {
        void loginWasPressed(String[] _body);
        void registerWasPressed();
        void forgotPasswordWasPressed();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof LoginFragmentInterface){
            mInterface = (LoginFragmentInterface) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getActivity() != null){
            Button loginButon = (Button) getActivity().findViewById(R.id.login_LOGIN_BTN);
            Button registerButton = (Button) getActivity().findViewById(R.id.register_LOGIN_BTN);
            Button forgotButton = (Button) getActivity().findViewById(R.id.forgotPassword_LOGIN_BTN);

            loginButon.setOnClickListener(loginWasPressed);
            registerButton.setOnClickListener(registerWasPressed);
            forgotButton.setOnClickListener(forgotPasswordWasPressed);
        }
    }

    Button.OnClickListener loginWasPressed = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(getActivity() != null) {
                EditText emailET = (EditText) getActivity().findViewById(R.id.email_LOGIN_ET);
                EditText passwordET = (EditText) getActivity().findViewById(R.id.password_LOGIN_ET);
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();

                if(mConnectionUtility.isNetworkAvailable(mContext)){

                    if(!email.isEmpty() && !password.isEmpty()) {
                        String[] body = new String[] {"/users/login", "{\"email\": \""+email+"\",\"password\":\""+password+"\"}"};

                        if(mInterface != null){
                            mInterface.loginWasPressed(body);
                        }

                    } else {
                        if(email.isEmpty()){
                            emailET.setError(getString(R.string.no_email));
                        }
                        if (password.isEmpty()){
                            passwordET.setError(getString(R.string.no_password));
                        }
                    }

                } else {
                    mFeedbackUtility.showToastWith(mContext, "Couldn't log in ", Toast.LENGTH_SHORT);
                }

            }
        }
    };

    Button.OnClickListener registerWasPressed = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mInterface != null){
                mInterface.registerWasPressed();
            }
        }
    };

    Button.OnClickListener forgotPasswordWasPressed = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mInterface != null){
                mInterface.forgotPasswordWasPressed();
            }
        }
    };

}
