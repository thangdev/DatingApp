package com.example.nam_t.datingapp.Chat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nam_t.datingapp.R;
import com.example.nam_t.datingapp.SelectedProfileActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by khanhnguyen on 1/14/18.
 */

public class ChatActivity extends AppCompatActivity{

    private String currentUserID, currentUserProfileUrl, matchId, profileMatchUrl, chatId;

    private ImageView btnSend, btnCamera;
    private EditText txtSend;
    private Uri imageHoldUri;

    private ProgressDialog mProgress;

    private static final int SELECT_FILE = 1;

    private TextView txtNameToolbar;
    private CircleImageView imgUserToolbar;
    private TextView onlineToolbar;

    DatabaseReference mDatabaseUser, mDatabaseChat;
    private Date date=new Date();
    private Toolbar toolbar;

    public ChatActivity() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation);

        txtNameToolbar = (TextView) findViewById(R.id.txtNameToolbar);
        imgUserToolbar = (CircleImageView) findViewById(R.id.imgUserToolbar);
        onlineToolbar = (TextView) findViewById(R.id.onlineToolbar);

        btnCamera = (ImageView) findViewById(R.id.btnCamera);

        mProgress = new ProgressDialog(ChatActivity.this);

        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getCurrentUserProfileUrl();


        matchId = getIntent().getExtras().get("matchId").toString();
        getInfoUserMatchById(matchId);

        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(currentUserID).child("connections").child("accepted")
                .child(matchId).child("chatId");

        mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("Chat");

        getChatId();


        btnSend = (ImageView) findViewById(R.id.btnSend);
        txtSend = (EditText) findViewById(R.id.txtSend);
        toolbar=findViewById(R.id.toolbar);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCamera();
            }
        });

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChatActivity.this,SelectedProfileActivity.class);
                Bundle b=new Bundle();
                b.putString("SelectedID", matchId);
                intent.putExtras(b);
                ChatActivity.this.startActivity(intent);
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
            newMessage.put("Created at", new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(date));

            newMessageDb.setValue(newMessage);
        }

        txtSend.setText(null);

    }

    private void sendCamera() {
        final CharSequence[] items = {"Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
        builder.setTitle("Add Photo");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(items[which].equals("Choose from Library")) {
                    sendCameraIntent();
                } else if(items[which].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }
    public void sendCameraIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Save URI from Gallery
        if(requestCode == SELECT_FILE && resultCode == Activity.RESULT_OK) {

            if(data.getData() != null) {
                mProgress.setTitle("Saving Profile");
                mProgress.setMessage("Please wait..loading..");
                mProgress.show();

                final StorageReference mChildStorage = FirebaseStorage.getInstance().getReference()
                        .child("Message_Image").child(data.getData().getLastPathSegment());
                mChildStorage.putFile(data.getData()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String urlMessageImage = taskSnapshot.getDownloadUrl().toString();

                        DatabaseReference newMessageImageDb = mDatabaseChat.push();

                        Map newMessageImage = new HashMap();
                        newMessageImage.put("createdByUser", currentUserID);
                        newMessageImage.put("image", urlMessageImage);
                        newMessageImage.put("Created at", new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(date));

                        newMessageImageDb.setValue(newMessageImage);
                    }
                });

                mProgress.dismiss();
            }
        }

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

        final ChatAdapter chatAdapter = new ChatAdapter(chatObjects, getApplicationContext());
        recyclerView.setAdapter(chatAdapter);
        layoutManager.setStackFromEnd(true);
        mDatabaseChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()) {

                    String message = null;
                    String createdByUser = null;
                    String imgMessage = null;
                    String timeStamp=null;
                    if(dataSnapshot.child("text").getValue() != null) {
                        message = dataSnapshot.child("text").getValue().toString();
                    }
                    if(dataSnapshot.child("createdByUser").getValue() != null) {
                        createdByUser = dataSnapshot.child("createdByUser").getValue().toString();
                    }
                    if(dataSnapshot.child("image").getValue() != null) {
                        imgMessage = dataSnapshot.child("image").getValue().toString();
                    }
                    if(dataSnapshot.child("Created at").getValue()!=null){
                        timeStamp=dataSnapshot.child("Created at").getValue().toString();
                    }
                    if(message != null && createdByUser != null) {
                        boolean currentUserBoolean = false;
                        boolean imgMessageBoolean = false;
                        String profileUrl = profileMatchUrl;
                        if(createdByUser.equals(currentUserID)) {
                            currentUserBoolean = true;
                            profileUrl = currentUserProfileUrl;
                        }
                        ChatObject newMessage = new ChatObject(profileUrl, message, imgMessage, currentUserBoolean, imgMessageBoolean,timeStamp);
                        chatObjects.add(newMessage);
                        chatAdapter.notifyDataSetChanged();

                    }
                    if(imgMessage != null && createdByUser != null) {
                        boolean currentUserBoolean = false;
                        boolean imgMessageBoolean = true;
                        String profileUrl = profileMatchUrl;
                        if(createdByUser.equals(currentUserID)) {
                            currentUserBoolean = true;
                            profileUrl = currentUserProfileUrl;
                        }
                        ChatObject newMessage = new ChatObject(profileUrl, message, imgMessage, currentUserBoolean, imgMessageBoolean,timeStamp);
                        chatObjects.add(newMessage);
                        chatAdapter.notifyDataSetChanged();
                    }

                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                    recyclerView.setHasFixedSize(true);

                    recyclerView.scrollToPosition(chatObjects.size());
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

    public void getInfoUserMatchById(String id) {

        DatabaseReference getUserById = FirebaseDatabase.getInstance().getReference().child("Users").child(id);
        getUserById.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                txtNameToolbar.setText(dataSnapshot.child("name").getValue().toString());
                if(! dataSnapshot.child("profileImageUrl").getValue().toString().isEmpty()) {
                    profileMatchUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
                    Picasso.with(getApplicationContext()).load(dataSnapshot.child("profileImageUrl").getValue().toString())
                            .into(imgUserToolbar);
                }

                if(dataSnapshot.child("online").getValue().equals(true)) {
                    onlineToolbar.setText("Online");
                } else {
                    onlineToolbar.setText("Last online: " + dataSnapshot.child("online").getValue().toString());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}