package com.lexivo.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.lexivo.R;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Toast {
    private static final int SHOWING_TIME_MS = 4000;
    private static final int ANIMATION_DURATION_MS = 140;
    private static boolean canShowNext = true;
    private static int bgColor;
    private static ConstraintLayout toastLayout;
    private static List<Tuple<Activity, String>> queue = new LinkedList<>();

    public static void success(Activity activity, String message) {
        bgColor = activity.getResources().getColor(R.color.bg_toast_success, null);
        toast(activity, message);
    }

    public static void warning(Activity activity, String message) {
        bgColor = activity.getResources().getColor(R.color.bg_toast_warning, null);
        toast(activity, message);
    }

    public static void error(Activity activity, String message) {
        bgColor = activity.getResources().getColor(R.color.bg_toast_error, null);
        toast(activity, message);
    }

    @SuppressLint("ClickableViewAccessibility")
    private static void toast(Activity activity, String message) {
        if (!canShowNext) {
            addToQueue(activity, message);
        };

        canShowNext = false;
        toastLayout = activity.findViewById(R.id.toast);
        toastLayout.post(() -> {
            toastLayout.setBackgroundColor(bgColor);
            ((TextView) toastLayout.findViewById(R.id.toastText)).setText(message);

            float translationStartingPoint = toastLayout.getHeight() + 60;
            toastLayout.setTranslationY(translationStartingPoint);

            toastLayout.setOnTouchListener(new OnSwipeTouchListenerToast(activity){
                @Override
                public void onSwipeBottom() {
                    closingLogic(translationStartingPoint);
                }
            });

            toastLayout.animate().setDuration(ANIMATION_DURATION_MS).translationY(-20).withEndAction(() -> {
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        closingLogic(translationStartingPoint);
                    }
                };
                new Timer(true).schedule(task, SHOWING_TIME_MS);
            });
        });
    }

    private static void closingLogic(float translationStartingPoint) {
        if (canShowNext) return;
        toastLayout.animate().setDuration(ANIMATION_DURATION_MS).translationY(translationStartingPoint);
        canShowNext = true;
        var nextToast = getNextInQueue();
        if (nextToast != null) {
            toast(nextToast.getFirstElement(), nextToast.getSecondElement());
        }
    }

    private static void addToQueue(Activity activity, String message) {
        queue.add(new Tuple<>(activity, message));
    }

    private static Tuple<Activity, String> getNextInQueue() {
        if (queue.isEmpty()) return null;
        var tuple =  queue.get(0);
        queue.remove(0);
        return tuple;
    }
}
