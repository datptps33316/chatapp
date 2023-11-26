package com.datcute.chatapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.datcute.chatapplication.dao.UserDao;
import com.datcute.chatapplication.databinding.ActivityDetailUserAcitivityBinding;
import com.datcute.chatapplication.interfacce.FCMService;
import com.datcute.chatapplication.model.FCMNotification;
import com.datcute.chatapplication.model.FCMPayload;
import com.datcute.chatapplication.model.FCMResponse;
import com.datcute.chatapplication.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailUserAcitivity extends AppCompatActivity {
    private ActivityDetailUserAcitivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailUserAcitivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        String name = getIntent().getStringExtra("name");
        String image = getIntent().getStringExtra("img");
        String uid = getIntent().getStringExtra("uid");
        String token = getIntent().getStringExtra("token");
        User user = new User(name, uid, Uri.parse(image), token);

        Glide.with(this).load(user.getImage()).error(R.drawable.img_5).into(binding.img);
        binding.name.setText(user.getName());
        binding.email.setText(user.getName());
        binding.phone.setText(user.getName());
        onClickthemban(user);



    }

    int check = 0;
    public void onClickthemban( User user){
        if(check == 0){
            binding.btnSubmit.setText("thêm bạn bè");
        }
        else {
            binding.btnSubmit.setText("hủy bạn bè");
        }
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDao.sendFriendInvitations(user,DetailUserAcitivity.this);
                if(check == 0){
                    check = 1;
                }else {
                    check = 0;
                }
                if(check == 0){
                    binding.btnSubmit.setText("thêm bạn bè");
                }
                else {
                    binding.btnSubmit.setText("hủy bạn bè");
                }

            }
        });
    }



}