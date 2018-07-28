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

public class OnboardingIntro extends Fragment {

    private static OnboardingIntroInterface mInterface;

    public OnboardingIntro() {
        // Required empty public constructor
    }

    public interface OnboardingIntroInterface{
        void nextWasPressed1();
        void skipWasPressed();
    }

    public static OnboardingIntro newInstance() {

        Bundle args = new Bundle();
        OnboardingIntro fragment = new OnboardingIntro();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof OnboardingIntroInterface){
            mInterface = (OnboardingIntroInterface) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_onboarding_intro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getActivity() != null){

            Button one = getActivity().findViewById(R.id.next_1);
            Button skip = getActivity().findViewById(R.id.skip_1);

            one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mInterface != null){
                        mInterface.nextWasPressed1();
                    }
                }
            });

            skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mInterface != null){
                        mInterface.skipWasPressed();
                    }
                }
            });
        }
    }
}
