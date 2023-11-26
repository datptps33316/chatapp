package com.datcute.chatapplication.model;
public class FCMPayload {
    private String to;
    private FCMNotification notification;

    public FCMPayload(String to, FCMNotification notification) {
        this.to = to;
        this.notification = notification;
    }

    // Getters and setters
}

