package com.turbo.ashish.hexon;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;

import com.roger.catloadinglibrary.CatLoadingView;
import com.turbo.ashish.hexon.chat.AccountActivity;
import com.turbo.ashish.hexon.chat.chatRoom;


import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PhoneLogin extends AppCompatActivity {

    private EditText refGetNumber;
    private String phoneVerificationID;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallback;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private FirebaseAuth firebaseAuth;
    private CatLoadingView CLV;
    private String combineOTP;
    private String[] countryCodeandname = new String[]{"93 ", " Afghanistan" , "355 ", " Albania" , "213 ", " Algeria" , "684 ", " American Samoa" , "376 ", " Andorra" , "244 ", " Angola" , "809 ", " Anguilla" , "268 ", " Antigua" , "54 ", " Argentina" , "374 ", " Armenia" , "297 ", " Aruba" , "247 ", " Ascension Island" , "61 ", " Australia" , "672 ", " Australian External Territories" , "43 ", " Austria" , "994 ", " Azerbaijan" , "242 ", " Bahamas" , "246 ", " Barbados" , "973 ", " Bahrain" , "880 ", " Bangladesh" , "375 ", " Belarus" , "32 ", " Belgium" , "501 ", " Belize" , "229 ", " Benin" , "809 ", " Bermuda" , "975 ", " Bhutan" , "284 ", " British Virgin Islands" , "591 ", " Bolivia" , "387 ", " Bosnia and Hercegovina" , "267 ", " Botswana" , "55 ", " Brazil" , "284 ", " British V.I." , "673 ", " Brunei Darussalm" , "359 ", " Bulgaria" , "226 ", " Burkina Faso" , "257 ", " Burundi" , "855 ", " Cambodia" , "237 ", " Cameroon" , "1 ", " Canada" , "238 ", " CapeVerde Islands" , "1 ", " Caribbean Nations" , "345 ", " Cayman Islands" , "238 ", " Cape Verdi" , "236 ", " Central African Republic" , "235 ", " Chad" , "56 ", " Chile" , "86 ", " China (People's Republic)" , "886 ", " China-Taiwan" , "57 ", " Colombia" , "269 ", " Comoros and Mayotte" , "242 ", " Congo" , "682 ", " Cook Islands" , "506 ", " Costa Rica" , "385 ", " Croatia" , "53 ", " Cuba" , "357 ", " Cyprus" , "420 ", " Czech Republic" , "45 ", " Denmark" , "246 ", " Diego Garcia" , "767 ", " Dominca" , "809 ", " Dominican Republic" , "253 ", " Djibouti" , "593 ", " Ecuador" , "20 ", " Egypt" , "503 ", " El Salvador" , "240 ", " Equatorial Guinea" , "291 ", " Eritrea" , "372 ", " Estonia" , "251 ", " Ethiopia" , "500 ", " Falkland Islands" , "298 ", " Faroe (Faeroe) Islands (Denmark)" , "679 ", " Fiji" , "358 ", " Finland" , "33 ", " France" , "596 ", " French Antilles" , "594 ", " French Guiana" , "241 ", " Gabon (Gabonese Republic)" , "220 ", " Gambia" , "995 ", " Georgia" , "49 ", " Germany" , "233 ", " Ghana" , "350 ", " Gibraltar" , "30 ", " Greece" , "299 ", " Greenland" , "473 ", " Grenada/Carricou" , "671 ", " Guam" , "502 ", " Guatemala" , "224 ", " Guinea" , "245 ", " Guinea-Bissau" , "592 ", " Guyana" , "509 ", " Haiti" , "504 ", " Honduras" , "852 ", " Hong Kong" , "36 ", " Hungary" , "354 ", " Iceland" , "91 ", " India" , "62 ", " Indonesia" , "98 ", " Iran" , "964 ", " Iraq" , "353 ", " Ireland (Irish Republic; Eire)" , "972 ", " Israel" , "39 ", " Italy" , "225 ", " Ivory Coast (La Cote d'Ivoire)" , "876 ", " Jamaica" , "81 ", " Japan" , "962 ", " Jordan" , "7 ", " Kazakhstan" , "254 ", " Kenya" , "855 ", " Khmer Republic (Cambodia/Kampuchea)" , "686 ", " Kiribati Republic (Gilbert Islands)" , "82 ", " Korea, Republic of (South Korea)" , "850 ", " Korea, People's Republic of (North Korea)" , "965 ", " Kuwait" , "996 ", " Kyrgyz Republic" , "371 ", " Latvia" , "856 ", " Laos" , "961 ", " Lebanon" , "266 ", " Lesotho" , "231 ", " Liberia" , "370 ", " Lithuania" ,"218 ", " Libya" , "423 ", " Liechtenstein" , "352 ", " Luxembourg" , "853 ", " Macao" , "389 ", " Macedonia" , "261 ", " Madagascar" , "265 ", " Malawi" , "60 ", " Malaysia" , "960 ", " Maldives" , "223 ", " Mali" , "356 ", " Malta" , "692 ", " Marshall Islands" , "596 ", " Martinique (French Antilles)" , "222 ", " Mauritania" , "230 ", " Mauritius" , "269 ", " Mayolte" , "52 ", " Mexico" , "691 ", " Micronesia (F.S. of Polynesia)" , "373 ", " Moldova" , "33 ", " Monaco" , "976 ", " Mongolia" , "473 ", " Montserrat" , "212 ", " Morocco" , "258 ", " Mozambique" , "95 ", " Myanmar (former Burma)" , "264 ", " Namibia (former South-West Africa)" , "674 ", " Nauru" , "977 ", " Nepal" , "31 ", " Netherlands" , "599 ", " Netherlands Antilles" , "869 ", " Nevis" , "687 ", " New Caledonia" , "64 ", " New Zealand" , "505 ", " Nicaragua" , "227 ", " Niger" , "234 ", " Nigeria" , "683 ", " Niue" , "850 ", " North Korea" , "1 670 ", " North Mariana Islands (Saipan)" , "47 ", " Norway" , "968 ", " Oman" , "92 ", " Pakistan" , "680 ", " Palau" , "507 ", " Panama" , "675 ", " Papua New Guinea" , "595 ", " Paraguay" , "51 ", " Peru" , "63 ", " Philippines" , "48 ", " Poland" , "351 ", " Portugal (includes Azores)" , "1 787 ", " Puerto Rico" , "974 ", " Qatar" , "262 ", " Reunion (France)" , "40 ", " Romania" , "7 ", " Russia" , "250 ", " Rwanda (Rwandese Republic)" , "670 ", " Saipan" , "378 ", " San Marino" , "239 ", " Sao Tome and Principe" , "966 ", " Saudi Arabia" , "221 ", " Senegal" , "381 ", " Serbia and Montenegro" , "248 ", " Seychelles" , "232 ", " Sierra Leone" , "65 ", " Singapore" , "421 ", " Slovakia" , "386 ", " Slovenia" , "677 ", " Solomon Islands" , "252 ", " Somalia" , "27 ", " South Africa" , "34 ", " Spain" , "94 ", " Sri Lanka" , "290 ", " St. Helena" , "869 ", " St. Kitts/Nevis" , "508 ", " St. Pierre &(et) Miquelon (France)" , "249 ", " Sudan" , "597 ", " Suriname" , "268 ", " Swaziland" , "46 ", " Sweden" , "41 ", " Switzerland" , "963 ", " Syrian Arab Republic (Syria)" , "689 ", " Tahiti (French Polynesia)" , "886 ", " Taiwan" , "7 ", " Tajikistan" , "255 ", " Tanzania (includes Zanzibar)" , "66 ", " Thailand" , "228 ", " Togo (Togolese Republic)" , "690 ", " Tokelau" , "676 ", " Tonga" , "1 868 ", " Trinidad and Tobago" , "216 ", " Tunisia" , "90 ", " Turkey" , "993 ", " Turkmenistan" , "688 ", " Tuvalu (Ellice Islands)" , "256 ", " Uganda" , "380 ", " Ukraine" , "971 ", " United Arab Emirates" , "44 ", " United Kingdom" , "598 ", " Uruguay" , "1 ", " USA" , "7 ", " Uzbekistan" , "678 ", " Vanuatu (New Hebrides)" , "39 ", " Vatican City" , "58 ", " Venezuela" , "84 ", " Viet Nam" , "1 340 ", " Virgin Islands" , "681 ", " Wallis and Futuna" , "685 ", " Western Samoa" , "381 ", " Yemen (People's Democratic Republic of)" , "967 ", " Yemen Arab Republic (North Yemen)" , "381 ", " Yugoslavia (discontinued)" , "243 ", " Zaire" , "260 ", " Zambia" , "263 ", " Zimbabwe"};
    private String[] CName = new String[countryCodeandname.length/2];
    private String[] CDail = new String[countryCodeandname.length/2];
    private Button refChooseCountryBtn;
    private int tmp1 = -1, tmp2 = -1;
    private TextView refCountryCodeShow;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private String getBroadcastSMS_OTP = "";
    private TextView refOutOTPtv;
    private boolean autoOtpswitchStatus;
    private EditText refOTP1, refOTP2, refOTP3, refOTP4, refOTP5, refOTP6;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mauthListener;

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
        @SuppressLint("HardwareIds") String macAddress = wInfo.getMacAddress();
        return macAddress;


    }
    private void separeteCountryAmbiguty(){
        for (int i=0; i<countryCodeandname.length;i++){
            if (i%2 == 0){
                tmp1++;
                CDail[tmp1] = countryCodeandname[i];
            }else {
                tmp2++;
                CName[tmp2] = countryCodeandname[i];
            }
        }
    }
    private boolean checkAndRequestPermissions(){
        int permissionSendMessage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS);
        int receveSMS = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS);
        int readSMS = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (receveSMS != PackageManager.PERMISSION_GRANTED){
            listPermissionsNeeded.add(android.Manifest.permission.RECEIVE_SMS);
        }
        if(readSMS != PackageManager.PERMISSION_GRANTED){
            listPermissionsNeeded.add(android.Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()){
            ActivityCompat.requestPermissions(this,listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
        }
        return true;
    }

    //Automatic Login
    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mauthListener);
    }

    @Override                                                   //Main
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        getSupportActionBar().hide();

        //Automatic Login Detection
        mauthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(PhoneLogin.this,Platform.class));
                }else {
                    //startActivity(new Intent(PhoneLogin.this,Profile.class));
                }
            }
        };
        mAuth = FirebaseAuth.getInstance();

        if (checkAndRequestPermissions()) {
        }


        setTitle("Verify your Phone number"); //Set Title Of Activity
        CLV = new CatLoadingView();
        CLV.setCancelable(false);
        Log.d("Log-1",getMacAddress()); // Get MacAddress
        refGetNumber = findViewById(R.id.idGetNumber);
        firebaseAuth = FirebaseAuth.getInstance();

        refOTP1 = findViewById(R.id.idOTP1); refOTP2 = findViewById(R.id.idOTP2);
        refOTP3 = findViewById(R.id.idOTP3); refOTP4 = findViewById(R.id.idOTP4);
        refOTP5  = findViewById(R.id.idOTP5); refOTP6  = findViewById(R.id.idOTP6);

        findViewById(R.id.idOTPBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = refCountryCodeShow.getText().toString() + refGetNumber.getText().toString();
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
        separeteCountryAmbiguty();
        refCountryCodeShow = findViewById(R.id.idCcodeView);
        refChooseCountryBtn = findViewById(R.id.idChooseCountryBtn);
        refChooseCountryBtn.setText(CName[95]);
        refCountryCodeShow.setText("+" + CDail[95]);
        refChooseCountryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PhoneLogin.this);
                builder.setTitle("Choose Country");
                builder.setItems(CName , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        refChooseCountryBtn.setText(CName[i]);
                        refCountryCodeShow.setText("+" + CDail[i]);
                    }
                });
                builder.show();
                //chooseCountryDialog();
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
        refOutOTPtv = (TextView) findViewById(R.id.textView4);

        final Switch refautoOtpSwitch = findViewById(R.id.idAutoOTPswitch);
        refautoOtpSwitch.setTextColor(Color.RED);
        refautoOtpSwitch.setChecked(true);
        if (refautoOtpSwitch.isChecked()){
            refautoOtpSwitch.setText("Auto OTP Detection");
            refautoOtpSwitch.setTextColor(Color.BLACK);
            autoOtpswitchStatus = true;
        }
        refautoOtpSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (refautoOtpSwitch.isChecked()){
                    refautoOtpSwitch.setText("Auto OTP Detection");
                    refautoOtpSwitch.setTextColor(Color.BLACK);
                    autoOtpswitchStatus = true;
                }else {
                    refautoOtpSwitch.setText("Auto OTP Detection Disabled");
                    refautoOtpSwitch.setTextColor(Color.RED);
                    refOutOTPtv.setText("");
                    autoOtpswitchStatus = false;
                }
            }
        });
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                char[] getOTP = message.toCharArray();
                for (int i=0; i<getOTP.length; i++){
                    if (getOTP[i] == '0' || getOTP[i] == '1' || getOTP[i] == '2' || getOTP[i] == '3' || getOTP[i] == '4' ||
                            getOTP[i] == '5' || getOTP[i] == '6' || getOTP[i] == '7' || getOTP[i] == '8' ||
                            getOTP[i] == '9' ){
                        getBroadcastSMS_OTP = getBroadcastSMS_OTP + getOTP[i];
                    }
                }
                if (autoOtpswitchStatus){
                    refOutOTPtv.setText("OTP Detected : " + getBroadcastSMS_OTP);
                    String tempOTP = getBroadcastSMS_OTP;
                    char[] setOTP = tempOTP.toCharArray();
                    refOTP1.setText(String.valueOf(setOTP[0]));
                    refOTP2.setText(String.valueOf(setOTP[1]));
                    refOTP3.setText(String.valueOf(setOTP[2]));
                    refOTP4.setText(String.valueOf(setOTP[3]));
                    refOTP5.setText(String.valueOf(setOTP[4]));
                    refOTP6.setText(String.valueOf(setOTP[5]));
                }else {

                }
            }
        }
    };
    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).
                registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }
}