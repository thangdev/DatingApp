package com.example.nam_t.datingapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nam_t.datingapp.Matches.match_Adapter;
import com.example.nam_t.datingapp.Matches.match_Object;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class tab_message extends Fragment {
    private RecyclerView matchRecyclerView;
    private RecyclerView.Adapter matchAdapter;
    private RecyclerView.LayoutManager matchLayoutManager;
    private String currentUserID;


    private ArrayList<match_Object> resultsMatches = new ArrayList<match_Object>();
    private List<match_Object> getDataSetMatches() {
        return resultsMatches;
    }

    public tab_message() {
        // Required empty public constructor
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_message, container, false);
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        matchRecyclerView = (RecyclerView) view.findViewById(R.id.message_list);
        matchRecyclerView.setNestedScrollingEnabled(false);
        matchRecyclerView.setHasFixedSize(true);
        matchLayoutManager = new LinearLayoutManager(getActivity());
        matchRecyclerView.setLayoutManager(matchLayoutManager);
        matchAdapter = new match_Adapter(getDataSetMatches(), getActivity());
        matchRecyclerView.setAdapter(matchAdapter);
        resultsMatches.clear();
        getUserMatchId();
        return view;
    }

    private void getUserMatchId() {

        DatabaseReference matchDb = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(currentUserID).child("connections").child("accepted");

        matchDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    ArrayList<String> keys=new ArrayList<>();
                    for(DataSnapshot objectSnapshot: dataSnapshot.getChildren()) {
                        keys.add(objectSnapshot.getKey());
                    }
                    FetchMatchInformation(keys);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void FetchMatchInformation(final ArrayList keys) {

        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users");
        userDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(keys.contains(dataSnapshot.getKey().toString())){

                    String userId = dataSnapshot.getKey().toString();
                    String name = "";
                    String profileImageUrl = "";
                    if(dataSnapshot.child("name").getValue()!=null){
                        name = dataSnapshot.child("name").getValue().toString();

                    }
                    if(dataSnapshot.child("profileImageUrl").getValue()!=null){
                        profileImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
                    }


                    match_Object obj = new match_Object(userId, name, profileImageUrl);
                    resultsMatches.add(obj);
                    matchAdapter.notifyDataSetChanged();
                }
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
