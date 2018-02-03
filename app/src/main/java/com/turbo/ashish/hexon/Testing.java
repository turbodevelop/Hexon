package com.turbo.ashish.hexon;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.DialogPlusBuilder;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;

public class Testing extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        getSupportActionBar().hide();


        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{"234","48"});
        findViewById(R.id.idClick).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                DialogPlus dialog = DialogPlus.newDialog(Testing.this)
                        .setContentHolder(new ViewHolder(R.layout.custom_otp_dialog))
                        .setContentHeight(150)
                        .setPadding(100,70,0,-30)
                        .setMargin(18,0,18,0)
                        .setExpanded(true).setGravity(Gravity.TOP)
                        .create();
                dialog.show();
            }
        });
    }
}
