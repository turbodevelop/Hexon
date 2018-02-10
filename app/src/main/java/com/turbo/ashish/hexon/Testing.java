package com.turbo.ashish.hexon;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.turbo.ashish.hexon.DragonEncryption.MessageEncryption.AES;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

public class Testing extends AppCompatActivity {

    DragonEncryption.MessageEncryption.AES MsgEncAES = new DragonEncryption.MessageEncryption.AES();
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
    private void exitApplication(){
        this.finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        getSupportActionBar().hide();

        findViewById(R.id.idClick).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                        .start(Testing.this);
            }
        });
    }


}
