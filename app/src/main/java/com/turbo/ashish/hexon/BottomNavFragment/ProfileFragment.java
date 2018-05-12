package com.turbo.ashish.hexon.BottomNavFragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.turbo.ashish.hexon.Platform;
import com.turbo.ashish.hexon.R;

import java.io.IOException;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.turbo.ashish.hexon.Platform.CurrentUserID;
import static com.turbo.ashish.hexon.Platform.CurrentUserPhone;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private ImageView profileImage;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mauthListener;
    private FirebaseStorage storage;
    ImageView profileuser;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot()
                .child("Users").child(CurrentUserPhone + "_" + CurrentUserID);
        final TextView refPhoneNumber = v.findViewById(R.id.idProfilePhone),
                refName = v.findViewById(R.id.idProfileName),
               refstatus = v.findViewById(R.id.userstatus),
                refmail=v.findViewById(R.id.mail);

        //ProfilePic
//        StorageReference storageRef = storageReference.child("Users/Profile Pictures/" +
//                Platform.CurrentUserPhone + "_#_" +
//                Platform.CurrentUserID);

        profileuser = v.findViewById(R.id.idProfilePic);
        profileuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseProfilePic();
            }
        });

        final ImageView interstgroup = v.findViewById(R.id.interstedgroup);
        interstgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),Interst.class));
            }
        });
        refPhoneNumber.setText(CurrentUserPhone);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                refName.setText(dataSnapshot.child("Name").getValue().toString());
               refstatus.setText(dataSnapshot.child("Status").getValue().toString());
               refmail.setText(dataSnapshot.child("Email").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        refName.setText(databaseReference.child("Name").getKey());

        v.findViewById(R.id.idEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileEditDialog();
            }
        });

        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void chooseProfilePic() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),6);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();

            }
        }
   }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void ProfileEditDialog() {
        final Dialog reportDialog = new Dialog(getActivity());
        reportDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        reportDialog.setCancelable(true);
        reportDialog.setContentView(R.layout.dailogprofile);
        reportDialog.show();
        final EditText name = reportDialog.findViewById(R.id.name);
        final EditText status = reportDialog.findViewById(R.id.status);
        final EditText email = reportDialog.findViewById(R.id.email);
        Button cancel = reportDialog.findViewById(R.id.cancel);
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot()
                .child("Users")
                .child(CurrentUserPhone + "_" + CurrentUserID);
        reportDialog.findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("Name").setValue(name.getText().toString());
                databaseReference.child("Email").setValue(email.getText().toString());
                databaseReference.child("Status").setValue(status.getText().toString());
                Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();
                reportDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reportDialog.dismiss();
            }
        });
    }
}
