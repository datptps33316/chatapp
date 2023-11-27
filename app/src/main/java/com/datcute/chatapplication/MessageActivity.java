package com.datcute.chatapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.datcute.chatapplication.databinding.ActivityMessageBinding;

public class MessageActivity extends AppCompatActivity {
private ActivityMessageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessageBinding.inflate(getLayoutInflater());

        String name= getIntent().getStringExtra("name");
        String image = getIntent().getStringExtra("image");
        if(image == null){
            image = "https://firebasestorage.googleapis.com/v0/b/chat-app-de681.appspot.com/o/images1700910106121%2Fprofile.jpg?alt=media&token=d0e37f44-fbbe-41e0-9f11-2f5edfe1cc1b";
        }

        Glide.with(this).load(Uri.parse(image)).error(R.drawable.img_5).into(binding.circleImageView);
        binding.name.setText(name);
        setContentView(binding.getRoot());


    }
}