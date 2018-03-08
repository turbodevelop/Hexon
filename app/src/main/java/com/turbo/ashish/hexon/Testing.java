package com.turbo.ashish.hexon;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.turbo.ashish.hexon.DragonEncryption.MessageEncrypt.AES;

public class Testing extends AppCompatActivity {

    private void exitApplication(){
        this.finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    //AES encryption = new AES();
    AES encryption = new AES();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        getSupportActionBar().hide();

        final String text = "hiDear Ok";
        final String key = "ashish485wedsasd";

        findViewById(R.id.idClick).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Testing.this,Platform.class));

            }
        });
    }


}
