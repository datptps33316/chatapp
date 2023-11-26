package com.datcute.chatapplication;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.datcute.chatapplication.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private String passWord;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static final String collection = "Users";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        checkValidate();
        onClickSubmit();
    }

    public boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        return password.matches(passwordPattern);
    }

    public boolean isValidName(String name) {
        String namePattern = "^[A-Za-z]+(\\s[A-Za-z]+)+$";
        return name.matches(namePattern);
    }
    public void checkValidate(){
        binding.edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String email =editable.toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                Pattern pattern = Pattern.compile(emailPattern);
                Matcher matcher = pattern.matcher(email);
                if (matcher.matches()) {
                    binding.tvEmail.setTextColor(getResources().getColor(R.color.xanhla));
                    binding.edtEmail.setBackgroundTintList(ContextCompat.getColorStateList(RegisterActivity.this,R.color.xanhla));
                    binding.edtPassword.setError("",getDrawable(R.drawable.baseline_check_24));
                } else {
                    binding.edtEmail.setError("email chưa hợp lệ");
                    binding.tvEmail.setTextColor(getResources().getColor(R.color.red));
                    // Java
                    binding.edtEmail.setBackgroundTintList(ContextCompat.getColorStateList(RegisterActivity.this, R.color.red));

                }

            }
        });
        binding.edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String name = editable.toString();
                if(isValidName(name)){
                    binding.edtPassword.setError("Password is incorrect", getDrawable(R.drawable.baseline_check_24));
                    binding.tvName.setTextColor(getResources().getColor(R.color.xanhla));
                    binding.edtName.setBackgroundTintList(ContextCompat.getColorStateList(RegisterActivity.this,R.color.xanhla));
                }
                else {
                    binding.edtName.setError("ten chưa hợp lệ");
                    binding.tvName.setTextColor(getResources().getColor(R.color.red));
                    // Java
                    binding.edtName.setBackgroundTintList(ContextCompat.getColorStateList(RegisterActivity.this, R.color.red));

                }

            }
        });
        binding.edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                passWord = editable.toString();
                if(isValidPassword(passWord)){
                    binding.edtPassword.setBackgroundTintList(ContextCompat.getColorStateList(RegisterActivity.this,R.color.xanhla));
                    binding.tvPassword.setTextColor(getResources().getColor(R.color.xanhla));
                    binding.edtPassword.setError("",getDrawable(R.drawable.baseline_check_24));
                }
                else {
                    binding.edtPassword.setError("password trên 8 kí tự và chứ kí tự chữ hoa chữ thường và số");
                    binding.tvPassword.setTextColor(getResources().getColor(R.color.red));
                    binding.edtPassword.setBackgroundTintList(ContextCompat.getColorStateList(RegisterActivity.this, R.color.red));

                }


            }
        });
        binding.edtConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String password = editable.toString();
                if(password.equals(passWord)){
                    binding.tvCfPassword.setTextColor(getResources().getColor(R.color.xanhla));
                    binding.edtConfirmPassword.setBackgroundTintList(ContextCompat.getColorStateList(RegisterActivity.this,R.color.xanhla));
                    binding.edtConfirmPassword.setError("",null);
                }else {
                    binding.edtConfirmPassword.setError("chưa trùng với password");
                    binding.edtConfirmPassword.setBackgroundTintList(ContextCompat.getColorStateList(RegisterActivity.this,R.color.red));
                    binding.tvCfPassword.setTextColor(getResources().getColor(R.color.red));
                }

            }
        });

    }
    public void onClickSubmit(){
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.edtEmail.getText().toString().trim();
                String pass = binding.edtPassword.getText().toString().trim();
                String name = binding.edtName.getText().toString().trim();
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task .isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = user.getUid();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            Map<String,Object> muser = new HashMap<>();
                            muser.put("name" ,name);
                            muser.put("email",email);
                            muser.put("uid",userId);
                            db.collection(collection).document(userId)
                                            .set(muser)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {

                                                        }
                                                    });
                            Toast.makeText(RegisterActivity.this,"dk thanhf cong",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegisterActivity.this,LoginMainActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        }else {
                            Toast.makeText(RegisterActivity.this,"dk thatbai",Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
        });

    }

}