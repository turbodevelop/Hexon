package com.turbo.ashish.hexon.chat;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.turbo.ashish.hexon.R;

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
    private String username = "Ashish";
    ListView roomList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        final EditText roomName = findViewById(R.id.idRoomEntry);
        roomList = findViewById(R.id.idRoomList);


        roomArrayList = new ArrayList<>();
        roomAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, roomArrayList);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //request_username();
        findViewById(R.id.idEntryBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map = new HashMap<>();
                map.put(roomName.getText().toString(),"");
                databaseReference.updateChildren(map);
            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator iterator = dataSnapshot.getChildren().iterator();
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