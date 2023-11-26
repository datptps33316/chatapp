package com.datcute.chatapplication.model;

import android.net.Uri;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class User implements Serializable {
    private String name ;
    private  String email ;
    private String Uid;
    private Uri image;
    private String tokenFcm;
    private long time;

    public User(String name, String email, String uid, Uri image, long time) {
        this.name = name;
        this.email = email;
        Uid = uid;
        this.image = image;
        this.time = time;
    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public User(String name, Uri uri) {
        this.name = name;
        this.image = uri;
    }

    public User(String name, String uid, Uri image) {
        this.name = name;
        Uid = uid;
        this.image = image;
    }
    public User(String name, String uid, Uri image ,String tokenFcm) {
        this.name = name;
        Uid = uid;
        this.image = image;
        this.tokenFcm = tokenFcm;
    }

    public String getTokenFcm() {
        return tokenFcm;
    }

    public void setTokenFcm(String tokenFcm) {
        this.tokenFcm = tokenFcm;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User() {
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
