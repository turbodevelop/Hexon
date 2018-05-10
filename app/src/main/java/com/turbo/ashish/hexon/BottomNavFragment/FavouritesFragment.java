package com.turbo.ashish.hexon.BottomNavFragment;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.turbo.ashish.hexon.Platform;
import com.turbo.ashish.hexon.R;

import com.turbo.ashish.hexon.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FavouritesFragment extends Fragment {

    private ArrayAdapter<String> favListAdapter;
    private DatabaseReference favMsgListReference;

    public FavouritesFragment() {
        // Required empty public constructor
    }

    private List<String> favMsgList;
    private ListView refFavListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);

        refFavListView = view.findViewById(R.id.idFavMsgListView);
        favMsgList = new ArrayList<>();
        favListAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,favMsgList);

        favMsgListReference = FirebaseDatabase.getInstance().getReference().getRoot()
                .child("FavouriteMessages").child(Platform.CurrentUserPhone);
        favMsgListReference.addChildEventListener(new ChildEventListener() {
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

        // Inflate the layout for this fragment
        return view;
    }

    private void update_message(DataSnapshot dataSnapshot) {
        favMsgList.add((String) dataSnapshot.child("Favoutite_Message").getValue());
        refFavListView.setAdapter(favListAdapter);
        refFavListView.setSelection(favListAdapter.getCount() - 1);
    }
}
