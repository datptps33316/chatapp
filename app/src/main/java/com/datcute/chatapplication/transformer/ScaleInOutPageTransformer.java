package com.datcute.chatapplication.transformer;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class ScaleInOutPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.85f;

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();

        if (position < -1) {
            view.setScaleX(MIN_SCALE);
            view.setScaleY(MIN_SCALE);
        } else if (position <= 1) {
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                view.setTranslationX(horzMargin - vertMargin / 2);
            } else {
                view.setTranslationX(-horzMargin + vertMargin / 2);
            }
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        } else {
            view.setScaleX(MIN_SCALE);
            view.setScaleY(MIN_SCALE);
        }
    }
}
