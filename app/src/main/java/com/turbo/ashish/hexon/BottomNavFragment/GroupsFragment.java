package com.turbo.ashish.hexon.BottomNavFragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roger.catloadinglibrary.CatLoadingView;
import com.turbo.ashish.hexon.R;
import com.turbo.ashish.hexon.chat.AccountActivity;
import com.turbo.ashish.hexon.chat.chatRoom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsFragment extends Fragment {
    ArrayList<String> roomArrayList;
    ArrayAdapter<String> roomAdapter;
    DatabaseReference databaseReference;
    private String username;
    ListView roomList;
    View view;
    private CatLoadingView CLV;
    private FragmentActivity myContext;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }
    private FragmentManager supportFragmentManager = myContext.getSupportFragmentManager(); //If using fragments from support v4

    //Functions
    private void exitApplication(){
        getActivity().finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void HideKeyboard(){
        View HideKeyBoardView = getActivity().getCurrentFocus();
        if (HideKeyBoardView != null) {
            InputMethodManager iMM = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            assert iMM != null;
            iMM.hideSoftInputFromWindow(HideKeyBoardView.getWindowToken(), 0);
        }
    }

    //Exit Application On Back Pressed
   /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            exitApplication();
        }
        return super.onKeyDown(keyCode, event);
    }*/

    public GroupsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_groups, container, false);

        final EditText roomName = view.findViewById(R.id.idRoomEntry);
        roomList = view.findViewById(R.id.idRoomList);

        roomArrayList = new ArrayList<>();
        roomAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, roomArrayList);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        username = "Ashish";//(String) getIntent().getExtras().get("CurrentUserPhone");

        view.findViewById(R.id.idEntryBtn).setOnClickListener(new View.OnClickListener() {
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
                CLV.show(supportFragmentManager,""); //Show CatLoadingView
                Iterator iterator = dataSnapshot.child("Groups").getChildren().iterator();
                Set<String> set = new HashSet<>();
                while (iterator.hasNext()){
                    set.add(((DataSnapshot)iterator.next()).getKey());
                }
                roomArrayList.clear();
                roomArrayList.addAll(set);
                roomList.setAdapter(roomAdapter);
                CLV.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        roomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(),chatRoom.class);
                intent.putExtra("Room_Name", ((TextView)view).getText().toString());
                intent.putExtra("User_Name",username);
                startActivity(intent);
            }
        });
        return view;

    }

    private void request_username() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter User Name");
        final EditText editText = new EditText(getActivity());
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