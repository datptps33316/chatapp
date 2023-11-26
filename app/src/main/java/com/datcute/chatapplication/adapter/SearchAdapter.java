package com.datcute.chatapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.datcute.chatapplication.DetailUserAcitivity;
import com.datcute.chatapplication.R;
import com.datcute.chatapplication.Search_Activity;
import com.datcute.chatapplication.databinding.ItemFriendBinding;
import com.datcute.chatapplication.databinding.ItemStatusBinding;
import com.datcute.chatapplication.model.User;
import com.google.common.collect.FluentIterable;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.UserViewHolder> {
    private List<User> list;
    private ItemFriendBinding binding;
    private static Context context;

    public SearchAdapter(List<User> list, Context context) {
        this.list = list;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemFriendBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new UserViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User item = list.get(position);
        UserViewHolder.createViewHolder(binding).bind(item);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailUserAcitivity.class);
                intent.putExtra("name",item.getName());
                intent.putExtra("img",String.valueOf(item.getImage()));
                intent.putExtra("uid",item.getUid());
                intent.putExtra("token",String.valueOf(item.getTokenFcm()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list.size() == 0) {
            return 0;
        }
        return list.size();
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
