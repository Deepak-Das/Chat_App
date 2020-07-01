package com.example.chat_app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chat_app.MessageActivity;
import com.example.chat_app.Models.Chat;
import com.example.chat_app.R;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdpater extends RecyclerView.Adapter<MessageAdpater.MessageViewHolder> {


    private Context mContext;
    private List<Chat> mChats;

    private String mPofileUrl;

    FirebaseUser currnetUser;

    int MESSAGE_TYPE_RIGHT=0;
    int MESSAGE_TYPE_LEFT=1;

    public MessageAdpater(Context mContext, List<Chat> mChats, String mPofileUrl) {
        this.mContext = mContext;
        this.mChats = mChats;
        this.mPofileUrl = mPofileUrl;
    }

    @NonNull
    @Override
    public MessageAdpater.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==MESSAGE_TYPE_RIGHT) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            View view = layoutInflater.inflate(R.layout.chat_right_item, parent, false);

            return new MessageAdpater.MessageViewHolder(view);
        }else {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            View view = layoutInflater.inflate(R.layout.chat_left_item, parent, false);

            return new MessageAdpater.MessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdpater.MessageViewHolder holder, final int position) {

        holder.chatMsg.setText(mChats.get(position).getMessage());
        if(mPofileUrl.equals("default")){
            holder.profileImageView.setImageResource(R.mipmap.ic_launcher);
        }else {
            Glide.with(mContext).load(mPofileUrl).into(holder.profileImageView);
        }

    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView chatMsg;
        CircleImageView profileImageView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            chatMsg=itemView.findViewById(R.id.chat_item_text_view);
            profileImageView=itemView.findViewById(R.id.chat_item_profile_img);
        }


    }

    @Override
    public int getItemViewType(int position) {
        currnetUser= FirebaseAuth.getInstance().getCurrentUser();
        if(currnetUser.getUid().equals(mChats.get(position).getSender_id())){
            return MESSAGE_TYPE_RIGHT;
        }else {
            return MESSAGE_TYPE_LEFT;
        }
    }
}
