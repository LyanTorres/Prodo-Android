package com.example.lyantorres.prodo.fragments.onboarding;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.lyantorres.prodo.R;

public class OnboardingDevices extends Fragment {

    private static OnBoardingDevicesInterface mInterface;

    public OnboardingDevices() {
        // Required empty public constructor
    }

    public interface OnBoardingDevicesInterface{
        void finishWasPressed();
    }

    public static OnboardingDevices newInstance() {

        Bundle args = new Bundle();

        OnboardingDevices fragment = new OnboardingDevices();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnBoardingDevicesInterface){
            mInterface = (OnBoardingDevicesInterface) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_onboarding_devices, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getActivity() != null){

            Button finish = getActivity().findViewById(R.id.finish_btn);

            finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mInterface != null){
                        mInterface.finishWasPressed();
                    }
                }
            });
        }
    }
}
