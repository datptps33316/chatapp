package com.datcute.chatapplication.interfacce;

import com.datcute.chatapplication.dao.UserDao;

import java.util.ArrayList;

public interface SenderIdCallback {
    void onSenderListReady(ArrayList<UserDao.UserSender> list );

}
