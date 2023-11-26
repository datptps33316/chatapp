package com.datcute.chatapplication.interfacce;

import com.datcute.chatapplication.model.FCMPayload;
import com.datcute.chatapplication.model.FCMResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface FCMService {
    String key = "AAAAxD5bJ4g:APA91bGG9YKwg_PQadY5h0T4Je_KP_gJTcV0VsSpvSVQR2q7P808b_jk_hqx92c28pNGvi0TdvB1mkOOSeAhikaZKSw-yCwEnwQ5SdFLrXwEmjbOUadYPd7tW1nIck74wyGkzk1GAOeC";
    @Headers({
            "Content-Type: application/json",
            "Authorization: key=" +key// Thay YOUR_SERVER_KEY bằng khóa server của bạn
    })
    @POST("fcm/send")
    Call<FCMResponse> sendMessage(@Body FCMPayload body);
}
