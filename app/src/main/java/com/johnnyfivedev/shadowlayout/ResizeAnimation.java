package com.johnnyfivedev.shadowlayout;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ResizeAnimation extends Animation {
    private int newWidth;
    private int startWidth;
    private View targetView;


    public ResizeAnimation(View view, int newWidth) {
        targetView = view;
        this.newWidth = newWidth;
        startWidth = view.getWidth();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        targetView.getLayoutParams().width = startWidth + (int) ((newWidth - startWidth) * interpolatedTime);
        targetView.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
