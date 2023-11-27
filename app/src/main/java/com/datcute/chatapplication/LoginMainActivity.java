package com.datcute.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.datcute.chatapplication.dao.UserDao;
import com.datcute.chatapplication.databinding.ActivityLoginMainBinding;
import com.datcute.chatapplication.fragment.FragmentMessage;
import com.datcute.chatapplication.fragment.FragmentSetting;
import com.datcute.chatapplication.interfacce.OnLoginResultListener;
import com.datcute.chatapplication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginMainActivity extends AppCompatActivity {
    private ActivityLoginMainBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.edtEmail.getText().toString().trim();
                String pass = binding.edtPassword.getText().toString().trim();
                UserDao.CheckLogin(LoginMainActivity.this,email, pass, new OnLoginResultListener() {
                    @Override
                    public void onLoginSuccess(String uid) {

                        UserDao.getInformationUser(uid,LoginMainActivity.this);
                    }

                    @Override
                    public void onLoginFailure(String error) {

                    }
                });
            }
        });
        binding.forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickForgotPassWord();
            }
        });


    }

    public void onClickForgotPassWord() {
        Intent intent = new Intent(LoginMainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }


    public static String getUidUser() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            // Trả về UID của người dùng hiện tại
            return uid;
        } else {
            // Người dùng chưa đăng nhập
            return null;
        }
    }

    private void moveToMainActivity(User user) {
        // tạo token

        Intent intent = new Intent(LoginMainActivity.this, MainActivity.class);
        intent.putExtra("name", user.getName());
        intent.putExtra("img", String.valueOf(user.getImage()));
        startActivity(intent);
        finish();
    }


}