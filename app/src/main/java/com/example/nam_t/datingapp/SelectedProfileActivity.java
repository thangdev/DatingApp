package com.example.nam_t.datingapp;

import android.os.Bundle;
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
    private DatabaseReference usersDb,currentUserDb,userDb;

    private Button btnGender;

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

        btnGender = (Button) findViewById(R.id.btnGender);

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();
        currentUserDb=FirebaseDatabase.getInstance().getReference().child("Users").child(currentUId);
        userID=getIntent().getExtras().get("SelectedID").toString();
        userDb=FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        checkSent();
        checkAccepted();
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SelectedProfileActivity.this,"Liked",Toast.LENGTH_SHORT).show();
                isConnectionMatch(userID);
                btnLike.setEnabled(false);
                btnLike.setText("Already liked");
                checkSent();
                checkAccepted();
            }
        });

        getUserInfo();

    }

    private void isConnectionMatch(final String userId) {
        final DatabaseReference likedUserConnectionsDb = usersDb.child(userID);
        likedUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("connections").child("sent").child(currentUId).exists()){
                    Toast.makeText(SelectedProfileActivity.this, "You have a new match!!", Toast.LENGTH_LONG).show();
                    userDb.child("connections").child("sent").child(currentUId).setValue(null);
                    String key = FirebaseDatabase.getInstance().getReference().child("connections").child("sent").child(currentUId).child("Chat").push().getKey();

                    usersDb.child(dataSnapshot.getKey()).child("connections").child("accepted").child(currentUId).child("chatId").setValue(key);
                    usersDb.child(currentUId).child("connections").child("accepted").child(dataSnapshot.getKey()).child("chatId").setValue(key);
                }
                else {
                    usersDb.child(currentUId).child("connections").child("sent").child(userID).setValue(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    private void checkSent(){
        DatabaseReference userConnectionsDb = usersDb.child(currentUId);
        userConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("connections").child("sent").hasChild(userID)){
                    btnLike.setEnabled(false);
                    btnLike.setText("Already liked");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void checkAccepted(){
        DatabaseReference userConnectionsDb = usersDb.child(currentUId);
        userConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("connections").child("accepted").hasChild(userID)){
                    btnLike.setEnabled(false);
                    btnLike.setText("Already liked");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
                            case "":
                                Picasso.with(getApplicationContext()).load(R.drawable.ic_person_black_48dp).into(user_img);
                                break;
                            case "default":
                                Picasso.with(getApplicationContext()).load(R.drawable.ic_person_black_48dp).into(user_img);
                                break;
                            default:
                                Picasso.with(getApplicationContext()).load(profileImageUrl).into(user_img);
                                break;
                        }
                    }
                    btnGender.setText(map.get("gender").toString());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
