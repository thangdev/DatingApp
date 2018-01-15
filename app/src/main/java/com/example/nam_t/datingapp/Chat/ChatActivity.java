package com.example.nam_t.datingapp.Chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nam_t.datingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by khanhnguyen on 1/14/18.
 */

public class ChatActivity extends AppCompatActivity{

    private String currentUserID, currentUserProfileUrl, matchId, profileMatchUrl, chatId;

    private ImageView btnSend;
    private EditText txtSend;
    // TODO: intent tu nam
    private TextView txtNameToolbar, txtBirdayToolbar;
    private CircleImageView imgUserToolbar;

    DatabaseReference mDatabaseUser, mDatabaseChat;

    public ChatActivity() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation);

        // Test
        final String u = "namtran1997@gmail.com";
        final String p = "Namtran174";
        FirebaseAuth.getInstance().signInWithEmailAndPassword(u,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("sad", "onAuthStateChanged: ");
                if(task.isSuccessful()) {
                    Log.d("sada", "Success: ");
                } else {
                    Log.d("sads", "Failt: ");
                }
            }
        });


        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getCurrentUserProfileUrl();

        // TODO: INTENT từ của
        matchId = "MZEsqz76TrVBIHUmUcMnX9X0EPv1";
        profileMatchUrl = "https://firebasestorage.googleapis.com/v0/b/date-now-ffc18.appspot.com/o/User_Profile%2F1906839609?alt=media&token=e76f771d-3784-4980-8166-3d52e9d8ad35";

        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(currentUserID).child("connections").child("accepted")
                .child(matchId).child("chatId");

        mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("Chat");

        getChatId();


        btnSend = (ImageView) findViewById(R.id.btnSend);
        txtSend = (EditText) findViewById(R.id.txtSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String sendMessageText = txtSend.getText().toString();

        if(!sendMessageText.isEmpty()) {
            DatabaseReference newMessageDb = mDatabaseChat.push();

            Map newMessage = new HashMap();
            newMessage.put("createdByUser", currentUserID);
            newMessage.put("text", sendMessageText.trim());

            newMessageDb.setValue(newMessage);
        }

        txtSend.setText(null);

    }

    private void getChatId() {
        mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    chatId = dataSnapshot.getValue().toString();
                    mDatabaseChat = mDatabaseChat.child(chatId);
                    getMessages();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getMessages() {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        final ArrayList<ChatObject> chatObjects = new ArrayList<>();

        // TODO: get data to object

        final ChatAdapter chatAdapter = new ChatAdapter(chatObjects, getApplicationContext());
        recyclerView.setAdapter(chatAdapter);

        mDatabaseChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()) {

                    // TODO: add object to arraylist. khai bao tren dau . chuyen userid sang boolean current user
                    String message = null;
                    String createdByUser = null;
                    if(dataSnapshot.child("text").getValue() != null) {
                        message = dataSnapshot.child("text").getValue().toString();
                        Log.d("hihi", "dm" + message);
                    }
                    if(dataSnapshot.child("createdByUser").getValue() != null) {
                        createdByUser = dataSnapshot.child("createdByUser").getValue().toString();
                    }
                    if(message != null && createdByUser != null) {
                        boolean currentUserBoolean = false;
                        String profileUrl = profileMatchUrl;
                        if(createdByUser.equals(currentUserID)) {
                            currentUserBoolean = true;
                            profileUrl = currentUserProfileUrl;
                            Log.e("CHECK PROFILE", mDatabaseUser.getParent().getParent().getKey() + "DAY");
                        }
                        ChatObject newMessage = new ChatObject(profileUrl, message, currentUserBoolean);
                        Log.d("Tin nhan", message + "cua " + currentUserBoolean);
                        chatObjects.add(newMessage);
                        chatAdapter.notifyDataSetChanged();

                    }

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

    public void getCurrentUserProfileUrl() {
        final DatabaseReference currentUserDB = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(currentUserID).child("profileImageUrl");
        currentUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUserProfileUrl = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
