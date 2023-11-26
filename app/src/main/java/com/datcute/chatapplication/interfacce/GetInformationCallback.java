package com.datcute.chatapplication.interfacce;

import com.datcute.chatapplication.model.User;

import java.util.ArrayList;

public interface GetInformationCallback {
    void onGetListFriendsRequest(ArrayList<User> listUser);
}
