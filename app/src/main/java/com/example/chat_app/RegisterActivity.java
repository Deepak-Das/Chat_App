package com.example.chat_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.chat_app.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference usersRef;

    Toolbar toolbar;

    MaterialButton buttonRegister;

    TextInputEditText mUsername,mEmail,mPassword;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        usersRef=database.getReference("Users");

        mUsername=findViewById(R.id.user_name);
        mEmail=findViewById(R.id.email);
        mPassword=findViewById(R.id.password);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login/Register");

        progressBar=findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);


        buttonRegister=findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username,email,password;
                username=mUsername.getText().toString();
                email=mEmail.getText().toString();
                password=mPassword.getText().toString();

                if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)||TextUtils.isEmpty(username)){
                    Toast.makeText(RegisterActivity.this, "All field require", Toast.LENGTH_SHORT).show();
                }else if (password.length()<6){
                    Toast.makeText(RegisterActivity.this, "password must be atleast 6 characters", Toast.LENGTH_SHORT).show();
                }else{
                    createNewUser(email,password,username,"Offline");
                }
            }
        });

    }

    public void createNewUser(String email, String password, final String username, final String status){
        progressBar.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String user_id=auth.getCurrentUser().getUid();

                            User user=new User(user_id,username,"default",status);

//                            Map<String,Object> user=new HashMap<>();
//                            user.put("id", user_id);
//                            user.put("user_name",username);
//                            user.put("image_URL","default");
                            usersRef.child(user_id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        progressBar.setVisibility(View.GONE);

                                        Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Log.d(TAG, "onComplete: "+task.getException());
                                    }
                                }
                            });
                        }
                        else{
                            Log.d(TAG, "onComplete: "+task.getException());
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

    }
}

