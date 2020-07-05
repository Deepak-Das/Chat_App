package com.example.chat_app.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chat_app.Adapters.UsersAdapter;
import com.example.chat_app.InterfaceActionHandler.ActionHandler;
import com.example.chat_app.MainActivity;
import com.example.chat_app.MessageActivity;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UsersFragment extends Fragment implements ActionHandler {

    private FirebaseUser currentUser;
    private DatabaseReference userRef;

    private RecyclerView recyclerView;
    private UsersAdapter usersAdapter;

    private List<User> mUsers;

    private static final String TAG = "UsersFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_users, container, false);

        recyclerView=view.findViewById(R.id.users_recycler_view);
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);


        mUsers=new ArrayList<>();

        readUsers();

        return view;
    }

    private void readUsers() {
        userRef=FirebaseDatabase.getInstance().getReference("Users");
        currentUser=FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG, "readUsers: "+currentUser.getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();

                for(DataSnapshot snapshot:dataSnapshot.getChildren()) {

                    User user=snapshot.getValue(User.class);

                    assert user!=null;
                    assert currentUser!=null;
                    if(!user.getId().equals(currentUser.getUid())){
                        mUsers.add(user);
                    }

                }

                usersAdapter=new UsersAdapter(getContext(),mUsers ,UsersFragment.this);
                recyclerView.setAdapter(usersAdapter);
                usersAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void usersItemClick(Context context, String UserId) {
        Intent intent=new Intent(context, MessageActivity.class);
        intent.putExtra("user_id",UserId);
        context.startActivity(intent);
    }

    @Override
    public void addUserChatList(final String receiverId, int position) {
        
        final FirebaseUser currentUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference receiverRef= userRef.child(receiverId);
        DatabaseReference senderRef= userRef.child(currentUser.getUid());

        final DatabaseReference FriendListRef=FirebaseDatabase.getInstance().getReference("FriendList");
        //Add user to sender user
        receiverRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                
               User user=dataSnapshot.getValue(User.class);
                Map<String,Object> user_id=new HashMap<>();
                user_id.put("user_id",user.getId());

               FriendListRef.child(currentUser.getUid()).child(receiverId).setValue(user_id);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Add user to receiver user
       senderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user=dataSnapshot.getValue(User.class);
                Map<String,Object> user_id=new HashMap<>();
                user_id.put("user_id",user.getId());

                FriendListRef.child(receiverId).child(currentUser.getUid()).setValue(user_id);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        
    }


}