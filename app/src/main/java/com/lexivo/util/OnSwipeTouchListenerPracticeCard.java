package com.lexivo.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OnSwipeTouchListenerPracticeCard implements View.OnTouchListener {
    private final GestureDetector gestureDetector;

    public OnSwipeTouchListenerPracticeCard(Context context) {
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
                final int SWIPE_THRESHOLD = 200;
                final int SWIPE_VELOCITY_THRESHOLD = 100;
                if (
                        Math.abs(diffX) > Math.abs(diffY)
                        && Math.abs(diffX) > SWIPE_THRESHOLD
                        && Math.abs(velocityX) > Math.abs(SWIPE_VELOCITY_THRESHOLD)
                ) {
                    if (diffX > 0) {
                        onSwipeRight();
                    }
                    else {
                        onSwipeLeft();
                    }
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean onSingleTapUp(@NonNull MotionEvent e) {
            onClick();
            return true;
        }
    }

    public void onSwipeRight() {}
    public void onSwipeLeft() {}
    public void onClick(){};
}