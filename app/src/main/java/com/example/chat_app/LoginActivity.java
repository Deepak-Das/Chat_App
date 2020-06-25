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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    Toolbar toolbar;

    TextInputEditText loginEmail,loginPassword;

    MaterialButton buttonLogin;

    ProgressBar progressBar;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth=FirebaseAuth.getInstance();

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");

        progressBar=findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);

        loginEmail=findViewById(R.id.login_email);
        loginPassword=findViewById(R.id.login_password);

        buttonLogin=findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                String email=loginEmail.getText().toString();
                String password=loginPassword.getText().toString();
                
                if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "all filed is require", Toast.LENGTH_SHORT).show();
                }else if(password.length()<6){
                    Toast.makeText(LoginActivity.this, "password lenght must be if 6 digit", Toast.LENGTH_SHORT).show();
                }else{
                    userLogin(email,password);
                }

            }
        });
    }

    private void userLogin(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);

        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            progressBar.setVisibility(View.GONE);

                            Toast.makeText(LoginActivity.this, "login sucessfully", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();

                        }else{
                            Log.d(TAG, "onComplete: "+task.getException());
                        }
                    }
                });
    }
}