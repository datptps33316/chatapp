package com.datcute.chatapplication.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.datcute.chatapplication.MainActivity;
import com.datcute.chatapplication.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(remoteMessage.getNotification() != null){
          String title = remoteMessage.getNotification().getTitle();
          String body = remoteMessage.getNotification().getBody();
           sendNotification(title,body);
        }

        // Xử lý thông điệp nhận được từ FCM ở đây
        // Hiển thị thông báo hoặc thực hiện các thao tác khác dựa trên nội dung của thông điệp
    }

    @Override
    public void onNewToken(String token) {
        // Xử lý khi token FCM mới được tạo hoặc cập nhật
    }

    private void sendNotification(String title, String body) {
        // Tạo thông báo tùy chỉnh
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String channelId = "Your_Channel_ID";
        String channelName = "Your_Channel_Name";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Your_Channel_Description");
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            notificationManager.createNotificationChannel(channel);
        }

        // Tạo intent cho khi thông báo được bấm
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setPriority(NotificationCompat.PRIORITY_HIGH); // Đặt priority là HIGH để hiển thị kiểu Heads-Up

// Thêm các thông tin khác vào notificationBuilder...

        notificationManager.notify(0, notificationBuilder.build());
    }
}
