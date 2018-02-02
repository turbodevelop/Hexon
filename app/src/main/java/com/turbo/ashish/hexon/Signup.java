package com.turbo.ashish.hexon;

import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.turbo.ashish.hexon.emailLogin.EmailAuth;

public class Signup extends AppCompatActivity {

    private EditText refEmail, refPsw;
    private FirebaseAuth auth;
    private CatLoadingView CLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        CLV = new CatLoadingView(); CLV.setCancelable(false); //Cat Loading
         auth = FirebaseAuth.getInstance();

        refEmail = findViewById(R.id.idRegEmail);
        refPsw = findViewById(R.id.idRegPsw);

        findViewById(R.id.idRegCnfBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getEmail = refEmail.getText().toString().trim();
                String getPsw = refPsw.getText().toString().trim();

                if (TextUtils.isEmpty(getEmail) || TextUtils.isEmpty(getPsw)){
                    Toast.makeText(getApplicationContext(),"Enter Credentials",Toast.LENGTH_LONG).show();
                }else {
                    if (getPsw.length() < 8){
                        Toast.makeText(getApplicationContext(),"Passworrd Too Sort",Toast.LENGTH_SHORT).show();
                    }else {
                        CLV.show(getSupportFragmentManager(),"");
                        auth.createUserWithEmailAndPassword(getEmail,getPsw).addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(Signup.this,"createUserWithEmail:onComplete: " + task.isSuccessful(),Toast.LENGTH_SHORT).show();
                                if (!task.isSuccessful()){
                                    CLV.dismiss();
                                    Toast.makeText(Signup.this,"Registration Fail",Toast.LENGTH_SHORT).show();
                                }else {
                                    CLV.dismiss();
                                    startActivity(new Intent(Signup.this, EmailAuth.class));
                                    finish();
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}
