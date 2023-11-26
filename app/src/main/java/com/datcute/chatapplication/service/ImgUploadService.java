package com.datcute.chatapplication.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.net.Uri;
import android.os.PersistableBundle;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ImgUploadService extends JobService {

    public static final String ACTION_UPLOAD_IMAGE = "action_upload_image";
    public static final String EXTRA_IMAGE_URI = "extra_image_uri";
    public static final String EXTRA_PERSISTABLE_BUNDLE ="jjdfd";


    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        PersistableBundle extras = jobParameters.getExtras();
        Log.e("dhfasdfasdfasd", "đang chạy service");
        Log.e("dhfasdfasdfasd", extras.toString());
        if (extras != null) {

            String imageUriString = extras.getString(ImgUploadService.EXTRA_IMAGE_URI);
            Uri imageUri = Uri.parse(imageUriString);
                Log.e("dat cute ne", String.valueOf(imageUri));
                handleUploadImage(imageUri,jobParameters);

        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    private void handleUploadImage(Uri imageUri, JobParameters jobParameters) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        StorageReference storageRef = storage.getReference();
        Calendar calendar = Calendar.getInstance();
        StorageReference imageRef = storageRef.child("images" + calendar.getTimeInMillis() + "/profile.jpg");

        UploadTask uploadTask = imageRef.putFile(imageUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String uriImage = uri.toString();
                        updateImgUser(uriImage,uid ,jobParameters);

                    }
                });
            }
        });

    }

    private void updateImgUser(String imageUrl,String uid ,JobParameters jobParameters){
        Map<String, Object> mapImageUser = new HashMap<>();
        mapImageUser.put("image", imageUrl);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(uid).update(mapImageUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.e("hủy service" ,"huyre service");
                        jobFinished(jobParameters, false);
                    }
                });
    }
}
