package com.lexivo.schema;

import androidx.annotation.NonNull;

import com.lexivo.util.HashUtil;

import java.util.Objects;
import java.util.UUID;

public abstract class LanguageElement implements ObjectContainingId {
    private final String id;
    private final Dictionary dictionary;
    private String hash;
    private Text original;
    private Text translation;
    private String comment;
    private int lastGuessDate;

    public LanguageElement(Dictionary dictionary, Text original, Text translation, String comment) {
        this.id = UUID.randomUUID().toString();
        this.dictionary = dictionary;
        this.original = original;
        this.translation = translation;
        this.comment = comment;
        updateHash();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public String getHash() {
        return hash;
    }

    public Text getOriginal() {
        return original;
    }

    public Text getTranslation() {
        return translation;
    }
    public int getLastGuessDate() {
        return lastGuessDate;
    }

    public void setOriginal(Text original) {
        this.original = original;
        updateHash();
    }

    public void setTranslation(Text translation) {
        this.translation = translation;
        updateHash();
    }

    private void updateHash() {
        this.hash = HashUtil.generateMd5HashFromString(original.getValue() + original.getDetails() + translation.getValue() + original.getDetails());
    }

    public void setLastGuessDate(int lastGuessDate) {
        this.lastGuessDate = lastGuessDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LanguageElement that = (LanguageElement) o;
        return Objects.equals(dictionary, that.dictionary) && Objects.equals(hash, that.hash) && Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dictionary, hash, original, translation, lastGuessDate);
    }

    @NonNull
    @Override
    public String toString() {
        return "LanguageElement{" +
                "id='" + id + '\'' +
                ", dictionary='" + dictionary + '\'' +
                ", hash='" + hash + '\'' +
                ", original=" + original +
                ", translation=" + translation +
                ", comment='" + comment + '\'' +
                ", lastGuessDate=" + lastGuessDate +
                '}';
    }
}
