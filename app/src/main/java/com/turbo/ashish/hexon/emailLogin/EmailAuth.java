package com.turbo.ashish.hexon.emailLogin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.roger.catloadinglibrary.CatLoadingView;
import com.turbo.ashish.hexon.chat.AccountActivity;
import com.turbo.ashish.hexon.R;

import java.net.InetAddress;

public class EmailAuth extends AppCompatActivity {

    private EditText RgetEmail;
    private EditText RGetPsw;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private CatLoadingView catLoading;

    // Automatic Login Detection
 /*   @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
 */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_auth);

        catLoading = new CatLoadingView();
        catLoading.setCancelable(false);


        // Automatic Login Detection
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(EmailAuth.this,AccountActivity.class));
                }
            }
        };

        mAuth = FirebaseAuth.getInstance();
        RgetEmail = findViewById(R.id.idGetEmail);
        RGetPsw = findViewById(R.id.idGetPsw);
        findViewById(R.id.idBtnSignin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetworkConnection() == true){
                    SignIn();
                }else Snackbar.make(v,"No Network Connection",Snackbar.LENGTH_SHORT).show();
            }
        });
    }
    private void SignIn(){
        String email = RgetEmail.getText().toString();
        String psw = RGetPsw.getText().toString();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(psw)){
            Toast.makeText(EmailAuth.this,"Please Enter Credential",Toast.LENGTH_SHORT).show();
        } else {
            catLoading.show(getSupportFragmentManager(),"Loading..");
            mAuth.signInWithEmailAndPassword(email,psw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        catLoading.dismiss();
                        Toast.makeText(EmailAuth.this,"Signin Problem",Toast.LENGTH_SHORT).show();
                    } else {
                        catLoading.dismiss();
                        startActivity(new Intent(EmailAuth.this,AccountActivity.class));
                    }
                }
            });
        }
    }
    private boolean CheckNetworkConnection(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}