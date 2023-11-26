package com.datcute.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import com.datcute.chatapplication.dao.UserDao;
import com.datcute.chatapplication.databinding.ActivityWelcomeMainBinding;
import com.datcute.chatapplication.interfacce.OnLoginResultListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeMainActivity extends AppCompatActivity {
    private ActivityWelcomeMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                login();
            }
        }.start();

    }

    private void login() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        String password = sharedPreferences.getString("password", "");
        if (!email.isEmpty()&& !password.isEmpty()) {
           UserDao.CheckLogin(WelcomeMainActivity.this, email, password, new OnLoginResultListener() {
               @Override
               public void onLoginSuccess(String uid) {
                   UserDao.getInformationUser(uid,WelcomeMainActivity.this);
               }

               @Override
               public void onLoginFailure(String error) {

               }
           });
        }
        else {
            Intent intent = new Intent(WelcomeMainActivity.this,LoginMainActivity.class);
            startActivity(intent);
        }


    }
}