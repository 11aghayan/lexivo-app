package com.lexivo.util;

import android.content.Context;
import android.widget.Toast;

public abstract class ToastUtil {
    public static void unableToSave(Context context) {
        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
    }
}
