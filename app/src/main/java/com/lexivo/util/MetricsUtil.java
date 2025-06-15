package com.lexivo.util;

import android.content.Context;

public abstract class MetricsUtil {
    public static float convertPxToDp(Context context, float px) {
        final float density = context.getResources().getDisplayMetrics().density;
        return px / density;
    }
}
