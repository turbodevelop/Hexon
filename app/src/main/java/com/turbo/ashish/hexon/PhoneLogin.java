package com.turbo.ashish.hexon;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.roger.catloadinglibrary.CatLoadingView;


import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class PhoneLogin extends AppCompatActivity {

    private EditText refGetNumber;
    private String phoneVerificationID;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallback;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private FirebaseAuth firebaseAuth;
    private CatLoadingView CLV;
    private String combineOTP;
    //Functions
    private void setupVerificationCallback(){
        CLV.show(getSupportFragmentManager(),"");
        verificationCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }
            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException){
                    //Invalid Request
                    Log.d("InvalidRequest", "Invalid Credential : " + e.getLocalizedMessage());
                }else if (e instanceof FirebaseTooManyRequestsException){
                    Log.d("TooMuchRequest", "SMS Quota Exceeded");
                }
            }
            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token){
                Toast.makeText(PhoneLogin.this,"OTP Sent",Toast.LENGTH_SHORT).show();
                CLV.dismiss();
                phoneVerificationID = verificationId;
                resendToken = token;
            }
        };
    }
    private void signinWithPhoneAuthCredential(PhoneAuthCredential credential){
        CLV.show(getSupportFragmentManager(),"");
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(PhoneLogin.this,"OTP Verified",Toast.LENGTH_SHORT).show();
                }else {
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                        Toast.makeText(PhoneLogin.this,"Wrong OTP",Toast.LENGTH_SHORT).show();
                    }
                }CLV.dismiss();
            }
        });
    }
    private String getMacAddress() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        String macAddress = wInfo.getMacAddress();
        return macAddress;


    }
    private void requestOTPslider(){
        DialogPlus dialog = DialogPlus.newDialog(this)
                .setContentHolder(new ViewHolder(R.layout.custom_otp_dialog))
                .setContentHeight(150)
                .setPadding(100,70,0,-30)
                .setMargin(18,0,18,0)
                .setExpanded(true).setGravity(Gravity.TOP)
                .create();
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        CLV = new CatLoadingView();
        CLV.setCancelable(false);
        Log.d("Log-1",getMacAddress()); // Get MacAddress
        refGetNumber = findViewById(R.id.idGetNumber);
        firebaseAuth = FirebaseAuth.getInstance();

        final EditText refOTP1 = findViewById(R.id.idOTP1), refOTP2 = findViewById(R.id.idOTP2),
                refOTP3 = findViewById(R.id.idOTP3), refOTP4 = findViewById(R.id.idOTP4),
                refOTP5  = findViewById(R.id.idOTP5), refOTP6  = findViewById(R.id.idOTP6);

        findViewById(R.id.idOTPBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = refGetNumber.getText().toString();
                if (TextUtils.isEmpty(phoneNumber)){
                    Toast.makeText(PhoneLogin.this,"Enter Phone Number",Toast.LENGTH_SHORT).show();
                }else {
                    setupVerificationCallback();
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60,
                            TimeUnit.SECONDS, PhoneLogin.this, verificationCallback);
                    refOTP1.requestFocus();
                }
            }
        });



        refOTP1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                refOTP2.requestFocus();
                refOTP2.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        refOTP3.requestFocus();
                        refOTP3.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }
                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                refOTP4.requestFocus();
                                refOTP4.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                    }
                                    @Override
                                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                        refOTP5.requestFocus();
                                        refOTP5.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                            }
                                            @Override
                                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                                refOTP6.requestFocus();
                                                refOTP6.addTextChangedListener(new TextWatcher() {
                                                    @Override
                                                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                                    }
                                                    @Override
                                                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                                        refOTP1.requestFocus();
                                                        combineOTP = refOTP1.getText().toString() + refOTP2.getText().toString() +
                                                                refOTP3.getText().toString() + refOTP4.getText().toString() +
                                                                refOTP5.getText().toString() + refOTP6.getText().toString();
                                                        if (combineOTP.length() == 6){
                                                            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneVerificationID, combineOTP);
                                                            signinWithPhoneAuthCredential(credential);
                                                        }else {
                                                            Toast.makeText(PhoneLogin.this,"Please Perform Exactly",Toast.LENGTH_SHORT).show();
                                                        }
                                                    }@Override
                                                    public void afterTextChanged(Editable editable) {
                                                    }
                                                });
                                            }@Override
                                            public void afterTextChanged(Editable editable) {

                                            }
                                        });
                                    }@Override
                                    public void afterTextChanged(Editable editable) {

                                    }
                                });
                            }@Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });
                    }@Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }@Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }
}