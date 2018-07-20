package com.example.lyantorres.prodo.fragments.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lyantorres.prodo.dataModels.User;
import com.example.lyantorres.prodo.R;

public class RegisterFragment extends Fragment {

    private static Context mContext;
    private static RegisterFragmentInterface mInterface;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public interface RegisterFragmentInterface {
        void registerWasPressed(User _user);
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

    }


}
