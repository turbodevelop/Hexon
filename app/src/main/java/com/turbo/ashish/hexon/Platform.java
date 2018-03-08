package com.turbo.ashish.hexon;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.turbo.ashish.hexon.BottomNavFragment.FeedsFragment;
import com.turbo.ashish.hexon.BottomNavFragment.ContactsFragment;
import com.turbo.ashish.hexon.BottomNavFragment.GroupsFragment;
import com.turbo.ashish.hexon.BottomNavFragment.ProfileFragment;
import com.turbo.ashish.hexon.chat.AccountActivity;

public class Platform extends AppCompatActivity {

    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platform);

        toolbar = getSupportActionBar();
        BottomNavigationView navigation = findViewById(R.id.idBottomNav);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar.setTitle("Feeds");


    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()){
                case R.id.idNavContacts:
                    toolbar.setTitle("Contacts");

                    return true;
                case R.id.idNavPerson:
                    toolbar.setTitle("Profile");
                    return true;
                case R.id.idNavGroups:
                    toolbar.setTitle("Groups");
                    return true;
                case R.id.idNavFeeds:
                    toolbar.setTitle("Feeds");
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.idFrameContainer,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
