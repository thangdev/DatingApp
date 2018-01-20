package com.example.nam_t.datingapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nam_t.datingapp.Users.user_Adapter;
import com.example.nam_t.datingapp.Users.user_object;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class tab_userlist extends Fragment {
    RangeSeekBar<Integer> age;
    private Button btn_save;
    private int min,max;
    private FirebaseAuth mAuth;
    private String currentUId;
    private DatabaseReference usersDb;

    private RecyclerView userListRecyclerView;
    private RecyclerView.Adapter userListAdapter;
    private RecyclerView.LayoutManager userListLayoutManager;
    private ArrayList<user_object> userList=new ArrayList<>();
    private List<user_object> getDataSetUser(){
        return userList;
    }

    public tab_userlist() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_tab_userlist, container, false);
        usersDb= FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth=FirebaseAuth.getInstance();
        currentUId=mAuth.getCurrentUser().getUid();
        age=(RangeSeekBar<Integer>) view.findViewById(R.id.filter_age);
        btn_save=(Button) view.findViewById(R.id.save);
        userListRecyclerView=(RecyclerView)view.findViewById(R.id.user_list);
        final TextView age_view=view.findViewById(R.id.age_view);
        // Get noticed while dragging
        age.setNotifyWhileDragging(true);
        userListRecyclerView=view.findViewById(R.id.user_list);
        userListRecyclerView.setNestedScrollingEnabled(false);
        userListRecyclerView.setHasFixedSize(true);
        userListLayoutManager=new LinearLayoutManager(getContext());
        userListRecyclerView.setLayoutManager(userListLayoutManager);
        userListAdapter=new user_Adapter(getDataSetUser(),getContext());
        userListRecyclerView.setAdapter(userListAdapter);
        checkUserGender();
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                min=age.getSelectedMinValue();
                max=age.getSelectedMaxValue();
                age_view.setText(min+"-"+max+" years old");
                userList.clear();
                userListAdapter.notifyDataSetChanged();
                getSuitableUsers();
            }
        });

        return view;
    }

    private String userGender;
    private String oppositeUserGender;
    public void checkUserGender(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userDb = usersDb.child(user.getUid());
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.child("gender").getValue() != null){
                        userGender = dataSnapshot.child("gender").getValue().toString();
                        switch (userGender){
                            case "Male":
                                oppositeUserGender = "Female";
                                break;
                            case "Female":
                                oppositeUserGender = "Male";
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

    public void getSuitableUsers(){
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists() && dataSnapshot.child("gender").getValue().toString().equals(oppositeUserGender)) {
                    int userAge=Calendar.getInstance().get(Calendar.YEAR)-Integer.parseInt(dataSnapshot.child("DOB_yyyy").getValue().toString());
                    if (userAge<=max&&userAge>=min) {
                        user_object item = new user_object(dataSnapshot.getKey(), dataSnapshot.child("name").getValue().toString(),String.valueOf(userAge),dataSnapshot.child("profileImageUrl").getValue().toString());
                        userList.add(item);
                        userListAdapter.notifyDataSetChanged();
                    }
                }
                Collections.shuffle(userList);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
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
    }



}