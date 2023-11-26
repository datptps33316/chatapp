package com.datcute.chatapplication.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.datcute.chatapplication.fragment.FragmentAllFriend;
import com.datcute.chatapplication.fragment.FragmentFriendRq;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new FragmentFriendRq();
            case 1:
                return  new FragmentAllFriend();
            default:
                new FragmentFriendRq();
        }
        return   new FragmentFriendRq();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
