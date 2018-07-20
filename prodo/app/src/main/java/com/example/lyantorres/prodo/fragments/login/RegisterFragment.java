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

import com.example.lyantorres.prodo.dataModels.User;
import com.example.lyantorres.prodo.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterFragment extends Fragment {

    private static Context mContext;
    private static RegisterFragmentInterface mInterface;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public interface RegisterFragmentInterface {
        void userHasRegistered();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof RegisterFragmentInterface){
            mInterface = (RegisterFragmentInterface) context;
        }
    }


    public static RegisterFragment newInstance(Context _context) {
        RegisterFragment fragment = new RegisterFragment();
        mContext = _context;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getActivity() != null){

            Button registerBTN = (Button) getActivity().findViewById(R.id.register_REGISTER_BTN);

            registerBTN.setOnClickListener(registerWasPressed);
        }

    }

    private Button.OnClickListener registerWasPressed = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(inputIsValid()){

                // TODO: ========== post new user to server ==========
                // TODO: ========== save new user to prefs ==========

                if(mInterface != null){
                    mInterface.userHasRegistered();
                }
            }
        }
    };

    private Boolean inputIsValid() {
        EditText emailET = (EditText) getActivity().findViewById(R.id.email_REGISTER_ET);
        EditText passwordET = (EditText) getActivity().findViewById(R.id.password_REGISTER_ET);
        EditText passwordConfirmET = (EditText) getActivity().findViewById(R.id.confirmPassword_REGISTER_ET);

        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        String passwordConfirm = passwordConfirmET.getText().toString();

        if(!email.isEmpty() && !password.isEmpty() && !passwordConfirm.isEmpty()) {
            if(isValidEmail(email)){

                if(isValidPassword(password)) {

                    if(password.equals(passwordConfirm)) {
                        return true;

                    } else {
                        passwordET.setError(getString(R.string.passwords_do_not_match));
                    }

                } else {
                    passwordET.setError(getString(R.string.no_valid_password));
                }

            } else {
                emailET.setError(getString(R.string.no_valid_email));
            }

        } else {
            validateInputs();
        }
        return false;
    }

    private Boolean isValidEmail(String _email){
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(_email);
        return matcher.matches();
    }

    private Boolean isValidPassword(String _password){
        if(_password.length() < 6) {
            return false;
        }
        return true;
    }

    private void validateInputs(){
        EditText emailET = (EditText) getActivity().findViewById(R.id.email_REGISTER_ET);
        EditText passwordET = (EditText) getActivity().findViewById(R.id.password_REGISTER_ET);
        EditText passwordConfirmET = (EditText) getActivity().findViewById(R.id.confirmPassword_REGISTER_ET);

        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        String passwordConfirm = passwordConfirmET.getText().toString();

        if(email.isEmpty()){
            emailET.setError(getString(R.string.no_email));
        }

        if(password.isEmpty()){
            passwordET.setError(getString(R.string.no_password));
        }

        if(passwordConfirm.isEmpty()){
            passwordConfirmET.setError(getString(R.string.no_confirm_password));
        }
    }


}
