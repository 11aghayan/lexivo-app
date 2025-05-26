package com.lexivo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lexivo.R;
import com.lexivo.data.Dictionary;
import com.lexivo.data.Language;
import com.lexivo.exception.DuplicateValueException;
import com.lexivo.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyDictionariesAdapter extends RecyclerView.Adapter<MyDictionariesAdapter.ViewHolder> {
    private final List<Dictionary> dictionaries;
    private final View view;

    public MyDictionariesAdapter(View view) {
        this.view = view;
        this.dictionaries = new ArrayList<>();
        toggleNoItemsText();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.dictionary,
                        parent,
                        false
                );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Dictionary dictionary = dictionaries.get(position);
        Language language = Language.get(dictionary.getLanguage());
        holder.dictionaryButton.setForeground(ResourcesCompat.getDrawable(view.getResources(), language.getFlag(), null));
        holder.expressionCount.setText(String.valueOf(dictionary.getExpressionCount()));
        holder.wordCount.setText(String.valueOf(dictionary.getWordCount()));
        holder.language.setText(StringUtil.capitalize(language.getLabel()));
    }

    @Override
    public int getItemCount() {
        return dictionaries.size();
    }

    public void addDictionary(Dictionary dictionary) throws DuplicateValueException {
        boolean isDuplicate = false;
        for (var d : dictionaries) {
            isDuplicate = Objects.equals(dictionary, d);
        }
        if (isDuplicate) {
            throw new DuplicateValueException();
        }
        dictionaries.add(dictionary);
        notifyItemInserted(dictionaries.size() - 1);
    }

    private void toggleNoItemsText() {
        view.findViewById(R.id.noDictionariesText).setVisibility(
                dictionaries.isEmpty() ? View.VISIBLE : View.GONE
        );
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView language, wordCount, expressionCount;
        private final Button dictionaryButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            language = itemView.findViewById(R.id.languageOnDictionaryCard);
            wordCount = itemView.findViewById(R.id.wordCountOnDictionaryCard);
            expressionCount = itemView.findViewById(R.id.expressionCountOnDictionaryCard);
            dictionaryButton = itemView.findViewById(R.id.dictionaryButton);
        }
    }
}
