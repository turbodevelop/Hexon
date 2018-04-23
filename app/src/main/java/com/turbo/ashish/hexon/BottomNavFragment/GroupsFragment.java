package com.turbo.ashish.hexon.BottomNavFragment;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.turbo.ashish.hexon.Platform;
import com.turbo.ashish.hexon.R;
import com.turbo.ashish.hexon.chat.chatRoom;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class GroupsFragment extends Fragment {
    private ArrayList<String> roomArrayList;
    private ArrayAdapter<String> roomAdapter;
    private DatabaseReference databaseReference;
    private String username;
    private ListView roomList;
    private View view;
    private ProgressDialog progressDialog;
    private List<String> GroupNames;

    public GroupsFragment() {                                                                       //Constructor
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_groups, container, false);
        simpleProcessDialog();                                                                      //ProcessDialog

        roomList = view.findViewById(R.id.idRoomList);

        roomArrayList = new ArrayList<>();
        roomAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, roomArrayList);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        username = Platform.CurrentUserPhone;
        progressDialog.show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator iterator = dataSnapshot.child("Groups").getChildren().iterator();
                Set<String> set = new HashSet<>();
                while (iterator.hasNext()){
//                    GroupNames.add(((DataSnapshot)iterator.next()).getKey());
                    set.add(((DataSnapshot)iterator.next()).getKey());
                }
                roomArrayList.clear();
                roomArrayList.addAll(set);
//                CustomAdapter customAdapter = new CustomAdapter();
                roomList.setAdapter(roomAdapter);
//                Log.d("Delta", String.valueOf(GroupNames.size()));
                progressDialog.dismiss();
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
    private void simpleProcessDialog(){                                                             //ProcessDialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading All Groups...");
        progressDialog.setTitle("Wait Please...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
    }
}