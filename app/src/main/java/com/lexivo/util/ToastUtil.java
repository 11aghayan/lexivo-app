package com.lexivo.util;

import android.content.Context;
import android.widget.Toast;

public abstract class ToastUtil {
    public static void unableToSave(Context context) {
        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
    }

    public static void dictionaryExported(Context context) {
        Toast.makeText(context, "Dictionary exported", Toast.LENGTH_SHORT).show();
    }

    public static void dictionaryNotExported(Context context) {
        Toast.makeText(context, "Unable to import dictionary", Toast.LENGTH_SHORT).show();
    }

    public static void dictionaryImported(Context context) {
        Toast.makeText(context, "Dictionary imported", Toast.LENGTH_SHORT).show();
    }

    public static void dictionaryNotImported(Context context) {
        Toast.makeText(context, "Unable to import dictionary", Toast.LENGTH_SHORT).show();
    }

    public static void dictionaryNotImported(Context context, boolean isDuplicate) {
        if (isDuplicate)
            Toast.makeText(context, "Dictionary already exists", Toast.LENGTH_SHORT).show();
        else
            dictionaryImported(context);
    }
}
