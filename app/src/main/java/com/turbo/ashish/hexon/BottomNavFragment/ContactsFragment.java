package com.turbo.ashish.hexon.BottomNavFragment;


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roger.catloadinglibrary.CatLoadingView;
import com.turbo.ashish.hexon.R;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static com.turbo.ashish.hexon.PhoneLogin.REQUEST_ID_MULTIPLE_PERMISSIONS;

public class ContactsFragment extends Fragment {

    public ContactsFragment() {
        // Required empty public constructor
    }

    private View view;
    private String ContactsList = null;
    private ListView refContactsListView;
    ArrayList<String> ContactsArray;


    @Override                                                                                       //OnCreate
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contacts, container, false);
        try {
            ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS);    //Peremission
            LoadContacts();                                                                         //LoadContacts
        }catch (SecurityException exceptionDetails){
            Log.d("SecurityException", String.valueOf(exceptionDetails));
        }
        //Set Contacts To ListView
        String[] work = {"Work 1","Work 2","Work 3","Work 4"};
        refContactsListView = view.findViewById(R.id.idContactsListView);
        ArrayAdapter<String> ContactsAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,ContactsArray);
        refContactsListView.setAdapter((ListAdapter) ContactsAdapter);
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        // Mark this fragment as the selected Fragment.
    }

    private void LoadContacts(){
        ContactsArray = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        ContentResolver resolver = getActivity().getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0){
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    Cursor cursor2 = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (cursor2.moveToNext()) {
                        String phonenumber = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        builder.append("Contact : ").append(name).append(", Phone Number : ").append(phonenumber).append("\n");
                        Log.d(name,phonenumber);
                        ContactsArray.add(name + " : " + phonenumber);
                    }
                    cursor2.close();
                }
            }
        }if (cursor != null) cursor.close();
        ContactsList = builder.toString();
    }
    private void exitApplication(){
        getActivity().finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
