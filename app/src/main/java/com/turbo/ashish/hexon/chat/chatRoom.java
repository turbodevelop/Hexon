package com.turbo.ashish.hexon.chat;
import android.annotation.SuppressLint;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.turbo.ashish.hexon.DragonEncryption;
import com.turbo.ashish.hexon.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import android.support.v7.app.ActionBar;


public class chatRoom extends AppCompatActivity {

    private DatabaseReference rootRoomName;
    private String roomName, userName;
    private TextView receviedMsg;
    private EditText sendMsg;
    private ScrollView scrollView;
    private String EncryptionKey = "ashish485wedsasd";
    private DragonEncryption.MessageEncrypt.AES cryptoAES = new DragonEncryption.MessageEncrypt.AES();

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        //Log.d("Delta",cryptoAES.encrypt("12345678","Work"))

        receviedMsg = findViewById(R.id.idReceviedMsg);
        sendMsg = findViewById(R.id.idTypeMsg);
        scrollView = findViewById(R.id.idChatScrollView);

        scrollView.fullScroll(View.FOCUS_DOWN);

        roomName = getIntent().getExtras().get("Room_Name").toString();
        userName = getIntent().getExtras().get("User_Name").toString();

        setTitle(roomName);

        rootRoomName = FirebaseDatabase.getInstance().getReference().getRoot().child("Groups").child(roomName);

        findViewById(R.id.idSendMsgBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference childRoot = rootRoomName.push();
                Map<String,Object> map = new HashMap<>();
                map.put("name", userName);
                try {
                    map.put("message", cryptoAES.encrypt(EncryptionKey, sendMsg.getText().toString()));
                }catch (Exception e){

                }

                childRoot.updateChildren(map);
                scrollView.fullScroll(View.FOCUS_DOWN);
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
    private void voidSync(){

    }
    private void update_message(DataSnapshot dataSnapshot) {
        chatUserName = (String) dataSnapshot.child("name").getValue();
        chatMessage = (String) dataSnapshot.child("message").getValue();
        String decryptedMsg = null;
        try{
            decryptedMsg = cryptoAES.decrypt(EncryptionKey,chatMessage);
        }catch (Exception e){
            Log.d("Error_Decryption","");
        }

        receviedMsg.append(chatUserName + " : " + decryptedMsg + "\n");
        sendMsg.setText("");
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}
