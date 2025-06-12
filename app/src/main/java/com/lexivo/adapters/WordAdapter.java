package com.lexivo.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lexivo.AddEditWordActivity;
import com.lexivo.R;
import com.lexivo.exception.UnableToSaveException;
import com.lexivo.schema.Dictionary;
import com.lexivo.schema.Word;
import com.lexivo.util.IntentUtil;
import com.lexivo.util.ToastUtil;
import com.lexivo.util.ViewUtil;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder> {
    private final Dictionary dictionary;
    private final Context context;
    private final ConstraintLayout modalDelete;
    private final TextView textNoWords;

    public WordAdapter(Dictionary dictionary, Context context, ConstraintLayout modalDelete, TextView textNoWords) {
        this.dictionary = dictionary;
        this.context = context;
        this.modalDelete = modalDelete;
        this.textNoWords = textNoWords;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.word,
                        parent,
                        false
                );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Word word = dictionary.getWordsFiltered().get(position);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddEditWordActivity.class);
            intent.putExtra(IntentUtil.KEY_WORD_ID, word.getId());
            intent.putExtra(IntentUtil.KEY_DICTIONARY_ID, dictionary.getId());
            context.startActivity(intent);
        });

        handleType(holder, word);
        handleGender(holder, word);
        handleTranslation(holder, word);
        handleOriginal(holder, word);
        handlePlural(holder, word);
        handlePast1(holder, word);
        handlePast2(holder, word);
        handleComment(holder, word);
        handleDelete(holder);
    }

    @Override
    public int getItemCount() {
        return dictionary.getFilteredWordsCount();
    }



    private void handleType(@NonNull ViewHolder holder, Word word) {
        holder.type.setText(word.getType().toString());
    }

    private void handleGender(@NonNull ViewHolder holder, Word word) {
        if (word.getGender() != null) {
            String[] genderData = ViewUtil.getGenderStringAndColorArray(context, word.getGender());
            if (genderData[0] != null && genderData[1] != null) {
                holder.gender.setVisibility(View.VISIBLE);
                holder.gender.setText(genderData[0]);
                holder.gender.setTextColor(Integer.parseInt(genderData[1]));
            }
            else
                holder.gender.setVisibility(View.GONE);
        } else
            holder.gender.setVisibility(View.GONE);
    }

    @SuppressLint("SetTextI18n")
    private void handleTranslation(@NonNull ViewHolder holder, Word word) {
        holder.translation.setText(word.getTranslation().getValue());
        if (word.getTranslation().getDetails() != null) {
            holder.additionTranslation.setVisibility(View.VISIBLE);
            holder.additionTranslation.setText("(" + word.getTranslation().getDetails() + ")");

        } else {
            holder.additionTranslation.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetTextI18n")
    private void handleOriginal(@NonNull ViewHolder holder, Word word) {
        if (word.getOriginal().getValue() != null) {
            holder.singular.setText(word.getOriginal().getValue());
        } else {
            holder.singular.setTextSize(12);
            holder.singular.setText("Only plural");
            holder.singular.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.secondary_text, null));
        }

        if (word.getOriginal().getDetails() != null) {
            holder.additionOriginal.setVisibility(View.VISIBLE);
            holder.additionOriginal.setText("(" + word.getOriginal().getDetails() + ")");
        } else {
            holder.additionOriginal.setVisibility(View.GONE);
        }
    }

    private void handlePlural(@NonNull ViewHolder holder, Word word) {
        if (word.getPlural() != null) {
            holder.plural.setVisibility(View.VISIBLE);
            holder.plural.setText(word.getPlural());
        } else {
            holder.plural.setVisibility(View.GONE);
        }
    }

    private void handlePast1(@NonNull ViewHolder holder, Word word) {
        if (word.getPast1() != null) {
            holder.past1.setVisibility(View.VISIBLE);
            holder.past1.setText(word.getPast1());
        } else {
            holder.past1.setVisibility(View.GONE);
        }
    }

    private void handlePast2(@NonNull ViewHolder holder, Word word) {
        if (word.getPast2() != null) {
            holder.past2.setVisibility(View.VISIBLE);
            holder.past2.setText(word.getPast2());
        } else {
            holder.past2.setVisibility(View.GONE);
        }
    }

    private void handleComment(@NonNull ViewHolder holder, Word word) {
        if (word.getComment() != null) {
            holder.separatorLower.setVisibility(View.VISIBLE);
            holder.comment.setVisibility(View.VISIBLE);
            holder.comment.setText(word.getComment());
        } else {
            holder.separatorLower.setVisibility(View.GONE);
            holder.comment.setVisibility(View.GONE);
        }
    }

    private void handleDelete(@NonNull ViewHolder holder) {
        holder.btnDelete.setOnClickListener(v -> {
            ViewUtil.openModal(modalDelete);

            modalDelete.findViewById(R.id.btnDelete).setOnClickListener(_v -> {
                try {
                    dictionary.deleteWord(holder.getLayoutPosition(), context);
                    notifyItemRemoved(holder.getLayoutPosition());
                    if (dictionary.getFilteredWordsCount() == 0) {
                        textNoWords.setVisibility(View.VISIBLE);
                    }
                }
                catch (UnableToSaveException utse) {
                    ToastUtil.unableToSave(context);
                }
                finally {
                    ViewUtil.closeModal(modalDelete);
                }
            });
            modalDelete.findViewById(R.id.btnCancel).setOnClickListener(_v -> ViewUtil.closeModal(modalDelete));
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView
            type, singular, additionOriginal, additionTranslation, plural, past1, past2, comment, translation, btnDelete, gender;
        private final View separatorLower;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.type);
            singular = itemView.findViewById(R.id.singular);
            additionOriginal = itemView.findViewById(R.id.additionOriginal);
            additionTranslation = itemView.findViewById(R.id.additionTranslation);
            plural = itemView.findViewById(R.id.plural);
            past1 = itemView.findViewById(R.id.past1);
            past2 = itemView.findViewById(R.id.past2);
            comment = itemView.findViewById(R.id.comment);
            translation = itemView.findViewById(R.id.translation);
            btnDelete = itemView.findViewById(R.id.btnOpenDeleteModal);
            gender = itemView.findViewById(R.id.gender);
            separatorLower = itemView.findViewById(R.id.separatorLower);
        }
    }
}
