package com.datcute.chatapplication.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.datcute.chatapplication.adapter.AllFriendAdapter;
import com.datcute.chatapplication.adapter.FriendRqAdapter;
import com.datcute.chatapplication.dao.UserDao;
import com.datcute.chatapplication.databinding.FragmentAllFriendBinding;
import com.datcute.chatapplication.databinding.FragmentContactsBinding;
import com.datcute.chatapplication.interfacce.GetInformationCallback;
import com.datcute.chatapplication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FragmentAllFriend extends Fragment {
private FragmentAllFriendBinding binding;

    ArrayList<User> userArrayList;
    private AllFriendAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAllFriendBinding.inflate(inflater, container, false);
        userArrayList = new ArrayList<>();
        getFriendsToFirebase();
        return binding.getRoot();

    }

    private void getFriendsToFirebase() {
        UserDao.getFriendsRealtime(UserDao.getReceiverId(), "1", new GetInformationCallback() {
            @Override
            public void onGetListFriendsRequest(ArrayList<User> listUser) {
                conFigurationRecyclerView(listUser);
            }
        });

    }
    public void conFigurationRecyclerView(ArrayList<User> list) {
        mAdapter = new AllFriendAdapter(list,getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        binding.RecyclerView.setLayoutManager(layoutManager);
        binding.RecyclerView.setAdapter(mAdapter);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL);
        binding.RecyclerView.addItemDecoration(itemDecoration);
    }


}
