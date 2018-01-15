package com.example.nam_t.datingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nam_t on 10-Jan-18.
 */

public class RegisterActivity extends AppCompatActivity {
    private Button mRegister;
    private EditText mEmail, mPassword, mName;
    private EditText dd,mm,yy;
    private String mDOB;

    private RadioGroup mRadioGroup;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user !=null){
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };


        mRegister = (Button) findViewById(R.id.btn_new);
        mEmail = (EditText) findViewById(R.id.new_email);
        mName = (EditText) findViewById(R.id.new_fullname);
        mPassword = (EditText) findViewById(R.id.new_password);
        dd=(EditText) findViewById(R.id.dob_dd);
        mm=(EditText) findViewById(R.id.dob_mm);
        yy=(EditText) findViewById(R.id.dob_yy);
        mRadioGroup = (RadioGroup) findViewById(R.id.gender);

        mRegister.setOnClickListener(new View.OnClickListener() {
            public boolean checkValidation(){
                if(mName.getText().length()<5&&mName.getText().length()<30){
                    Toast.makeText(RegisterActivity.this,"Invalid name",Toast.LENGTH_SHORT).show();
                    return false;
                }
                if(mEmail.getText().length()<10){
                    Toast.makeText(RegisterActivity.this,"Invalid email",Toast.LENGTH_SHORT).show();
                    return false;
                }
                if(mPassword.getText().length()<7){
                    Toast.makeText(RegisterActivity.this,"Invalid password",Toast.LENGTH_SHORT).show();
                    return false;
                }
                mDOB=dd.getText()+"-"+mm.getText()+"-"+yy.getText();
                SimpleDateFormat dateFormat= new SimpleDateFormat("dd-MM-yyyy");
                if(mDOB.trim().length()!=dateFormat.toPattern().length()||Integer.parseInt(String.valueOf(yy.getText()))<1920||Integer.parseInt(String.valueOf(yy.getText()))>2002){
                    Toast.makeText(RegisterActivity.this,"Invalid date",Toast.LENGTH_SHORT).show();
                    return false;
                }
                dateFormat.setLenient(false);
                try{
                    dateFormat.parse(mDOB.trim());
                }
                catch(ParseException e){
                    Toast.makeText(RegisterActivity.this,"Invalid date",Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }
            @Override
            public void onClick(View view) {
                if(checkValidation()){
                    int selectId = mRadioGroup.getCheckedRadioButtonId();

                    final RadioButton radioButton = (RadioButton) findViewById(selectId);

                    if(radioButton.getText() == null){
                        return;
                    }

                    final String email = mEmail.getText().toString();
                    final String password = mPassword.getText().toString();
                    final String name = mName.getText().toString();
                    final String day = dd.getText().toString();
                    final String month = mm.getText().toString();
                    final String year = yy.getText().toString();
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "sign up error", Toast.LENGTH_SHORT).show();
                            }else{
                                String userId = mAuth.getCurrentUser().getUid();
                                DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                                Map userInfo = new HashMap<>();
                                userInfo.put("name", name);
                                userInfo.put("DOB_dd",day);
                                userInfo.put("DOB_mm",month);
                                userInfo.put("DOB_yyyy",year);
                                userInfo.put("gender",radioButton.getText().toString());
                                userInfo.put("bio","");
                                userInfo.put("profileImageUrl", "");
                                currentUserDb.updateChildren(userInfo);
                                Toast.makeText(RegisterActivity.this,"Sign up successfully",Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
}
