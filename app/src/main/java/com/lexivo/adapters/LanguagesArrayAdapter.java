package com.lexivo.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.lexivo.R;

public class LanguagesArrayAdapter {
    public final ArrayAdapter<CharSequence> adapter;

    public LanguagesArrayAdapter(Context context) {
        this.adapter = ArrayAdapter.createFromResource(context, R.array.languages, R.layout.language_spinner_item);
    }
}
