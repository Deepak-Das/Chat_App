package com.example.chat_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chat_app.Fragments.ChatFragment;
import com.example.chat_app.Fragments.UsersFragment;
import com.example.chat_app.InterfaceActionHandler.ActionHandler;
import com.example.chat_app.Models.User;
import com.example.chat_app.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.UsersViewHolder> {

    private Context mContext;
    private List<User> mUsers;
    private ActionHandler mActionHandler;

    public ChatAdapter(Context mContext, List<User> mUsers, ActionHandler mActionHandler) {
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.mActionHandler=mActionHandler;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(mContext);
        View view=layoutInflater.inflate(R.layout.user_list_item,parent,false);

        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, final int position) {

        holder.username.setText(mUsers.get(position).getUser_name());
        if(mUsers.get(position).getImage_URL().equals("default")){
            holder.profileImageView.setImageResource(R.mipmap.ic_launcher);
        }else {
            Glide.with(mContext).load(mUsers.get(position).getImage_URL()).into(holder.profileImageView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActionHandler.usersItemClick(mContext,mUsers.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profileImageView;
        MaterialTextView username;

       public UsersViewHolder(@NonNull View itemView) {
           super(itemView);

           profileImageView=itemView.findViewById(R.id.user_list_profile_img);
           username=itemView.findViewById(R.id.user_list_unsername);

       }
   }
}
