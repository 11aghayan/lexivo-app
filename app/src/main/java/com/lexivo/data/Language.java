package com.lexivo.data;

import androidx.annotation.NonNull;

import com.lexivo.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Language {
    private static final Map<String, Language> languages = new HashMap<>();
    private final String label;
    private final int flag;

    private Language(String label, int flag) {
        this.label = label;
        this.flag = flag;
        languages.put(label, this);
    }

    public static Language get(String language) {
        return languages.get(language);
    }

    public String getLabel() {
        return label;
    }

    public int getFlag() {
        return flag;
    }

    public static void init() {
        new Language("arabic", R.drawable.language_flag_arabic);
        new Language("armenian", R.drawable.language_flag_armenian);
        new Language("english", R.drawable.language_flag_english);
        new Language("german", R.drawable.language_flag_german);
        new Language("hungarian", R.drawable.language_flag_hungarian);
        new Language("italian", R.drawable.language_flag_italian);
        new Language("russian", R.drawable.language_flag_russian);
        new Language("spanish", R.drawable.language_flag_spanish);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Language language = (Language) o;
        return flag == language.flag && Objects.equals(label, language.label);
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
