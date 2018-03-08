package com.turbo.ashish.hexon;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.FrameLayout;

import com.turbo.ashish.hexon.BottomNavFragment.ContactsFragment;
import com.turbo.ashish.hexon.BottomNavFragment.FeedskFragment;
import com.turbo.ashish.hexon.BottomNavFragment.GroupsFragment;
import com.turbo.ashish.hexon.BottomNavFragment.ProfileFragment;


public class Platform extends AppCompatActivity {

    private ActionBar toolbar;
    private FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platform);

        fragmentContainer = findViewById(R.id.idFrameContainer);
        getSupportActionBar().hide();

        toolbar = getSupportActionBar();
        BottomNavigationView navigation = findViewById(R.id.idBottomNav);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar.setTitle("Feeds");


    }
    private void inside(){
        ProfileFragment profileFragment = new ProfileFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_right,R.anim.enter_from_right,R.anim.exit_to_right);
        fragmentTransaction.addToBackStack(null);
        //fragmentTransaction.add(R.id.idFragmentContainer, profileFragment).commit();
    }
    private void setFragment(Fragment f){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
        ft.replace(R.id.idFrameContainer, f);
        ft.commit();
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
            }
            return false;
        }
    };
}
