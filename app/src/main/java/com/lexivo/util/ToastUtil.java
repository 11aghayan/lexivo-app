package com.lexivo.util;

import android.content.Context;
import android.widget.Toast;

public abstract class ToastUtil {
    public static void unableToSave(Context context) {
        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
    }

    public static void dictionaryExported(Context context) {
        Toast.makeText(context, "Dictionary is exported", Toast.LENGTH_SHORT).show();
    }

    public static void dictionaryNotExported(Context context) {
        Toast.makeText(context, "Dictionary could not be exported", Toast.LENGTH_SHORT).show();
    }
}
