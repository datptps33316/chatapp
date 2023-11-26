package com.datcute.chatapplication.adapter;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.datcute.chatapplication.databinding.ItemAvatarUserBinding;
import com.datcute.chatapplication.model.User;

import java.util.ArrayList;
import java.util.List;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    public List<User> mListWareHouse ;
    ItemAvatarUserBinding binding;

    public  UserAdapter( @NonNull List<User> listWareHouse) {
        this.mListWareHouse =listWareHouse ;
    }

    @NonNull
    @Override
    public  UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemAvatarUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new UserViewHolder(binding);

    }


    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User item = mListWareHouse.get(position);
        UserAdapter.UserViewHolder.createViewHolder(binding).bind(item);

    }

    @Override
    public int getItemCount() {
        if(mListWareHouse.size() == 0){
            return 0 ;
        }
        return mListWareHouse.size();
    }


    public static class UserViewHolder extends RecyclerView.ViewHolder {
        private  ItemAvatarUserBinding binding;

        public static   UserViewHolder createViewHolder(ItemAvatarUserBinding binding){
            return  new UserViewHolder(binding);
        }

        public UserViewHolder(ItemAvatarUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public  void bind(User item){
        binding.nameUser.setText(item.getName());
        }

    }

}
