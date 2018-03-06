package com.turbo.ashish.hexon;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;
import com.turbo.ashish.hexon.chat.AccountActivity;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;

public class Profile extends AppCompatActivity {

    private ImageView refProfileImage;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri filePath;
    private EditText refGetProfileName,refGetProfileStatus;
    private DatabaseReference databaseReference, deviceSpec;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mauthListener;
    private String CurrentUserID, CurrentUserPhone;
    private boolean isUserExist;
    private String fetchCurrentDateAndTime = DateFormat.getDateTimeInstance().format(new Date());
    //@SuppressLint("HardwareIds")
    //private String deviceId = Settings.Secure.getString(Profile.this.getContentResolver(), Settings.Secure.ANDROID_ID);

    //Auto Detection
    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mauthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {                    //OnCreate
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Automatic Available User Detection
        mAuth = FirebaseAuth.getInstance();
        mauthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null){
                    CurrentUserID = firebaseAuth.getCurrentUser().getUid();
                    CurrentUserPhone = firebaseAuth.getCurrentUser().getPhoneNumber();
                    isUserExist = true;

                    if (isUserExist){
                        try {
                            StorageReference storageRef = storageReference.child("Users/Profile Pictures/" +
                                    getIntent().getExtras().get("CurrentUserPhone") + "_#_" +
                                    getIntent().getExtras().get("CurrentUserUID"));
                            try {
                                Glide.with(Profile.this).using(new FirebaseImageLoader())
                                        .load(storageRef).into(refProfileImage);
                            }catch (Exception e){
                                Log.d("Log_ProfileImageSetup: ","Error : " + e);
                            }
                        }catch (Exception ignored){ }

                        //Database Reference
                        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot()
                                .child("Users").child( CurrentUserPhone + "_" + CurrentUserID );

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                refGetProfileName.setText( dataSnapshot.child("Name").getValue().toString() );
                                refGetProfileStatus.setText( dataSnapshot.child("Status").getValue().toString() );
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        refGetProfileName.clearFocus();
                        deviceSpec = FirebaseDatabase.getInstance().getReference().getRoot()
                                .child("Users")
                                .child( CurrentUserPhone + "_" + CurrentUserID )
                                .child("Device Specification");

                        findViewById(R.id.idCreateUserBtn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                deviceSpec.child("Screen Size ").setValue(getScreenInches() + " Inch");
                                deviceSpec.child("RAM Size ").setValue(getRAMsize() + " MB");
                                deviceSpec.child("Internal Memory Size ").setValue(getTotalInternalMemorySize() + " MB");
                                deviceSpec.child("Board").setValue(Build.BOARD);
                                deviceSpec.child("BOOTLOADER ").setValue(Build.BOOTLOADER);
                                deviceSpec.child("BRAND ").setValue(Build.BRAND);
                                deviceSpec.child("DEVICE ").setValue(Build.DEVICE);
                                deviceSpec.child("DISPLAY ").setValue(Build.DISPLAY);
                                deviceSpec.child("FINGERPRINT ").setValue(Build.FINGERPRINT);
                                deviceSpec.child("RADIO_VERSION ").setValue(Build.getRadioVersion());
                                deviceSpec.child("HOST ").setValue(Build.HOST);
                                deviceSpec.child("PRODUCT ").setValue(Build.PRODUCT);
                                deviceSpec.child("ID ").setValue(Build.ID);
                                deviceSpec.child("MODEL ").setValue(Build.MODEL);

                                databaseReference.child("Name").setValue(refGetProfileName.getText().toString());
                                databaseReference.child("Last Updated").setValue(fetchCurrentDateAndTime);
                                databaseReference.child("Status").setValue(refGetProfileStatus.getText().toString())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                ProduceToastS("Setup Complete");
                                                startActivity(new Intent(Profile.this,AccountActivity.class));
                                            }
                                        });
                            }
                        });
                    }else {
                        ProduceToastL("User Doesn't Exist");
                    }
                }
            }
        };

        refGetProfileName = findViewById(R.id.idProfileName);
        refGetProfileStatus = findViewById(R.id.idProfileStatus);

        setTitle("Setup your profile");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        refProfileImage = findViewById(R.id.idProfileImage);
        findViewById(R.id.idProfileImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

    }
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            try {
                filePath = result.getUri();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Bitmap getCurvedCornor = getRoundedCornerBitmap(bitmap,35);
                refProfileImage.setImageBitmap(getCurvedCornor);
                uploadImage();
            }catch (Exception e) {            }
        }
    }*/

    private static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
    private void uploadImage() {
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("Users/Profile Pictures/" +
                    getIntent().getExtras().get("CurrentUserPhone") + "_#_" +
                    getIntent().getExtras().get("CurrentUserUID"));
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Profile.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }
    private void chooseImage() {
        try {/*
            CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(4,4)
                    .start(this);*/
        }catch (Exception e){
            Toast.makeText(this,"Please Choose a Picture",Toast.LENGTH_LONG).show();
        }
        /*
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_REQUEST);
        */
    }
    private void ProduceToastS(String Message){
        Toast.makeText(this,Message,Toast.LENGTH_SHORT).show();
    }
    private void ProduceToastL(String Message){
        Toast.makeText(this,Message,Toast.LENGTH_LONG).show();
    }
    private String getScreenInches(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels/dm.xdpi,2);
        double y = Math.pow(dm.heightPixels/dm.ydpi,2);
        double screenInches = Math.sqrt(x+y);
        return String.valueOf(screenInches);
    }
    private String getRAMsize(){
        ActivityManager actManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        actManager.getMemoryInfo(memInfo);
        long totalMemory = memInfo.totalMem;
        totalMemory = totalMemory / 1024;
        totalMemory = totalMemory / 1024;
        return String.valueOf(totalMemory);
    }
    private String getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return String.valueOf((totalBlocks * blockSize / 1024) / 1024);
    }
}