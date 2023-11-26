package com.datcute.chatapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.datcute.chatapplication.R;
import com.datcute.chatapplication.databinding.ItemAvatarUserBinding;
import com.datcute.chatapplication.databinding.ItemFriendBinding;
import com.datcute.chatapplication.model.User;

import java.util.List;


public class AllFriendAdapter extends RecyclerView.Adapter<AllFriendAdapter.UserViewHolder> {

    public List<User> mListWareHouse;
    ItemFriendBinding binding;
    static Context context;

    public AllFriendAdapter(@NonNull List<User> listWareHouse, Context context) {
        this.mListWareHouse = listWareHouse;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemFriendBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new UserViewHolder(binding);

    }


    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User item = mListWareHouse.get(position);
        AllFriendAdapter.UserViewHolder.createViewHolder(binding).bind(item);

    }

    @Override
    public int getItemCount() {
        if (mListWareHouse.size() == 0) {
            return 0;
        }
        return mListWareHouse.size();
    }


    public static class UserViewHolder extends RecyclerView.ViewHolder {
        private ItemFriendBinding binding;

        public static UserViewHolder createViewHolder(ItemFriendBinding binding) {
            return new UserViewHolder(binding);
        }

        public UserViewHolder(ItemFriendBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(User item) {
            binding.nameUser.setText(item.getName());
            Glide.with(context).load(item.getImage()).error(R.drawable.img_5).into(binding.imageUser);
        }

    }

}
