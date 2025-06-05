package com.lexivo.util;

import android.view.View;
import android.widget.Spinner;

import com.google.android.material.card.MaterialCardView;

public abstract class ViewUtil {

    public static Spinner getSpinner(View v) {
        return (Spinner) ((MaterialCardView) v).getChildAt(0);
    }
}
