package com.example.lyantorres.prodo.fragments.mainScreens;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.lyantorres.prodo.adapters.StoresAdapter;
import com.example.lyantorres.prodo.dataModels.Store;
import com.example.lyantorres.prodo.R;

import java.util.ArrayList;


public class StoresListViewFragment extends ListFragment{


    private static Context mContext;
    private static StoreListFragmentInterface mInterface;
    private static ArrayList<Store> mStores;

    public StoresListViewFragment() {
        // Required empty public constructor
    }

    public interface StoreListFragmentInterface{
        void storeWasSelected(String _storeId, int _i);
        void storeWasAdded(String[] _body);
        void profileWasPressed();
    }

    public static StoresListViewFragment newInstance(Context _context, ArrayList<Store> _stores) {
        StoresListViewFragment fragment = new StoresListViewFragment();
        mContext = _context;
        mStores = _stores;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StoreListFragmentInterface){
            mInterface = (StoreListFragmentInterface) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getActivity() != null){
            StoresAdapter adapter = new StoresAdapter(mContext, mStores);
            ListView lv = getActivity().findViewById(android.R.id.list);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(itemWasSelected);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stores_list_view, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.stores_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.stores_menu_add) {
            addStoreDialog();
        } else if (item.getItemId() == R.id.stores_menu_profile){
            if (mInterface != null){
                mInterface.profileWasPressed();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void addStoreDialog(){
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.custom_add_store, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptsView);

        final EditText storeNameET = (EditText) promptsView.findViewById(R.id.alert_store_name);
        final EditText storeIDET = (EditText) promptsView.findViewById(R.id.alert_store_id);

        alertDialogBuilder
                .setTitle("Add a store")
                .setMessage("Add your store's name and identifier. An identifier can be an address or store number.")
                .setCancelable(true)
                .setPositiveButton("ADD", null)
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
        final AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button okBTN = ((AlertDialog)dialog).getButton(alertDialog.BUTTON_POSITIVE);
                okBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String name = storeNameET.getText().toString();
                        String storeId = storeIDET.getText().toString();

                        if(!name.isEmpty() && !storeId.isEmpty()){
                            String[] body = new String[] {"/stores", "{\"name\":\""+name+"\",\"storeId\":\""+storeId+"\"}"};

                            if(mInterface != null){
                                mInterface.storeWasAdded(body);
                            }

                            alertDialog.dismiss();
                        }

                        if(name.isEmpty()){
                            storeNameET.setError("Please do not leave this blank");
                        }

                        if(storeId.isEmpty()){
                            storeIDET.setError("please don't leave this blank");
                        }
                    }
                });
            }
        });

        alertDialog.show();
    }

    ListView.OnItemClickListener itemWasSelected = new ListView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            for (int i = 0; i < mStores.size(); i++) {
                Log.i("===== PRODO =====", "========== \n itemSELECTED: " + mStores.get(i).get_id() + " \n ==========");
            }

            if (mInterface != null) {
                mInterface.storeWasSelected(mStores.get(position).get_id(), position);
            }
        }
    };
}
