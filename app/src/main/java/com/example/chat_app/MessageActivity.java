package com.example.chat_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chat_app.Adapters.ChatAdapter;
import com.example.chat_app.Adapters.MessageAdpater;
import com.example.chat_app.Models.Chat;
import com.example.chat_app.Models.ChatUser;
import com.example.chat_app.Models.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    private static final String TAG = "MessageActivity";
    
    private CircleImageView mToolbarProfile_image;
    private MaterialTextView mToolbarUsername;
    private MaterialTextView mToolbarStatus;
    private String receiverProfileUri;

    private Toolbar mToolbar;

    RecyclerView recyclerView;

    MessageAdpater messageAdpater;

    private List<Chat> mChats;
    
    private AppCompatImageButton sendButton;
    private TextInputEditText sendMessageText;
    private Boolean flag;


    private Intent intent;

    private FirebaseUser currentUser;
    String receiverUserId;
    private DatabaseReference receiverUserRef;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        mToolbar=findViewById(R.id.message_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mToolbarProfile_image=findViewById(R.id.message_profile_image);
        mToolbarUsername=findViewById(R.id.message_toolbar_username);
        mToolbarStatus=findViewById(R.id.message_toolbar_status);

        recyclerView=findViewById(R.id.message_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        mChats =new ArrayList<>();

        
        sendButton=findViewById(R.id.button_send);
        sendMessageText=findViewById(R.id.message_edit_text);

        sendMessageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                dispalyTypingSatus(CharSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        intent=getIntent();
        receiverUserId=intent.getStringExtra("user_id");
        

        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference=FirebaseDatabase.getInstance().getReference();
        receiverUserRef= databaseReference.child("Users").child(receiverUserId);

        receiverUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ChatUser chatuser=dataSnapshot.getValue(ChatUser.class);
                mToolbarUsername.setText(chatuser.getUser_name());
                mToolbarStatus.setText(chatuser.getStatus());

                receiverProfileUri =  chatuser.getImage_URL();

                if(chatuser.getImage_URL().equals("default")){
                    mToolbarProfile_image.setImageResource(R.mipmap.ic_launcher);
                }else{
                    Glide.with(getApplicationContext()).load(chatuser.getImage_URL()).into(mToolbarProfile_image);
                }

                readChat();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Editable msgText=sendMessageText.getText();
                if(!TextUtils.isEmpty(msgText)){
                    sendMessage(currentUser.getUid(),receiverUserId,msgText.toString());
                }else {
                    Toast.makeText(MessageActivity.this, "can not send empty message", Toast.LENGTH_SHORT).show();
                }
                sendMessageText.setText("");
            }
        });

    }

    public void sendMessage(String sender_id, String receiver_id, String msg){

        Chat chat=new Chat(sender_id,receiver_id,msg);

        Map<String, Object> message=new HashMap<>();
        message.put("last_message",msg);

        databaseReference.child("FriendList").child(sender_id).child(receiver_id).updateChildren(message);
        databaseReference.child("FriendList").child(receiver_id).child(sender_id).updateChildren(message);

        databaseReference.child("Chats").child(sender_id).child(receiver_id).push().setValue(chat);
        databaseReference.child("Chats").child(receiver_id).child(sender_id).push().setValue(chat);



    }


    public void readChat(){


        databaseReference.child("Chats").child(currentUser.getUid()).child(receiverUserId)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mChats.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    mChats.add(snapshot.getValue(Chat.class));
                }
                messageAdpater= new MessageAdpater(getApplicationContext(),mChats,receiverProfileUri);
                recyclerView.setAdapter(messageAdpater);
                messageAdpater.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat calenderfomate = new SimpleDateFormat("EEE dd, YYY (hh:mm)");
        String lastSeenDate=calenderfomate.format(calendar.getTime());
        status(lastSeenDate);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }


    private void status(String status) {

        Map<String, Object> status_field=new HashMap<>();
        status_field.put("status",status);
        databaseReference.child("Users").child(currentUser.getUid()).updateChildren(status_field);
    }
}