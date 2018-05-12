package com.turbo.ashish.hexon.chat;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.turbo.ashish.hexon.DragonEncryption;
import com.turbo.ashish.hexon.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class chatRoom extends AppCompatActivity {

    private DatabaseReference rootRoomName, FavReference;
    private String userName;
    private EditText sendMsg;
    private String EncryptionKey = "ashish485wedsasd";
    private DragonEncryption.MessageEncrypt.AES cryptoAES = new DragonEncryption.MessageEncrypt.AES();
    private List<String> ChatUser, ChatMsg, ChatTime;
    private ListView ChatListView;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        ChatUser = new ArrayList<>();
        ChatMsg = new ArrayList<>();
        ChatTime = new ArrayList<>();
        ChatListView = findViewById(R.id.idChatroomListView);

        //Log.d("Delta",cryptoAES.encrypt("12345678","Work"))
        sendMsg = findViewById(R.id.idTypeMsg);


        final String roomName = getIntent().getExtras().get("Room_Name").toString();
        userName = getIntent().getExtras().get("User_Name").toString();

        setTitle(roomName);

        rootRoomName = FirebaseDatabase.getInstance().getReference().getRoot().child("Groups").child(roomName);

        findViewById(R.id.idSendMsgBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference childRoot = rootRoomName.push();
                Map<String, Object> map = new HashMap<>();
                Calendar calendar = Calendar.getInstance();
                Log.d("Deta", String.valueOf(calendar.get(Calendar.SECOND)));
                map.put("name", userName);
                map.put("time", calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE));
                try {
                    map.put("message", cryptoAES.encrypt(EncryptionKey, sendMsg.getText().toString()));
                } catch (Exception e) {
                    //
                }
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
        ChatListView.setLongClickable(true);
        ChatListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String collectFavMsg = roomName + " : " + ChatUser.get(i) + " : " + ChatMsg.get(i) + " : "
                        + ChatTime.get(i);
                FavReference = FirebaseDatabase.getInstance().getReference().getRoot()
                        .child("FavouriteMessages").child(userName);
                DatabaseReference childRoot = FavReference.push();
                Map<String, Object> map = new HashMap<>();
                map.put("Favoutite_Message", collectFavMsg);
                childRoot.updateChildren(map);
                Toast.makeText(getApplicationContext(),"Added To Favourites",Toast.LENGTH_LONG).show();


                return true;
            }
        });


    }

    private void showAlert(){

    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return ChatUser.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @SuppressLint({"ViewHolder", "InflateParams"})
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.chat_list_row, null);
            TextView refUser = view.findViewById(R.id.idUser),
                    refMsg = view.findViewById(R.id.idMsg),
                    refTime = view.findViewById(R.id.idChatTime);
            refUser.setText(ChatUser.get(i));
            refMsg.setText(ChatMsg.get(i));
            refTime.setText(ChatTime.get(i));
            return view;
        }
    }

    private void update_message(DataSnapshot dataSnapshot) {
        CustomAdapter customAdapter = new CustomAdapter();
        String chatUserName = (String) dataSnapshot.child("name").getValue();
        String chatTime = (String) dataSnapshot.child("time").getValue();
        String chatMessage = (String) dataSnapshot.child("message").getValue();
        String decryptedMsg = null;
        try {
            decryptedMsg = cryptoAES.decrypt(EncryptionKey, chatMessage);
        } catch (Exception e) {
            Log.d("Error", "Error While Decryption");
        }

        ChatUser.add(chatUserName);
        ChatMsg.add(decryptedMsg);
        ChatTime.add(chatTime);
//            ArrayAdapter<String> chatListAdapter = new ArrayAdapter<String>(this,
//                    android.R.layout.simple_list_item_2, ChatArray);
        ChatListView.setAdapter(customAdapter);
        ChatListView.setSelection(customAdapter.getCount() - 1);
        sendMsg.setText("");
    }
}