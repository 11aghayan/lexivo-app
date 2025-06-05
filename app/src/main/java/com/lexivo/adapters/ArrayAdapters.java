package com.lexivo.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.lexivo.R;
import com.lexivo.schema.Gender;
import com.lexivo.schema.WordType;
import com.lexivo.util.StringUtil;

import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class ArrayAdapters {
    public static android.widget.ArrayAdapter<CharSequence> languagesAdapter(Context context) {
        return android.widget.ArrayAdapter.createFromResource(context, R.array.languages, R.layout.spinner_selected_item);
    }

    public static android.widget.ArrayAdapter<String> wordTypeAdapter(Context context) {
        var data = Arrays.stream(WordType.values()).map(wt -> StringUtil.capitalize(wt.toString())).collect(Collectors.toList());
        return new ArrayAdapter<>(context, R.layout.spinner_selected_item, data);
    }

    public static android.widget.ArrayAdapter<String> wordGenderAdapter(Context context) {
        var data = Arrays.stream(Gender.values()).map(g -> StringUtil.capitalize(g.toString().toLowerCase())).collect(Collectors.toList());
        return new ArrayAdapter<>(context, R.layout.spinner_selected_item, data);
    }
}
