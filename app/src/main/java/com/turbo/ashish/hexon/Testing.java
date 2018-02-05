package com.turbo.ashish.hexon;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

public class Testing extends AppCompatActivity {

    CountDownTimer countDownTimer;
    TextView tvTime;
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
        setContentView(R.layout.activity_testing);

        tvTime = findViewById(R.id.textView5);
        findViewById(R.id.idClick).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                requestOTPslider();

            }
        });
    }

}
