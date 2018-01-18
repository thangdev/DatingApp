package com.example.nam_t.datingapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nam_t on 13-Jan-18.
 */

public class SelectedProfileActivity extends AppCompatActivity{
    private ImageView user_img;
    private TextView user_name,user_dd,user_mm,user_yy,user_bio;
    private Button btnLike;
    private String currentUId,userID,profileImageUrl;
    private FirebaseAuth mAuth;
    private DatabaseReference usersDb,userDb;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        user_img=findViewById(R.id.imgUserImg);
        user_name=findViewById(R.id.txtNameProfile);
        user_dd=findViewById(R.id.user_dd);
        user_mm=findViewById(R.id.user_mm);
        user_yy=findViewById(R.id.user_yy);
        user_bio=findViewById(R.id.user_bio);
        btnLike=findViewById(R.id.btn_like);
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();
        userID=getIntent().getExtras().get("SelectedID").toString();
        userDb=FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usersDb.child(currentUId).child("connections").child("sent").child(userID).setValue(true);
                Toast.makeText(SelectedProfileActivity.this,"Liked",Toast.LENGTH_SHORT).show();
                btnLike.setEnabled(false);
            }
        });
        getUserInfo();

    }
    private void getUserInfo(){
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()&& dataSnapshot.getChildrenCount()>0){
                    Map<String,Object> map=(Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("name")!=null){
                        user_name.setText(map.get("name").toString());
                    }
                    if(map.get("DOB_dd")!=null){
                        user_dd.setText(map.get("DOB_dd").toString());
                    }
                    if(map.get("DOB_mm")!=null){
                        user_mm.setText(map.get("DOB_mm").toString());
                    }
                    if(map.get("DOB_yyyy")!=null){
                        user_yy.setText(map.get("DOB_yyyy").toString());
                    }
                    if(map.get("bio")!=null){
                        user_bio.setText(map.get("bio").toString());
                    }
                    if(map.get("profileImageUrl")!=null){
                        profileImageUrl = map.get("profileImageUrl").toString();
                        switch(profileImageUrl){
                            case "default":
                                Picasso.with(getApplicationContext()).load(R.drawable.ic_person_black_48dp).into(user_img);
                                break;
                            default:
                                Picasso.with(getApplicationContext()).load(profileImageUrl).into(user_img);
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}