package com.example.lyantorres.prodo.fragments.login;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lyantorres.prodo.R;

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

}
