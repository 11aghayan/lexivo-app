package com.lexivo.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lexivo.R;
import com.lexivo.data.Dictionary;
import com.lexivo.data.Gender;
import com.lexivo.data.Word;
import com.lexivo.data.WordType;
import com.lexivo.util.ListUtil;

import java.util.List;
import java.util.stream.Collectors;

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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Word word = words.get(position);
        holder.type.setText(
                ListUtil.joinElementsIntoString(
                        word.getType().stream().map(WordType::getString).collect(Collectors.toList()),
                        ",")
        );
        holder.photo.setImageDrawable(getPhoto(word.getPhoto()));
        holder.sound.setImageDrawable(getSoundIcon(word.getSound()));
        holder.translation.setText(word.getTranslation().getValue());
        if (word.getGender() != null) {
            String[] genderData = getGender(word.getGender());
            holder.gender.setVisibility(View.VISIBLE);
            holder.gender.setText(String.valueOf(genderData[0]));
            holder.gender.setTextColor(Integer.parseInt(genderData[1]));
        } else {
            holder.gender.setVisibility(View.GONE);
        }
        if (word.getTranslation().getDetails() != null) {
            holder.additionTranslation.setVisibility(View.VISIBLE);
            holder.additionTranslation.setText("(" + word.getTranslation().getDetails() + ")");

        } else {
            holder.additionTranslation.setVisibility(View.GONE);
        }
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
        if (word.getPlural() != null) {
            holder.plural.setVisibility(View.VISIBLE);
            holder.pluralHeader.setVisibility(View.VISIBLE);
            holder.plural.setText(word.getPlural());
        } else {
            holder.pluralHeader.setVisibility(View.GONE);
            holder.plural.setVisibility(View.GONE);
        }
        if (word.getComment() != null) {
            holder.comment.setVisibility(View.VISIBLE);
            holder.comment.setText(word.getComment());
        } else {
            holder.comment.setVisibility(View.GONE);
        }
        holder.sound.setOnClickListener(v -> {
            if (isSoundPlaying || word.getSound() == null) return;
            isSoundPlaying = true;
            notifyItemChanged(holder.getAdapterPosition());
//          TODO: implement actual sound playing
            isSoundPlaying = false;
            notifyItemChanged(holder.getAdapterPosition());
        });
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
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.placeholder, null);
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

    public void setWords(List<Word> words) {
        this.words = words;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView
            type, singular, additionOriginal, additionTranslation, plural, comment, translation, btnDelete, gender, pluralHeader;
        private final ImageView photo, sound;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.type);
            singular = itemView.findViewById(R.id.singular);
            additionOriginal = itemView.findViewById(R.id.additionOriginal);
            additionTranslation = itemView.findViewById(R.id.additionTranslation);
            plural = itemView.findViewById(R.id.plural);
            comment = itemView.findViewById(R.id.comment);
            translation = itemView.findViewById(R.id.translation);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            gender = itemView.findViewById(R.id.gender);
            pluralHeader = itemView.findViewById(R.id.pluralHeader);
            photo = itemView.findViewById(R.id.photo);
            sound = itemView.findViewById(R.id.sound);
        }
    }
}
