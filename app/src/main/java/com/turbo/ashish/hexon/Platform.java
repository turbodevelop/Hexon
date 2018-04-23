package com.turbo.ashish.hexon;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.turbo.ashish.hexon.BottomNavFragment.ContactsFragment;
import com.turbo.ashish.hexon.BottomNavFragment.FavouritesFragment;
import com.turbo.ashish.hexon.BottomNavFragment.FeedskFragment;
import com.turbo.ashish.hexon.BottomNavFragment.GroupsFragment;
import com.turbo.ashish.hexon.BottomNavFragment.ProfileFragment;

import java.util.ArrayList;

public class Platform extends AppCompatActivity {

    private ActionBar toolbar;
    protected ArrayList<String> allContacts;                                                        //AllContactsArrayList
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mauthListener;
    public static String CurrentUserID, CurrentUserPhone;
    //Auto Detection
    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mauthListener);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platform);

        //Automatic Available User Detection
        mAuth = FirebaseAuth.getInstance();
        mauthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null){
                    CurrentUserID = firebaseAuth.getCurrentUser().getUid();
                    CurrentUserPhone = firebaseAuth.getCurrentUser().getPhoneNumber();
                }
            }
        };

//        FrameLayout fragmentContainer = findViewById(R.id.idFrameContainer);
        getSupportActionBar().hide();
        toolbar = getSupportActionBar();
        BottomNavigationView navigation = findViewById(R.id.idBottomNav);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        toolbar.setTitle("Feeds");
//

    }
    private void getContactList() {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.i("Delta", "Name: " + name);
                        Log.i("Delta", "Phone Number: " + phoneNo);
                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }

    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @SuppressLint("ResourceType")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();

            switch (item.getItemId()){
                case R.id.idNavContacts:
                    toolbar.setTitle("Contacts");
                    ft.replace(R.id.idFrameContainer, new ContactsFragment(), "Contacts Ftagment").commit();
                    return true;

                case R.id.idNavPerson:
                    toolbar.setTitle("Profile");
                    ft.replace(R.id.idFrameContainer,new ProfileFragment(), "Profile Fragment").commit();
                    return true;

                case R.id.idNavGroups:

                    toolbar.setTitle("Groups");
                    ft.replace(R.id.idFrameContainer,new GroupsFragment(), "Groups Fragment").commit();
                    return true;

                case R.id.idNavFeeds:
                    toolbar.setTitle("Feeds");
                    ft.replace(R.id.idFrameContainer, new FeedskFragment(), "Feeds Fragment").commit();
                    return true;

                case R.id.idNavFavourites:
                    toolbar.setTitle("Favourites");
                    ft.replace(R.id.idFrameContainer, new FavouritesFragment(), "Favourite Fragment").commit();
                    return true;
            }
            return false;
        }
    };
}
