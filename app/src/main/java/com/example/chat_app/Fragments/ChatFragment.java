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
import com.example.chat_app.Models.Chat;
import com.example.chat_app.Models.ChatUser;
import com.example.chat_app.Models.ChatUserID;
import com.example.chat_app.Models.User;
import com.example.chat_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatFragment extends Fragment implements ActionHandler {

    private static final String TAG = "ChatFragment";


   private RecyclerView recyclerView;
   private ChatAdapter chatAdapter;

   private List<ChatUser> mChatUsers;

   private DatabaseReference FriendListRef;
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

        auth=FirebaseAuth.getInstance().getCurrentUser();

        FriendListRef.child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChatUsers.clear();

                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    ChatUser chatUser=snapshot.getValue(ChatUser.class);

                        mChatUsers.add(chatUser);

                    chatAdapter=new ChatAdapter(getContext(), mChatUsers,ChatFragment.this);
                    recyclerView.setAdapter(chatAdapter);
                    chatAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
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