package com.datcute.chatapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.datcute.chatapplication.R;
import com.datcute.chatapplication.databinding.ItemAvatarUserBinding;
import com.datcute.chatapplication.databinding.ItemFriendBinding;
import com.datcute.chatapplication.interfacce.onItemRcvClickListener;
import com.datcute.chatapplication.model.User;

import java.util.List;


public class AllFriendAdapter extends RecyclerView.Adapter<AllFriendAdapter.UserViewHolder>  {

    public List<User> mListWareHouse;
    ItemFriendBinding binding;
    final onItemRcvClickListener mItemClickListener;

    static Context context;

    public AllFriendAdapter(@NonNull List<User> listWareHouse, Context context ,onItemRcvClickListener onItemRcvClickListener) {
        this.mListWareHouse = listWareHouse;
        this.context = context;
        this.mItemClickListener = onItemRcvClickListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemFriendBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new UserViewHolder(binding ,mItemClickListener);

    }


    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User item = mListWareHouse.get(position);
        holder.bind(item);
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
        onItemRcvClickListener onItemRcvClickListener;



        public UserViewHolder(ItemFriendBinding binding ,onItemRcvClickListener onItemRcvClickListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.onItemRcvClickListener = onItemRcvClickListener;
        }

        public void bind(User item) {
            binding.nameUser.setText(item.getName());
            Glide.with(context).load(item.getImage()).error(R.drawable.img_5).into(binding.imageUser);
            binding.nhantin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemRcvClickListener.onItemAcceptClick(getBindingAdapterPosition());
                }
            });
        }

    }

}
