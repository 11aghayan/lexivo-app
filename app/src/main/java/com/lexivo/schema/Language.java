package com.lexivo.schema;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.lexivo.R;
import com.lexivo.util.BitmapUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Language {
    private static final Map<String, Language> languages = new HashMap<>();
    private final String label;
    private final String flag;

    private Language(Context context, String label, int flagId) {
        this.label = label;
        this.flag = BitmapUtil.getBitmapStringFromDrawableId(context, flagId);
        languages.put(label, this);
    }

    public static Language get(String language) {
        return languages.get(language);
    }

    public String getLabel() {
        return label;
    }

    public Drawable getFlag(Context context) {
        return BitmapUtil.getDrawableFromBitmapString(context, this.flag);
    }

    public static void init(Context context) {
        new Language(context, "arabic", R.drawable.language_flag_arabic);
        new Language(context, "armenian", R.drawable.language_flag_armenian);
        new Language(context, "english", R.drawable.language_flag_english);
        new Language(context, "german", R.drawable.language_flag_german);
        new Language(context, "hungarian", R.drawable.language_flag_hungarian);
        new Language(context, "italian", R.drawable.language_flag_italian);
        new Language(context, "russian", R.drawable.language_flag_russian);
        new Language(context, "spanish", R.drawable.language_flag_spanish);
    }



    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Language language = (Language) o;
        return flag.equals(language.flag) && Objects.equals(label, language.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, flag);
    }

    @NonNull
    @Override
    public String toString() {
        return "Language{" +
                ", label='" + label + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}
