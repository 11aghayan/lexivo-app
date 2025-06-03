package com.lexivo.data;

import androidx.annotation.NonNull;

import com.lexivo.util.HashUtil;

import java.util.Objects;
import java.util.UUID;

public abstract class LanguageElement implements ObjectContainingId {
    private final String id;
    private final String dictionary;
    private String hash;
    private Text original;
    private Text translation;
    private String photo;
    private String sound;
    private String comment;
    private int lastGuessDate;

    public LanguageElement(String dictionaryId, Text original, Text translation, String photo, String sound, String comment) {
        this.id = UUID.randomUUID().toString();
        this.dictionary = dictionaryId;
        this.original = original;
        this.translation = translation;
        this.photo = photo;
        this.sound = sound;
        this.hash = HashUtil.generateMd5HashFromString(original.getValue() + translation.getValue());
        this.comment = comment;
    }

    public LanguageElement(String dictionaryId, Text original, Text translation, String comment) {
        this.id = UUID.randomUUID().toString();
        this.dictionary = dictionaryId;
        this.original = original;
        this.translation = translation;
        this.hash = HashUtil.generateMd5HashFromString(original.getValue() + translation.getValue());
        this.comment = comment;
    }

    public LanguageElement(String dictionaryId, Text original, Text translation) {
        this.id = UUID.randomUUID().toString();
        this.dictionary = dictionaryId;
        this.original = original;
        this.translation = translation;
        this.hash = HashUtil.generateMd5HashFromString(original.getValue() + translation.getValue());
    }

    public void setHash(String hash) {
        this.hash = hash;
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

    public String getDictionary() {
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

    public String getPhoto() {
        return photo;
    }

    public String getSound() {
        return sound;
    }

    public int getLastGuessDate() {
        return lastGuessDate;
    }

    public void setOriginal(Text original) {
        this.original = original;
        this.hash = HashUtil.generateMd5HashFromString(original.getValue() + translation.getValue());
    }

    public void setTranslation(Text translation) {
        this.translation = translation;
        this.hash = HashUtil.generateMd5HashFromString(original.getValue() + translation.getValue());
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setSound(String sound) {
        this.sound = sound;
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
        return Objects.hash(id, dictionary, hash, original, translation, photo, sound, lastGuessDate);
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
                ", photo='" + photo + '\'' +
                ", sound='" + sound + '\'' +
                ", comment='" + comment + '\'' +
                ", lastGuessDate=" + lastGuessDate +
                '}';
    }
}
