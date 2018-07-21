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
import com.example.lyantorres.prodo.helpers.FeedbackUtility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPasswordFragment extends Fragment {

    private static Context mContext;
    private static ForgotPasswordFragmentInterface mInterface;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    public static ForgotPasswordFragment newInstance(Context _context) {

        Bundle args = new Bundle();
        mContext = _context;
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public interface ForgotPasswordFragmentInterface {
        void sendEmailWasPressed();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof ForgotPasswordFragmentInterface) {
            mInterface = (ForgotPasswordFragmentInterface) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getActivity() != null){

            Button sendEmailBTN = (Button) getActivity().findViewById(R.id.sendEmail_FORGOT_BTN);

            sendEmailBTN.setOnClickListener(sendEmailWasPressed);

        }
    }

    Button.OnClickListener sendEmailWasPressed = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText emailET = (EditText) getActivity().findViewById(R.id.email_FORGOT_ET);
            String email = emailET.getText().toString();

            if(email.isEmpty()){
                emailET.setError(getString(R.string.no_email));
            } else {

                if(isValidEmail(email)) {
                    // TODO: send data to server so that password can be reset


                    new FeedbackUtility().showToastWith(mContext, "An email to change your password has been sent", Toast.LENGTH_LONG);

                    if (mInterface != null) {
                        mInterface.sendEmailWasPressed();
                    }
                } else {
                    emailET.setError(getString(R.string.no_valid_email));
                }
            }
        }
    };

    private Boolean isValidEmail(String _email){
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(_email);
        return matcher.matches();
    }
}
