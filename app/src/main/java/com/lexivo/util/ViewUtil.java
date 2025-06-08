package com.lexivo.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.activity.ComponentActivity;
import androidx.activity.OnBackPressedCallback;

import com.google.android.material.card.MaterialCardView;

public abstract class ViewUtil {

    public static Spinner getSpinner(View v) {
        return (Spinner) ((MaterialCardView) v).getChildAt(0);
    }

    public static void openModal(ViewGroup modal) {
        modal.setVisibility(View.VISIBLE);
        View content = modal.getChildAt(0);
        content.setScaleX(0);
        content.setScaleY(0);
        content.animate().setDuration(100).scaleX(1).scaleY(1);
    }

    public static void closeModal(ViewGroup modal) {
        View content = modal.getChildAt(0);
        content.animate().setDuration(100).scaleX(0).scaleY(0).withEndAction(() -> modal.setVisibility(View.GONE));
    }
}
