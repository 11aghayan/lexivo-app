package com.lexivo.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lexivo.DictionaryActivity;
import com.lexivo.R;
import com.lexivo.exception.UnableToSaveException;
import com.lexivo.schema.Dictionary;
import com.lexivo.schema.Language;
import com.lexivo.exception.DuplicateValueException;
import com.lexivo.util.IntentUtil;
import com.lexivo.util.StringUtil;

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.ViewHolder> {
    private final Context mainContext;

    public DictionaryAdapter(Context mainContext) {
        this.mainContext = mainContext;
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
        holder.dictionaryButton.setForeground(dictionary.getLanguage().getFlag(mainContext));
        holder.dictionaryButton.setForegroundGravity(Gravity.FILL);
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

    public void addDictionary(Dictionary dictionary) throws DuplicateValueException, UnableToSaveException {
        Dictionary.addDictionary(dictionary, mainContext);
        notifyItemInserted(Dictionary.getDictionaries().size() - 1);
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
