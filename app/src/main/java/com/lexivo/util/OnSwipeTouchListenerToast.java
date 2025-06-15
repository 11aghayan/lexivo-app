package com.lexivo.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OnSwipeTouchListenerToast implements View.OnTouchListener {
    private final GestureDetector gestureDetector;

    public OnSwipeTouchListenerToast(Context context) {
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(@NonNull MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
            if (e1 != null) {
                final float diffX = e2.getX() - e1.getX();
                final float diffY = e2.getY() - e1.getY();
                final int SWIPE_THRESHOLD = 0;
                final int SWIPE_VELOCITY_THRESHOLD = 0;
                if (
                        Math.abs(diffY) > Math.abs(diffX)
                                && Math.abs(diffY) > SWIPE_THRESHOLD
                                && Math.abs(velocityY) > Math.abs(SWIPE_VELOCITY_THRESHOLD)
                                && diffY > 0
                ) {
                    onSwipeBottom();
                    return true;
                }
            }
            return false;
        }
    }

    public void onSwipeBottom() {}
}