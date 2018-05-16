package com.turbo.ashish.hexon.BottomNavFragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
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
    private GridView roomListGrid;
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

        roomListGrid = view.findViewById(R.id.idRoomListGrid);

        roomArrayList = new ArrayList<>();
        roomAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, roomArrayList);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        progressDialog.show();
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Iterator iterator = dataSnapshot.child("Groups").getChildren().iterator();
//                Set<String> set = new HashSet<>();
//                while (iterator.hasNext()){
////                    GroupNames.add(((DataSnapshot)iterator.next()).getKey());
//                    set.add(((DataSnapshot)iterator.next()).getKey());
//                }
//                roomArrayList.clear();
//                roomArrayList.addAll(set);
////                CustomAdapter customAdapter = new CustomAdapter();
//                roomList.setAdapter(roomAdapter);
////                Log.d("Delta", String.valueOf(GroupNames.size()));
//                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator iterator = dataSnapshot.child("Users")
                        .child(Platform.CurrentUserPhone + "_" + Platform.CurrentUserID)
                        .child("FavouriteGroups").getChildren().iterator();
                Set<String> set = new HashSet<>();
                while (iterator.hasNext()){
                    set.add(((DataSnapshot)iterator.next()).getKey());
                }
                roomArrayList.clear();
                roomArrayList.addAll(set);
                CustomAdapter customAdapter = new CustomAdapter();
                roomListGrid.setAdapter(customAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        roomListGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(),chatRoom.class);
                intent.putExtra("Room_Name", roomArrayList.get(i));
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
//                username = editText.getText().toString();
//                if (!TextUtils.isEmpty(username)){
//                }else request_username();
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
    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return roomArrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @SuppressLint({"InflateParams", "ViewHolder"})
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.groups_card_layout, null);
            TextView refLabel = view.findViewById(R.id.idGroupsCardText);
            refLabel.setText(roomArrayList.get(i));
            ImageView refImageLbl = view.findViewById(R.id.idGroupImgLbl);
            if (roomArrayList.get(i).equals("Books")){
                refImageLbl.setImageDrawable(getResources().getDrawable(R.drawable.ic_group_book));
            }if (roomArrayList.get(i).equals("Cricket")){
                refImageLbl.setImageDrawable(getResources().getDrawable(R.drawable.ic_group_cricket));
            }if (roomArrayList.get(i).equals("Entrepreneur")){
                refImageLbl.setImageDrawable(getResources().getDrawable(R.drawable.ic_group_entrepreneur));
            }if (roomArrayList.get(i).equals("Fashion")){
                refImageLbl.setImageDrawable(getResources().getDrawable(R.drawable.ic_group_fashion));
            }if (roomArrayList.get(i).equals("Food")){
                refImageLbl.setImageDrawable(getResources().getDrawable(R.drawable.ic_group_food));
            }if (roomArrayList.get(i).equals("Job")) {
                refImageLbl.setImageDrawable(getResources().getDrawable(R.drawable.ic_group_job));
            }if (roomArrayList.get(i).equals("Mobile")){
                refImageLbl.setImageDrawable(getResources().getDrawable(R.drawable.ic_group_mobile));
            }if (roomArrayList.get(i).equals("Movies")){
                refImageLbl.setImageDrawable(getResources().getDrawable(R.drawable.ic_group_movie));
            }if (roomArrayList.get(i).equals("Programming")){
                refImageLbl.setImageDrawable(getResources().getDrawable(R.drawable.ic_group_programming));
            }if (roomArrayList.get(i).equals("Technology")){
                refImageLbl.setImageDrawable(getResources().getDrawable(R.drawable.ic_group_tech));
            }if (roomArrayList.get(i).equals("Test")){
                refImageLbl.setImageDrawable(getResources().getDrawable(R.drawable.ic_group_test));
            }if (roomArrayList.get(i).equals("Travel")) {
                refImageLbl.setImageDrawable(getResources().getDrawable(R.drawable.ic_group_travel));
            }if (roomArrayList.get(i).equals("Trending")) {
                refImageLbl.setImageDrawable(getResources().getDrawable(R.drawable.ic_group_trending));
            }
            return view;
        }
    }
}