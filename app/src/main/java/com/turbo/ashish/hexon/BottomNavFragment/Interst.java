package com.turbo.ashish.hexon.BottomNavFragment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.turbo.ashish.hexon.R;

import java.util.List;

import static com.turbo.ashish.hexon.Platform.CurrentUserID;
import static com.turbo.ashish.hexon.Platform.CurrentUserPhone;

public class Interst extends AppCompatActivity {
    String Cricket1,Programming1,Trending1,Enterprenure1,Books1,Job1,Food1,Fashion1,Technology1, Travel1,Movies1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interst2);
       final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().getRoot()
                .child("Users").child(CurrentUserPhone + "_" + CurrentUserID);

        final CheckBox Cricket =findViewById(R.id.checkBox);
        final CheckBox Programing=findViewById(R.id.checkBox1);
        final CheckBox Trending=findViewById(R.id.checkBox2);
        final CheckBox Enterprenure=findViewById(R.id.checkBox11);
        final CheckBox Books=findViewById(R.id.checkBox8);
        final CheckBox Food=findViewById(R.id.checkBox4);
        final CheckBox Fashion=findViewById(R.id.checkBox5);
        final CheckBox Technology=findViewById(R.id.checkBox7);
        final CheckBox Travel=findViewById(R.id.checkBox3);
        final CheckBox Movies=findViewById(R.id.checkBox9);
        final CheckBox job=findViewById(R.id.checkBox10);
        Button submit=findViewById(R.id.Submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Cricket.isChecked()){
                    Cricket1 = "Cricket";
                    databaseReference.child("FavouriteGroups").child(Cricket1).setValue(Cricket1);
                }
                if (Programing.isChecked()){
                    Programming1 = "Programming";
                    databaseReference.child("FavouriteGroups").child(Programming1).setValue(Programming1);
                }
                if (Trending.isChecked()){
                     Trending1="Trending";
                    databaseReference.child("FavouriteGroups").child(Trending1).setValue(Trending1);
                }if (Enterprenure.isChecked()){
                     Enterprenure1="Enterprenure";
                    databaseReference.child("FavouriteGroups").child(Enterprenure1).setValue(Enterprenure1);
                }if (Books.isChecked()){
                     Books1=("Books");
                    databaseReference.child("FavouriteGroups").child(Books1).setValue(Books1);
                }if (Food.isChecked()){
                    Food1="Food";
                    databaseReference.child("FavouriteGroups").child(Food1).setValue(Food1);
                }
                if (Fashion.isChecked()){
                    Fashion1="Fashion";
                    databaseReference.child("FavouriteGroups").child(Fashion1).setValue(Fashion1);
                }
                if (Technology.isChecked()){
                    Technology1 ="Technology";
                    databaseReference.child("FavouriteGroups").child(Technology1).setValue(Technology1);
                }
                if (Travel.isChecked()){
                    Travel1 ="Travel";
                    databaseReference.child("FavouriteGroups").child(Travel1).setValue(Travel1);
                }
                if (Movies.isChecked()){
                    Movies1 ="Movies";
                    databaseReference.child("FavouriteGroups").child(Movies1).setValue(Movies1);
                }
                if (job.isChecked()){
                    Job1="Job";
                    databaseReference.child("FavouriteGroups").child(Job1).setValue(Job1);
                }
                Toast.makeText(getApplicationContext(),"Groups Have successfully Added to Favourites",
                        Toast.LENGTH_SHORT).show();
                finish();

            }
        });
        Button cancel=findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });






    }
}
