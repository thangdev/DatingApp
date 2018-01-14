package com.example.nam_t.datingapp;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class tab_profile extends Fragment {

    private FirebaseAuth mAuth;

    private DatabaseReference mUserDatabase;
    private StorageReference mStorageRef;

    private ProgressDialog mProgress;

    private static final int SELECT_FILE = 1;
    private Uri imageHoldUri;

    private CircleImageView imgProfile;
    private TextView txtNameProfile, txtDayProfile, txtMonthProfile, txtYearProfile, txtBioProfile;
    private Button btnSave, btnLogout;

    private String _name, _day, _month, _year, _bio, _avatar, _gender;

    public tab_profile() {
        // Constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.current_user_profile, container, false);

        mProgress = new ProgressDialog(getActivity());

        imageHoldUri = null;

        imgProfile = (CircleImageView) rootView.findViewById(R.id.imgProfile);
        txtNameProfile = (TextView) rootView.findViewById(R.id.txtNameProfile);
        txtDayProfile = (TextView) rootView.findViewById(R.id.txtDayProfile);
        txtMonthProfile = (TextView) rootView.findViewById(R.id.txtMonthProfile);
        txtYearProfile = (TextView) rootView.findViewById(R.id.txtYearProfile);
        txtBioProfile = (TextView) rootView.findViewById(R.id.txtBioProfile);

        btnSave = (Button) rootView.findViewById(R.id.btnSave);
        btnLogout = (Button) rootView.findViewById(R.id.btnLogout);


        mAuth = FirebaseAuth.getInstance();

        // Test
        final String u = "nguyenkhanh97nd@gmail.com";
        final String p = "123456";
        mAuth.signInWithEmailAndPassword(u,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(getUserGender()).child(mAuth.getCurrentUser().getUid());
        mStorageRef = FirebaseStorage.getInstance().getReference();

        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                txtNameProfile.setText(dataSnapshot.child("name").getValue().toString());
                txtDayProfile.setText(dataSnapshot.child("DOB_dd").getValue().toString());
                txtMonthProfile.setText(dataSnapshot.child("DOB_mm").getValue().toString());
                txtYearProfile.setText(dataSnapshot.child("DOB_yyyy").getValue().toString());
                txtBioProfile.setText(dataSnapshot.child("bio").getValue().toString());

                _avatar = dataSnapshot.child("profileImageUrl").getValue().toString();

                Picasso.with(getActivity()).load(_avatar).into(imgProfile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageProfilePicture();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Logout", Toast.LENGTH_LONG).show();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(), "Logout", Toast.LENGTH_LONG).show();

//                mAuth.signOut();
//                Intent intent = new Intent(getActivity(), WebcomeActivity.class);
//                startActivity(intent);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = txtNameProfile.getText().toString().trim();
                String day = txtDayProfile.getText().toString().trim();
                String month = txtMonthProfile.getText().toString().trim();
                String year = txtYearProfile.getText().toString().trim();
                String bio = txtBioProfile.getText().toString().trim();

                if(validateForm(name, day, month, year, bio)) {
                    _name = name;
                    _day = day;
                    _month = month;
                    _year = year;
                    _bio = bio;

                    if(imageHoldUri != null) {

                        mProgress.setTitle("Saving Profile");
                        mProgress.setMessage("Vui lòng chờ...");
                        mProgress.show();

                        if(_avatar != null) {
                            StorageReference deleteAvatar = FirebaseStorage.getInstance().getReferenceFromUrl(_avatar);
                            deleteAvatar.delete();
                        }

                        final StorageReference mChildStorage = mStorageRef.child("User_Profile").child(imageHoldUri.getLastPathSegment());
                        String profilePicUri = imageHoldUri.getLastPathSegment();

                        mChildStorage.putFile(imageHoldUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                _avatar = taskSnapshot.getDownloadUrl().toString();

                                mUserDatabase.child("name").setValue(_name);
                                mUserDatabase.child("DOB_dd").setValue(_day);
                                mUserDatabase.child("DOB_mm").setValue(_month);
                                mUserDatabase.child("DOB_yyyy").setValue(_year);
                                mUserDatabase.child("bio").setValue(_bio);
                                mUserDatabase.child("profileImageUrl").setValue(_avatar);

                                mProgress.dismiss();

                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                            }
                        });
                    } else {
                        mProgress.setTitle("Saving Profile");
                        mProgress.setMessage("Vui lòng chờ...");
                        mProgress.show();

                        mUserDatabase.child("name").setValue(_name);
                        mUserDatabase.child("DOB_dd").setValue(_day);
                        mUserDatabase.child("DOB_mm").setValue(_month);
                        mUserDatabase.child("DOB_yyyy").setValue(_year);
                        mUserDatabase.child("bio").setValue(_bio);

                        mProgress.dismiss();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Vui lòng nhập đúng giá trị", Toast.LENGTH_LONG).show();
                }

            }
        });
        return rootView;
    }

    public boolean validateForm(String name, String day, String month, String year, String bio) {
        if(name.length() <= 0 || name.length() > 35) return false;
        if(bio.length() <= 0 || bio.length() > 100) return false;

        int getDay = Integer.parseInt(day);
        int getMonth = Integer.parseInt(month);
        int getYear = Integer.parseInt(year);

        if( (getDay > 30) && (getMonth == 4 || getMonth == 6 || getMonth == 9 || getMonth == 11)) {
            return false;
        } else if(getMonth == 2) {
            if(getYear % 4 == 0) {
                if(getDay < 30) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if(getDay < 29) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public void pickImageProfilePicture() {

        // Display dialog to choose camera or gallery
        final CharSequence[] items = {"Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(items[which].equals("Choose from Library")) {
                    galleryIntent();
                } else if(items[which].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }


    public void galleryIntent() {
        Log.d("Text", "Gallery");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_FILE);
    }

    private String userGender;
    public String getUserGender() {
        userGender = "Female";
        DatabaseReference maleDB = FirebaseDatabase.getInstance().getReference().child("User").child("Male");
        maleDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getKey().equals(mAuth.getCurrentUser().getUid())) {
                    userGender = "Male";
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
        return userGender;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Save URI from Gallery
        if(requestCode == SELECT_FILE && resultCode == Activity.RESULT_OK) {
            this.imageHoldUri = data.getData();
        }

    }

}
