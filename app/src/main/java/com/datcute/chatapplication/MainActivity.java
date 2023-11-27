package com.datcute.chatapplication;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.datcute.chatapplication.adapter.ViewPagerAdapter;
import com.datcute.chatapplication.databinding.ActivityMainBinding;
import com.datcute.chatapplication.fragment.FragmentContacts;
import com.datcute.chatapplication.fragment.FragmentFriend;
import com.datcute.chatapplication.fragment.FragmentMessage;
import com.datcute.chatapplication.fragment.FragmentSetting;
import com.datcute.chatapplication.model.User;
import com.datcute.chatapplication.transformer.RotatePageTransformer;
import com.datcute.chatapplication.transformer.ScaleInOutPageTransformer;
import com.datcute.chatapplication.transformer.ZoomOutPageTransformer;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nl.joery.animatedbottombar.AnimatedBottomBar;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    final String PHOTO_USER_KEY = "image";
    final String NAME_USER_KEY = "name";
    final String COLLECTION_KEY = "Users";
    public static final int MY_REQUEST_CODE = 10;

    private FirebaseFirestore db;
    User user;


    public void setUser(User user) {
        this.user = user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        String imageUri = getIntent().getStringExtra("image");
        String name = getIntent().getStringExtra("name");
        if(imageUri == null){
            imageUri ="https://firebasestorage.googleapis.com/v0/b/chat-app-de681.appspot.com/o/images1700910106121%2Fprofile.jpg?alt=media&token=d0e37f44-fbbe-41e0-9f11-2f5edfe1cc1b";
        }
        user = new User(name,Uri.parse(imageUri));
        setFragmentDefault();
        onClickRequestPermission();
        binding.bottomNavigation.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int i, @Nullable AnimatedBottomBar.Tab tab, int i1, @NonNull AnimatedBottomBar.Tab tab1) {
                if (tab1.getId() == R.id.mnmessg) {
                    initFragment(new FragmentMessage(user));
                }
                if (tab1.getId() == R.id.mn_contacts) {
                    initFragment(new FragmentContacts());
                }
                if (tab1.getId() == R.id.mn_friend) {
                    initFragment(new FragmentFriend());
                }
                if (tab1.getId() == R.id.mn_setting) {
                    initFragment(new FragmentSetting(user));
                }
            }

            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {

            }
        });
        db = FirebaseFirestore.getInstance();
        Query query = db.collection(COLLECTION_KEY);
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
                            String name = modifiedDocument.getString(NAME_USER_KEY);
                            String image = modifiedDocument.getString(PHOTO_USER_KEY);
                            if(image == null){
                                image ="https://firebasestorage.googleapis.com/v0/b/chat-app-de681.appspot.com/o/images1700910106121%2Fprofile.jpg?alt=media&token=d0e37f44-fbbe-41e0-9f11-2f5edfe1cc1b";
                            }
                            user = new User(name, Uri.parse(image));

                            break;
                        case REMOVED:
                            // Xử lý khi một document bị xóa
                            break;
                    }
                }
            }
        });
        setContentView(binding.getRoot());

    }



    private void initFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.silde_in_right).replace(R.id.container, fragment).commit();
    }


    public void setFragmentDefault() {
        FragmentMessage message = new FragmentMessage(user);
        initFragment(message);
    }



    private void onClickRequestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        } else {
            String[] permissione = {android.Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.POST_NOTIFICATIONS};
            this.requestPermissions(permissione, MY_REQUEST_CODE);
        }
    }


}






