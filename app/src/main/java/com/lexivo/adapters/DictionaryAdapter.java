package com.lexivo.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lexivo.DictionaryActivity;
import com.lexivo.MainActivity;
import com.lexivo.R;
import com.lexivo.schema.Dictionary;
import com.lexivo.schema.Language;
import com.lexivo.exception.DuplicateValueException;
import com.lexivo.util.IntentUtil;
import com.lexivo.util.StringUtil;

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.ViewHolder> {
    private final View view;
    private final Context mainContext;

    public DictionaryAdapter(View view, Context mainContext) {
        this.view = view;
        this.mainContext = mainContext;
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
        Dictionary dictionary = Dictionary.getDictionaries().get(position);
        Language language = dictionary.getLanguage();
        holder.dictionaryButton.setForeground(ResourcesCompat.getDrawable(view.getResources(), language.getFlag(), null));
        holder.expressionCount.setText(String.valueOf(dictionary.getExpressionCount()));
        holder.wordCount.setText(String.valueOf(dictionary.getAllWordsCount()));
        holder.language.setText(StringUtil.capitalize(language.getLabel()));
        holder.dictionaryButton.setOnClickListener(v -> {
            Intent intent = new Intent(mainContext, DictionaryActivity.class);
            intent.putExtra(IntentUtil.KEY_DICTIONARY_ID, dictionary.getId());
            mainContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return Dictionary.getDictionaries().size();
    }

    public void addDictionary(Dictionary dictionary) throws DuplicateValueException {
        Dictionary.addDictionary(dictionary);
        notifyItemInserted(Dictionary.getDictionaries().size() - 1);
    }

    private void toggleNoItemsText() {
        view.findViewById(R.id.noDictionariesText).setVisibility(
            Dictionary.getDictionaries().isEmpty() ? View.VISIBLE : View.GONE
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
