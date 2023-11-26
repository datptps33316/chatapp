package com.datcute.chatapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.datcute.chatapplication.Search_Activity;
import com.datcute.chatapplication.adapter.ViewPagerAdapter;
import com.datcute.chatapplication.databinding.FragmentFriendBinding;
import com.google.android.material.tabs.TabLayout;


public class FragmentFriend extends Fragment {
private FragmentFriendBinding binding;
ViewPagerAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFriendBinding.inflate(getLayoutInflater());
        binding.tablayout.addTab(binding.tablayout.newTab().setText("Friend Request"));
        binding.tablayout.addTab(binding.tablayout.newTab().setText(" All Friend"));

        adapter = new ViewPagerAdapter(requireActivity());
        binding.viewpg.setAdapter(adapter);

        binding.addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Search_Activity.class);
                startActivity(intent);
            }
        });

       binding.viewpg.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
           @Override
           public void onPageSelected(int position) {
               super.onPageSelected(position);
               binding.tablayout.setScrollPosition(position,0,true);
           }
       });
        onclickTabLayout();
        return binding.getRoot();
    }
    public void onclickTabLayout(){
     binding.tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
         @Override
         public void onTabSelected(TabLayout.Tab tab) {
             binding.viewpg.setCurrentItem(tab.getPosition(),true);
         }

         @Override
         public void onTabUnselected(TabLayout.Tab tab) {

         }

         @Override
         public void onTabReselected(TabLayout.Tab tab) {

         }
     });
    }



}
