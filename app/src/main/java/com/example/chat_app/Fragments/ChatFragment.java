package com.example.chat_app.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chat_app.Adapters.ChatAdapter;
import com.example.chat_app.InterfaceActionHandler.ActionHandler;
import com.example.chat_app.MessageActivity;
import com.example.chat_app.Models.ChatUser;
import com.example.chat_app.Models.User;
import com.example.chat_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment implements ActionHandler {

    private static final String TAG = "ChatFragment";

   private RecyclerView recyclerView;
   private ChatAdapter chatAdapter;
   private List<User> mUsers;
   private List<ChatUser> mChatUsers;
   private DatabaseReference FriendListRef;
   private DatabaseReference UserRef;
   private FirebaseUser auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView=view.findViewById(R.id.chats_recycler_view);
        RecyclerView.ItemDecoration itemDecoration=new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        mChatUsers=new ArrayList<>();



        FriendListRef= FirebaseDatabase.getInstance().getReference("FriendList");
        UserRef=FirebaseDatabase.getInstance().getReference("Users");

        auth=FirebaseAuth.getInstance().getCurrentUser();
        
        FriendListRef.child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChatUsers.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                   ChatUser chatUser=snapshot.getValue(ChatUser.class);
                   mChatUsers.add(chatUser);
                    Log.d(TAG, "onDataChange: "+chatUser.getUser_id());
                }

                readUsers(mChatUsers);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return view;

    }

    private void readUsers(List<ChatUser> mChatUsers) {
        mUsers=new ArrayList<>();
        mUsers.clear();
        if(mChatUsers.size()<=0){
            chatAdapter=new ChatAdapter(getContext(),mUsers,ChatFragment.this);
            recyclerView.setAdapter(chatAdapter);
            chatAdapter.notifyDataSetChanged();

        }else{
            for(ChatUser chatUser:mChatUsers){
                Log.d(TAG, "readUsers list: "+chatUser.getUser_id());

                UserRef.child(chatUser.getUser_id()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user=dataSnapshot.getValue(User.class);
                        mUsers.add(user);
                        chatAdapter=new ChatAdapter(getContext(),mUsers,ChatFragment.this);
                        recyclerView.setAdapter(chatAdapter);
                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }

    @Override
    public void usersItemClick(Context context, String UserId) {
        Intent intent=new Intent(context, MessageActivity.class);
        intent.putExtra("user_id",UserId);
        context.startActivity(intent);
    }

    @Override
    public void addUserChatList(String userId, int position) {

    }
}