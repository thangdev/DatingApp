package com.example.nam_t.datingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by nam_t on 18-Jan-18.
 */

public class ChangePasswordActivity extends AppCompatActivity {


    private EditText edt_new;
    private EditText edt_confirm_new;
    private Button btn_savePassword;

    FirebaseAuth auth;
    FirebaseUser user ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
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
                                    Toast.makeText(ChangePasswordActivity.this, "Password has been changed", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ChangePasswordActivity.this,MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(ChangePasswordActivity.this, "Password could not be changed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else {
                        Toast.makeText(ChangePasswordActivity.this,"Password does not match",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}
