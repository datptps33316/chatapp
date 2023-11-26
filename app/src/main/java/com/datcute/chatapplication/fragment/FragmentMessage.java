package com.datcute.chatapplication.fragment;

import android.graphics.Bitmap;

import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.datcute.chatapplication.R;
import com.datcute.chatapplication.databinding.FragmentMessageBinding;
import com.datcute.chatapplication.model.User;


public class FragmentMessage extends Fragment {

    private FragmentMessageBinding binding;
    User users;

    public FragmentMessage(User users) {
        this.users = users;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMessageBinding.inflate(getLayoutInflater());
        setInForUser(users);

        return binding.getRoot();

    }

    public void setInForUser(User user){
        if(users!= null){
            Glide.with(requireActivity()).load(users.getImage()).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.img_5).into(binding.imageavt);
        }else {
            Drawable drawable = getResources().getDrawable(R.drawable.img_5);
            Bitmap bitmap = drawableToBitmap(drawable);
            binding.imageavt.setImageBitmap(bitmap);
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




}




