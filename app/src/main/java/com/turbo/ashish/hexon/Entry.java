package com.turbo.ashish.hexon;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.turbo.ashish.hexon.emailLogin.EmailAuth;

import junit.framework.Test;

import static android.content.ContentValues.TAG;

public class Entry extends AppCompatActivity {

    MyFirebaseIdServices mfis = new MyFirebaseIdServices();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        findViewById(R.id.idBtElog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Entry.this,EmailAuth.class));
            }
        });
        findViewById(R.id.idTestBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Entry.this, Testing.class));
            }
        });
        mfis.onTokenRefresh(); //Token Specifier

        findViewById(R.id.idRegBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Entry.this,Signup.class));
            }
        });
    }
}
class MyFirebaseIdServices extends FirebaseInstanceIdService {
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Getting Tocken", "Refreshed token: " + refreshedToken);
        //sendRegistrationToServer(refreshedToken);
    }
}