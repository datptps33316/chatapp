package com.datcute.chatapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.datcute.chatapplication.R;
import com.datcute.chatapplication.databinding.ItemFriendrequestBinding;
import com.datcute.chatapplication.interfacce.onItemRcvClickListener;
import com.datcute.chatapplication.model.User;

import java.util.ArrayList;
import java.util.List;

public class FriendRqAdapter extends RecyclerView.Adapter<FriendRqAdapter.UserViewHolder> {
    List<User> mFriendRequestList;
    final onItemRcvClickListener mItemClickListener;
    Context context;


    public void setList(List<User> list) {
        this.mFriendRequestList = list;
    }
    public FriendRqAdapter(List<User> FriendRequestList, Context context, onItemRcvClickListener itemClickListener) {
        this.mFriendRequestList = FriendRequestList;
        this.context = context;
        this.mItemClickListener = itemClickListener;

    }
    public void updateData(ArrayList<User> newList) {
        this.mFriendRequestList = newList;
    }


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friendrequest, parent, false);
        return new UserViewHolder(view,mItemClickListener);

    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, @SuppressLint("RecyclerView") int position) {
        User item = mFriendRequestList.get(holder.getAdapterPosition());
        holder.nameUser.setText(item.getName());
        holder.ngay.setText(String.valueOf(item.getTime()+" ngày"));
        Glide.with(context).load(item.getImage()).error(R.drawable.img_5).into(holder.imageUser);

    }

    @Override
    public int getItemCount() {
        return mFriendRequestList.size() != 0 ? mFriendRequestList.size() : 0;
    }


    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageUser;
        public TextView nameUser;
        public Button xnButton;
        TextView ngay;

        public UserViewHolder(View view, onItemRcvClickListener mItemClickListener) {
            super(view);
            imageUser = view.findViewById(R.id.imageuser1);
            nameUser = view.findViewById(R.id.nameuser1);
            xnButton = view.findViewById(R.id.xn);
            ngay = view.findViewById(R.id.ngay);
            xnButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemAcceptClick(getBindingAdapterPosition());
                }
            });
        }
    }


}


