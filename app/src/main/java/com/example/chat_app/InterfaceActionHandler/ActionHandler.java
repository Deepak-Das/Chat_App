package com.example.chat_app.InterfaceActionHandler;

import android.content.Context;

public interface ActionHandler {
    public void usersItemClick(Context context, String UserId);
    public void addUserChatList(String userId, int position);
}
