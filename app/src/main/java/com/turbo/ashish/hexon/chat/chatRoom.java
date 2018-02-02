package com.turbo.ashish.hexon.chat;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.turbo.ashish.hexon.R;

import java.util.HashMap;
import java.util.Map;
import android.support.v7.app.ActionBar;

public class chatRoom extends AppCompatActivity {

    DatabaseReference rootRoomName;
    String roomName, userName;
    TextView receviedMsg;
    EditText sendMsg;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        receviedMsg = findViewById(R.id.idReceviedMsg);
        sendMsg = findViewById(R.id.idTypeMsg);
        scrollView = findViewById(R.id.idChatScrollView);

        scrollView.fullScroll(View.FOCUS_DOWN);

        roomName = getIntent().getExtras().get("Room_Name").toString();
        userName = getIntent().getExtras().get("User_Name").toString();

        setTitle(roomName);

        rootRoomName = FirebaseDatabase.getInstance().getReference().getRoot().child(roomName);

        findViewById(R.id.idSendMsgBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference childRoot = rootRoomName.push();
                Map<String,Object> map = new HashMap<>();
                map.put("name", userName);
                map.put("message", sendMsg.getText().toString());
                childRoot.updateChildren(map);
            }
        });

        rootRoomName.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                update_message(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                update_message(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private String chatUserName, chatMessage;
    //Load Message
    private void update_message(DataSnapshot dataSnapshot) {
        chatUserName = (String) dataSnapshot.child("name").getValue();
        chatMessage = (String) dataSnapshot.child("message").getValue();
        receviedMsg.append(chatUserName + "\n" + chatMessage + "\n\n");
        sendMsg.setText("");
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}
