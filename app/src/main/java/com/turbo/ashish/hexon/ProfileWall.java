package com.turbo.ashish.hexon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;

public class ProfileWall extends AppCompatActivity {

    private ImageView profileImage;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mauthListener;
    private FirebaseStorage storage;
    private Uri filePath;
    String CurrentUserID, CurrentUserPhone;

    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mauthListener);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_wall);

        setProfileImage();  // SetProfileImage
        setupStatusBarColor();  //Statusbar Color

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.profileColPrimaryDark)));
        getSupportActionBar().hide();

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
                profileImage.setImageBitmap(getCurvedCornor);
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
    private void setProfileImage(){
        profileImage = findViewById(R.id.idImageViewCollapsingToolbarLayout);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mAuth = FirebaseAuth.getInstance();
        mauthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null){
                    CurrentUserID = firebaseAuth.getCurrentUser().getUid();
                    CurrentUserPhone = firebaseAuth.getCurrentUser().getPhoneNumber();

                    try {
                        StorageReference storageRef = storageReference.child("Users/Profile Pictures/" +
                                CurrentUserPhone + "_#_" +
                                CurrentUserID);
                        Glide.with(ProfileWall.this).using(new FirebaseImageLoader())
                                .load(storageRef).into(profileImage);
                    }catch (Exception e){
                        Log.d("LOG_ProfileWall_Pimg",String.valueOf(e));
                    }
                }
            }
        };
        //Upload New Image
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

    }
    private void uploadImage() {
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("Users/Profile Pictures/" +
                    CurrentUserPhone + "_#_" +
                    CurrentUserID);
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
                            Toast.makeText(ProfileWall.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
                    .setAspectRatio(6,4)
                    .start(this);*/
        }catch (Exception e){
            Toast.makeText(this,"Please Choose a Picture",Toast.LENGTH_LONG).show();
        }
    }
    private void setupStatusBarColor(){
        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.profileColPrimary));
    }

}
