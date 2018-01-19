package com.example.nam_t.datingapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Change_Password extends AppCompatActivity {


    private EditText edt_new;
    private EditText edt_confirm_new;
    private Button btn_savePassword;

    FirebaseAuth auth;
    FirebaseUser user ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__password);
        edt_confirm_new = (EditText)findViewById(R.id.edt_confirm_new);
        edt_new = (EditText) findViewById(R.id.edt_new_p);
        auth = FirebaseAuth.getInstance();
        btn_savePassword = (Button) findViewById(R.id.btn_save_p);

        user = FirebaseAuth.getInstance().getCurrentUser();





        btn_savePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user !=null)
                {
                    if(edt_new.getText().toString().equals(edt_confirm_new.getText().toString())) {
                        user.updatePassword(edt_new.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Change_Password.this, "password has been changed", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Change_Password.this, "Password could not be changed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else {
                        Toast.makeText(Change_Password.this,"Passwords are not same ",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });





    }












}
