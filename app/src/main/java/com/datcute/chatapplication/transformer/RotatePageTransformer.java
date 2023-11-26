package com.datcute.chatapplication.transformer;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

public class RotatePageTransformer implements ViewPager2.PageTransformer {
    private static final float MAX_ROTATION = 20.0f;

    @Override
    public void transformPage(@NonNull View page, float position) {
        float normalizedPosition = Math.abs(Math.abs(position) - 1);
        page.setRotation(MAX_ROTATION * normalizedPosition);
    }
}

