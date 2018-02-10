package com.turbo.ashish.hexon.chat;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.turbo.ashish.hexon.R;
import com.turbo.ashish.hexon.Testing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class AccountActivity extends AppCompatActivity {

    ArrayList<String> roomArrayList;
    ArrayAdapter<String> roomAdapter;
    DatabaseReference databaseReference;
    private String username;
    ListView roomList;

    //Functions
    private void exitApplication(){
        this.finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void HideKeyboard(){
        View HideKeyBoardView = this.getCurrentFocus();
        if (HideKeyBoardView != null) {
            InputMethodManager iMM = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            assert iMM != null;
            iMM.hideSoftInputFromWindow(HideKeyBoardView.getWindowToken(), 0);
        }
    }

    //Exit Application On Back Pressed
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            exitApplication();
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        final EditText roomName = findViewById(R.id.idRoomEntry);
        roomList = findViewById(R.id.idRoomList);


        roomArrayList = new ArrayList<>();
        roomAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, roomArrayList);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        username = "Ashish";//(String) getIntent().getExtras().get("CurrentUserPhone");

        findViewById(R.id.idEntryBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map = new HashMap<>();
                map.put(roomName.getText().toString(), "");
                databaseReference.child("Groups").updateChildren(map);
                roomName.setText("");
                roomName.clearFocus();
                HideKeyboard();

            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Iterator iterator = dataSnapshot.getChildren().iterator();
                Iterator iterator = dataSnapshot.child("Groups").getChildren().iterator();
                Set<String> set = new HashSet<>();
                while (iterator.hasNext()){
                    set.add((String) ((DataSnapshot)iterator.next()).getKey());
                }
                roomArrayList.clear();
                roomArrayList.addAll(set);
                roomList.setAdapter(roomAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        roomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(AccountActivity.this,chatRoom.class);
                intent.putExtra("Room_Name", ((TextView)view).getText().toString());
                intent.putExtra("User_Name",username);
                startActivity(intent);
            }
        });
    }

    private void request_username() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter User Name");
        final EditText editText = new EditText(this);
        editText.setId(R.id.dialog_editext);
        builder.setView(editText);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                username = editText.getText().toString();
                if (!TextUtils.isEmpty(username)){
                }else request_username();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                request_username();
            }
        });
        builder.show();

    }
}