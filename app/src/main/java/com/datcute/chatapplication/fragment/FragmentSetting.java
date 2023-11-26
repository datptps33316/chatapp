package com.datcute.chatapplication.fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.JOB_SCHEDULER_SERVICE;

import static androidx.core.content.ContextCompat.getSystemService;

import static com.google.api.ChangeType.ADDED;

import android.Manifest;
import android.app.Dialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.datcute.chatapplication.LoginMainActivity;
import com.datcute.chatapplication.MainActivity;
import com.datcute.chatapplication.R;

import com.datcute.chatapplication.UpdateProfileActivity;
import com.datcute.chatapplication.WelcomeMainActivity;
import com.datcute.chatapplication.databinding.FragmentSettingBinding;
import com.datcute.chatapplication.model.User;
import com.datcute.chatapplication.service.ImgUploadService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.Logging;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pub.devrel.easypermissions.EasyPermissions;

public class FragmentSetting extends Fragment {
    private FragmentSettingBinding binding;
    public static final int MY_REQUEST_CODE = 10;
    private int progressValue = 0;
    private Handler handler;
    private Uri muri;
    Button button;
    Dialog dialog;
    ImageView imageView;
    String imageUrl;


    User user;
    private Disposable disposable;
    private FirebaseAuth mAuth;
    private int progress;
    private FirebaseFirestore db;


    public FragmentSetting(User user) {
        this.user = user;
    }
    public FragmentSetting() {
        // Constructor không tham số
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(getLayoutInflater());
        setInformationUser(user);
        onClickUpdate();
        onListenerFromFirebase();
        handler = new Handler();


        binding.updateimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UpdateProfileActivity.class);
                startActivity(intent);
            }
        });


        binding.logour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().getCurrentUser();
                Intent intent = new Intent(requireActivity(), LoginMainActivity.class);
                startActivity(intent);

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                // Tăng giá trị của ProgressBar sau mỗi giây
                int progress = binding.progressBar.getProgress();
                if (progress < binding.progressBar.getMax()) {
                    binding.progressBar.setProgress(progress + 1);
                    handler.sendEmptyMessageDelayed(0, 500); // Gửi tin nhắn lại sau mỗi giây
                }
                return true;
            }
        });
        return binding.getRoot();

    }


    private void onClickUpdate() {
        binding.imageUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission();
            }
        });

    }

    public void setInformationUser(User user) {
        if (user != null) {
            Glide.with(requireActivity()).load(user.getImage()).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.img_5).into(binding.imageUsers);
            binding.nameUser.setText(user.getName());
        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.img_5);
            Bitmap bitmap = drawableToBitmap(drawable);
            binding.imageUsers.setImageBitmap(bitmap);
        }
    }

    public Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    public void setName(String name) {
        binding.nameUser.setText(name);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("dfasdfasd", "ondicktroy");
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
        Log.e("dat", "ondicktroy");
    }


    private void onClickRequestPermission() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(requireContext(), permissions)) {
            openGallery();
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    "Ứng dụng cần quyền để truy cập ảnh.",
                    MY_REQUEST_CODE,
                    permissions
            );
        }
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
                muri = intent.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), muri);
                    setImg(bitmap);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    });

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
        }
    }

    public void setImg(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }




    private void startProgressUpdate() {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Tăng giá trị progress
                progressValue += 10; // Tăng giá trị theo ý muốn

                // Cập nhật giá trị của ProgressBar
                binding.progressBar.setProgress(progressValue);

                // Kiểm tra điều kiện dừng (ở đây là 100, có thể điều chỉnh theo ý muốn)
                if (progressValue < 50) {
                    // Lặp lại công việc sau mỗi giây
                    startProgressUpdate();
                }
            }
        }, 200); // Thời gian lặp lại là 1000 milliseconds (1 giây)
    }

    private void onListenerFromFirebase() {
        db = FirebaseFirestore.getInstance();
        Query query = db.collection("Users");
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot querySnapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    // Xử lý lỗi
                    return;
                }

                // Xử lý dữ liệu khi có sự thay đổi
                for (DocumentChange documentChange : querySnapshot.getDocumentChanges()) {
                    switch (documentChange.getType()) {
                        case ADDED:
                            // Xử lý khi có một document mới được thêm vào
                            break;
                        case MODIFIED:
                            // Xử lý khi một document đã tồn tại được thay đổi
                            DocumentSnapshot modifiedDocument = documentChange.getDocument();
                            String name = modifiedDocument.getString("name");
                            String image = modifiedDocument.getString("image");
                            if (isAdded()) {
                                startProgress(image);
                                MainActivity mainActivity = new MainActivity();
                                mainActivity.setUser(new User(name,Uri.parse(image)));

                                binding.progressBar.setProgress(100);
                                binding.progressBar.setProgress(0);
                                // Thực hiện các thao tác với Fragment ở đây
                            }

                            break;
                        case REMOVED:
                            // Xử lý khi một document bị xóa
                            break;
                    }
                }
            }
        });

        }

    private void startProgress(String imageUrl) {
        // Tạo một Runnable để cập nhật giá trị của ProgressBar
        Runnable updateProgressRunnable = new Runnable() {
            @Override
            public void run() {
                if (progress < binding.progressBar.getMax()) {
                    // Tăng giá trị của ProgressBar sau mỗi giây
                    progress = progress+7;
                    binding.progressBar.setProgress(progress);

                    // Lập lịch thực hiện lại sau mỗi giây
                    handler.postDelayed(this, 150);
                }else {
                    if(isAdded()){
                        Glide.with(requireActivity()).load(Uri.parse(imageUrl)).error(R.drawable.img_5).into(binding.imageUsers);
                        progress = 0;
                        binding.progressBar.setProgress(progress);
                    }

                }
            }
        };
        // Lập lịch thực hiện Runnable sau một khoảng thời gian
        handler.postDelayed(updateProgressRunnable, 200);
    }
    }





