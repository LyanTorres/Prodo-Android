package com.example.lyantorres.prodo.fragments.mainScreens;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lyantorres.prodo.R;
import com.example.lyantorres.prodo.adapters.StoresAdapter;
import com.example.lyantorres.prodo.dataModels.Store;
import com.example.lyantorres.prodo.dataModels.User;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private static Context mContext;
    private static User mUser;
    private static ArrayList mStores = new ArrayList();
    private static ProfileFragmentInterface mInterface;


    public ProfileFragment() {
        // Required empty public constructor
    }

    public interface ProfileFragmentInterface {
        void logoutWasPressed();
        void resetEmailWasPressed();
        void storeWasDeleted(Store store);
    }

    public static ProfileFragment newInstance(Context _context, User _user, ArrayList<Store> _stores) {

        mContext = _context;
        mUser = _user;
        mStores = _stores;
        Bundle args = new Bundle();
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof ProfileFragmentInterface){
            mInterface = (ProfileFragmentInterface) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getActivity() != null){

            Button resetPassword = getActivity().findViewById(R.id.resetPassword_P_BTN);
            ListView storesLV = getActivity().findViewById(R.id.stores_P_LV);
            TextView email = getActivity().findViewById(R.id.email_P_TV);
            TextView noStores = getActivity().findViewById(R.id.nostores_P_TV);

            email.setText(mUser.getmEmail());
            resetPassword.setOnClickListener(resetPasswordWasPressed);

            if(mStores.size() != 0){
                noStores.setVisibility(View.INVISIBLE);
                StoresAdapter adapter = new StoresAdapter(mContext,mStores);
                storesLV.setAdapter(adapter);

                storesLV.setOnItemClickListener(itemWasSelected);

            } else {
                noStores.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.profile_menu_logout){
            if(mInterface != null){
                mInterface.logoutWasPressed();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private static Button.OnClickListener resetPasswordWasPressed = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (mInterface != null){
                mInterface.resetEmailWasPressed();
            }
        }
    };

    ListView.OnItemClickListener itemWasSelected = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final Store store = (Store) mStores.get(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Deleting: "+store.getmName()+"");
            builder.setMessage("Are you sure you want to delete this store? \n Store: \n"+store.getmName()+" \n"+store.getmStoreId()+"\n"+store.getmDevices().size()+" device(s)");
            builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(mInterface != null){
                        mInterface.storeWasDeleted(store);
                    }
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.create().show();
        }
    };
}
