package com.datcute.chatapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.datcute.chatapplication.R;
import com.datcute.chatapplication.databinding.ActivityUpdateInforMainBinding;
import com.datcute.chatapplication.databinding.ActivityUpdateProfileBinding;
import com.datcute.chatapplication.fragment.FragmentSetting;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpdateInforMainActivity extends AppCompatActivity {
private ActivityUpdateInforMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityUpdateInforMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.name.getText().toString().trim();
                String email = binding.email.getText().toString().trim();
                String phone = binding.phone.getText().toString().trim();
                Map<String, Object> muser = new HashMap<>();
                muser.put("name",name);
                muser.put("email",email);
                muser.put("phone",phone);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Users").document(uid).update(muser)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(UpdateInforMainActivity.this,"update thanh cong" , Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });
            }
        });
    }
}




