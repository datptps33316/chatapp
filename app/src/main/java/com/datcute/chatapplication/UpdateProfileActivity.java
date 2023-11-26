package com.datcute.chatapplication;


import android.Manifest;
import android.app.Dialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.datcute.chatapplication.databinding.ActivityUpdateProfileBinding;
import com.datcute.chatapplication.fragment.FragmentMessage;
import com.datcute.chatapplication.fragment.FragmentSetting;
import com.datcute.chatapplication.service.ImgUploadService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UpdateProfileActivity extends AppCompatActivity {
    private ActivityUpdateProfileBinding binding;
    public static final int MY_REQUEST_CODE = 10;
    private Uri uriImage;

    public UpdateProfileActivity() {

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());

        binding.btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(uriImage != null){
                   scheduleJob(String.valueOf(uriImage));
                   Toast.makeText(UpdateProfileActivity.this,"đang tiến hành cập nhật" ,Toast.LENGTH_LONG).show();
                   finish();
               }
            }
        });

        binding.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        setContentView(binding.getRoot());
    }

    private void scheduleJob(String imageUri) {
        Intent jobIntent = new Intent(this, ImgUploadService.class);

// Tạo một PersistableBundle và thêm dữ liệu cần truyền
        PersistableBundle persistableBundle = new PersistableBundle();
        persistableBundle.putString(ImgUploadService.EXTRA_IMAGE_URI,imageUri);

        jobIntent.putExtra(ImgUploadService.EXTRA_PERSISTABLE_BUNDLE, persistableBundle);

// Đặt các thông tin khác vào Intent
        jobIntent.setAction(ImgUploadService.ACTION_UPLOAD_IMAGE);

// Tạo một JobInfo và thêm công việc vào JobScheduler
        JobInfo jobInfo = new JobInfo.Builder(1, new ComponentName(this, ImgUploadService.class))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setExtras(persistableBundle)
                .build();

        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        activityResultLauncher.launch(Intent.createChooser(intent, "Select image"));
    }

    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Intent intent = result.getData();
                if (intent == null) {
                    return;
                }
                uriImage = intent.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriImage);
                    setImg(bitmap);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    });
    public void setImg(Bitmap bitmap){
        binding.img.setImageBitmap(bitmap);
    }


}
