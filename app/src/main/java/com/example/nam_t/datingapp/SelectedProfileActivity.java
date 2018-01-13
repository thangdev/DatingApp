package com.example.nam_t.datingapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by nam_t on 13-Jan-18.
 */

public class SelectedProfileActivity extends AppCompatActivity{
    private ImageView imgUserImg;
    private TextView user_name,user_dd,user_mm,user_yy,user_bio;
    private Button btnLike;
    private String userID,profileImageURL;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        imgUserImg=findViewById(R.id.imgUserImg);
        user_name=findViewById(R.id.txtNameProfile);
        user_dd=findViewById(R.id.user_dd);
        user_mm=findViewById(R.id.user_mm);
        user_yy=findViewById(R.id.user_yy);
        user_bio=findViewById(R.id.user_bio);
        btnLike=findViewById(R.id.btn_like);
        userID=getIntent().getExtras().get("selectedID").toString();

    }
}
