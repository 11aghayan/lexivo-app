package com.lexivo.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lexivo.AddEditWordActivity;
import com.lexivo.R;
import com.lexivo.schema.Dictionary;
import com.lexivo.schema.Gender;
import com.lexivo.schema.Word;

import java.util.List;

public class AllWordsAdapter extends RecyclerView.Adapter<AllWordsAdapter.ViewHolder> {
    private static boolean isSoundPlaying = false;
    private final Dictionary dictionary;
    private final Context context;
    private final ConstraintLayout deleteModal;
    private List<Word> words;
    private final TextView textNoWords;

    public AllWordsAdapter(Dictionary dictionary, Context context, ConstraintLayout deleteModal, TextView textNoWords) {
        this.dictionary = dictionary;
        this.words = dictionary.getAlWords();
        this.context = context;
        this.deleteModal = deleteModal;
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
        Word word = words.get(position);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddEditWordActivity.class);
            intent.putExtra("word_id", word.getId());
            intent.putExtra("dictionary_id", dictionary.getId());
            context.startActivity(intent);
        });

        handleType(holder, word);
        handlePhoto(holder, word);
        handleGender(holder, word);
        handleTranslation(holder, word);
        handleOriginal(holder, word);
        handlePlural(holder, word);
        handlePast1(holder, word);
        handlePast2(holder, word);
        handleComment(holder, word);
        handleSound(holder, word);
        handleDelete(holder);
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    private String[] getGender(Gender gender) {
        switch(gender) {
            case MASCULINE:
                return new String[]{context.getString(R.string.text_gender_masculine), String.valueOf(context.getColor(R.color.gender_male))};
            case FEMININE:
                return new String[]{context.getString(R.string.text_gender_feminine), String.valueOf(context.getColor(R.color.gender_female))};
            case NEUTRAL:
                return new String[]{context.getString(R.string.text_gender_neutral), String.valueOf(context.getColor(R.color.gender_neutral))};
            case PERSONAL:
                return new String[]{context.getString(R.string.text_gender_personal), String.valueOf(context.getColor(R.color.gender_personal))};
            default:
                return new String[]{context.getString(R.string.text_gender_plural), String.valueOf(context.getColor(R.color.gender_plural))};
        }
    }

    private Drawable getPhoto(String photo) {
        if (photo == null) {
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.placeholder_photo, null);
        }
        //TODO: create a drawable from photo
        return null;
    }

    private Drawable getSoundIcon(String sound) {
        if (isSoundPlaying || sound == null) {
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.icon_sound_disabled, null);
        }
        return ResourcesCompat.getDrawable(context.getResources(), R.drawable.icon_sound, null);
    }

    private void handleType(@NonNull ViewHolder holder, Word word) {
        holder.type.setText(word.getType().toString());
    }

    private void handlePhoto(@NonNull ViewHolder holder, Word word) {
        holder.photo.setImageDrawable(getPhoto(word.getPhoto()));
    }

    private void handleGender(@NonNull ViewHolder holder, Word word) {
        if (word.getGender() != null) {
            String[] genderData = getGender(word.getGender());
            holder.gender.setVisibility(View.VISIBLE);
            holder.gender.setText(String.valueOf(genderData[0]));
            holder.gender.setTextColor(Integer.parseInt(genderData[1]));
        } else {
            holder.gender.setVisibility(View.GONE);
        }
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
            holder.pluralContainer.setVisibility(View.VISIBLE);
            holder.plural.setText(word.getPlural());
        } else {
            holder.pluralContainer.setVisibility(View.GONE);
        }
    }

    private void handlePast1(@NonNull ViewHolder holder, Word word) {
        if (word.getPast1() != null) {
            holder.past1Container.setVisibility(View.VISIBLE);
            holder.past1.setText(word.getPast1());
        } else {
            holder.past1Container.setVisibility(View.GONE);
        }
    }

    private void handlePast2(@NonNull ViewHolder holder, Word word) {
        if (word.getPast2() != null) {
            holder.past2Container.setVisibility(View.VISIBLE);
            holder.past2.setText(word.getPast2());
        } else {
            holder.past2Container.setVisibility(View.GONE);
        }
    }

    private void handleComment(@NonNull ViewHolder holder, Word word) {
        if (word.getComment() != null) {
            holder.comment.setVisibility(View.VISIBLE);
            holder.comment.setText(word.getComment());
        } else {
            holder.comment.setVisibility(View.GONE);
        }
    }

    private void handleSound(@NonNull ViewHolder holder, Word word) {
        holder.sound.setImageDrawable(getSoundIcon(word.getAudio()));

        holder.sound.setOnClickListener(v -> {
            if (isSoundPlaying || word.getAudio() == null) return;
            isSoundPlaying = true;
            notifyItemChanged(holder.getAdapterPosition());
//          TODO: implement actual sound playing
            isSoundPlaying = false;
            notifyItemChanged(holder.getAdapterPosition());
        });
    }

    private void handleDelete(@NonNull ViewHolder holder) {
        holder.btnDelete.setOnClickListener(v -> {
            deleteModal.setVisibility(View.VISIBLE);
            deleteModal.findViewById(R.id.btnDelete).setOnClickListener(_v -> {
                dictionary.deleteWord(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                deleteModal.setVisibility(View.GONE);
                if (words.isEmpty()) {
                    textNoWords.setVisibility(View.VISIBLE);
                }
            });
            deleteModal.findViewById(R.id.btnCancel).setOnClickListener(_v -> {
                deleteModal.setVisibility(View.GONE);
            });
        });
    }

    public void setWords(List<Word> words) {

        this.words = words;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView
            type, singular, additionOriginal, additionTranslation, plural, past1, past2, comment, translation, btnDelete, gender;
        private final ImageView photo, sound;
        private final LinearLayout pluralContainer, past1Container, past2Container;

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
            btnDelete = itemView.findViewById(R.id.btnDelete);
            gender = itemView.findViewById(R.id.gender);
            pluralContainer = itemView.findViewById(R.id.pluralContainer);
            past1Container = itemView.findViewById(R.id.past1Container);
            past2Container = itemView.findViewById(R.id.past2Container);
            photo = itemView.findViewById(R.id.photo);
            sound = itemView.findViewById(R.id.sound);
        }
    }
}
