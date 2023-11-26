package com.datcute.chatapplication.fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.datcute.chatapplication.adapter.FriendRqAdapter;
import com.datcute.chatapplication.dao.UserDao;
import com.datcute.chatapplication.databinding.FragmentFriendRequestBinding;
import com.datcute.chatapplication.interfacce.GetInformationCallback;
import com.datcute.chatapplication.interfacce.onItemRcvClickListener;
import com.datcute.chatapplication.model.User;
import java.util.ArrayList;
import java.util.List;


public class FragmentFriendRq extends Fragment {
    private FragmentFriendRequestBinding binding;
    FriendRqAdapter mAdapter;





    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFriendRequestBinding.inflate(inflater, container, false);
        getListFriendRequest();
        return binding.getRoot();
    }

    public void conFigurationRecyclerView(ArrayList<User> list) {
        mAdapter = new FriendRqAdapter(list, requireActivity(), new onItemRcvClickListener() {
            @Override
            public void onItemAcceptClick(int position) {
                String uid =list.get(position).getUid();
                UserDao.accept(uid, new UpdateStatusFriends() {
                    @Override
                    public boolean onSuccess(Boolean b) {
                        if(b!= true){
                            Toast.makeText(requireActivity(), "có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                        }
                        list.remove(position);
                        mAdapter.notifyItemRemoved(position);
                        return false;
                    }
                });

            }

            @Override
            public void onItemDeleteClick(int position) {

            }

        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        binding.recyclrview.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        binding.recyclrview.addItemDecoration(itemDecoration);
        binding.recyclrview.setAdapter(mAdapter);

    }

    public void getListFriendRequest() {
        UserDao.getFriendsRealtime(UserDao.getReceiverId(), "0", new GetInformationCallback() {
            @Override
            public void onGetListFriendsRequest(ArrayList<User> listUser) {
                conFigurationRecyclerView(listUser);
            }
        });
    }

    public interface UpdateStatusFriends{
        boolean onSuccess (Boolean b);
    }



}
