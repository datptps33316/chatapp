package com.datcute.chatapplication.interfacce;

public interface OnLoginResultListener {
    void onLoginSuccess(String uid);
    void onLoginFailure(String error);
}
