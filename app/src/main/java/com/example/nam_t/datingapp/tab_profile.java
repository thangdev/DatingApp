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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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

    private String _name, _day, _month, _year, _bio, _avatar;

    public tab_profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.current_user_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        final String u = "nguyenkhanh917nd@gmail.com";
        final String p = "123456";
        mAuth.createUserWithEmailAndPassword(u,p).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {

                    FirebaseUser user = mAuth.getCurrentUser();
                    if(user != null) {
                        Log.d("s", "ss");
                    }
                }

            }
        });


//        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        mStorageRef = FirebaseStorage.getInstance().getReference();

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
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        });
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Save URI from Gallery
        if(requestCode == SELECT_FILE && resultCode == Activity.RESULT_OK) {
            this.imageHoldUri = data.getData();
        }

    }
}
