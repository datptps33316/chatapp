package com.datcute.chatapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.datcute.chatapplication.adapter.SearchAdapter;
import com.datcute.chatapplication.databinding.ActivitySearchBinding;
import com.datcute.chatapplication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class Search_Activity extends AppCompatActivity {
    private static ActivitySearchBinding binding;
    private static SearchAdapter adapter;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        db = FirebaseFirestore.getInstance();

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setContentView(binding.getRoot());

        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchUsers(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

    }

    public  void conFigurationRecyclerView(List<User> list , Context context) {
        adapter = new SearchAdapter(list, context);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.recyclrview.setLayoutManager(layoutManager);
        binding.recyclrview.setAdapter(adapter);
    }

    private void searchUsers(String recherche) {
        List<User> listUsers = new ArrayList<>();

        db.collection("Users")
                .whereEqualTo("name", recherche)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            System.err.println("Listen failed:" + error);
                            return;
                        }
                        if (value != null) {
                            for (DocumentSnapshot doc : value) {
                                String name = doc.getString("name");
                                String img = doc.getString("image");
                                String uid = doc.getString("uid");
                                String token = doc.getString("token");
                                listUsers.add(new User(name,uid, Uri.parse(img),token));
                            }
                            updateListUsers(listUsers);
                        }
                    }
                });
    }

    private void updateListUsers(List<User> listUsers) {
        conFigurationRecyclerView(listUsers,this);
    }
}
