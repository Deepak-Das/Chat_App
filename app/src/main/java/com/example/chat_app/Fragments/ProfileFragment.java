package com.example.chat_app.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;

import com.bumptech.glide.Glide;
import com.example.chat_app.Models.User;
import com.example.chat_app.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";



    MaterialTextView username;
    CircleImageView my_profile_img;

    DatabaseReference userRef;
    FirebaseUser auth;

    StorageReference storageReference;
    private static  final  int IMAGE_REQUEST=1;
    private Uri imageUri;
    private StorageTask uploadTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_profile, container, false);

        username=view.findViewById(R.id.my_name);
        my_profile_img=view.findViewById(R.id.my_profile_img);

        storageReference= FirebaseStorage.getInstance().getReference("ProfileImages");


        auth=FirebaseAuth.getInstance().getCurrentUser();
        userRef=FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);

                username.setText(user.getUser_name());

                if(user.getImage_URL().equals("default")){
                   my_profile_img.setImageResource(R.mipmap.ic_launcher);
                }else {
                    Glide.with(getContext()).load(user.getImage_URL()).into(my_profile_img);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        my_profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });

        return view;
    }

    private void openImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){

        ContentResolver contentResolver=getContext().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType((uri)));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMAGE_REQUEST && resultCode==Activity.RESULT_OK
            && data!=null && data.getData()!=null){

            imageUri=data.getData();

            Glide.with(this).load(imageUri).into(my_profile_img);


            uploadImage();


        }
    }

    private void  uploadImage(){
        final ProgressDialog pd= new ProgressDialog(getContext());
        pd.setTitle("Uploading");
        pd.show();

        if(imageUri!=null){

            final StorageReference fileRef = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));

            uploadTask=fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();
                        Log.d(TAG, "onComplete: "+mUri);
                        pd.dismiss();
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });

        }
    }


}