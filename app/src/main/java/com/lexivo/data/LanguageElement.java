package com.lexivo.data;

import androidx.annotation.NonNull;

import com.lexivo.util.HashUtil;

import java.util.Objects;
import java.util.UUID;

public abstract class LanguageElement implements ObjectContainingId {
    private final String id;
    private final String dictionaryId;
    private String hash;
    private Text original;
    private Text translation;
    private String pictureId;
    private String soundId;
    private int lastGuessDate;

    public LanguageElement(String dictionaryId, Text original, Text translation, String pictureId, String soundId) {
        this.id = UUID.randomUUID().toString();
        this.dictionaryId = dictionaryId;
        this.original = original;
        this.translation = translation;
        this.pictureId = pictureId;
        this.soundId = soundId;
        this.hash = HashUtil.generateMd5HashFromString(original.getValue() + translation.getValue());
    }

    public LanguageElement(String dictionaryId, Text original, Text translation) {
        this.id = UUID.randomUUID().toString();
        this.dictionaryId = dictionaryId;
        this.original = original;
        this.translation = translation;
        this.hash = HashUtil.generateMd5HashFromString(original.getValue() + translation.getValue());
    }

    public String getId() {
        return id;
    }

    public String getDictionaryId() {
        return dictionaryId;
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

    public String getPictureId() {
        return pictureId;
    }

    public String getSoundId() {
        return soundId;
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

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

    public void setSoundId(String soundId) {
        this.soundId = soundId;
    }

    public void setLastGuessDate(int lastGuessDate) {
        this.lastGuessDate = lastGuessDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LanguageElement that = (LanguageElement) o;
        return lastGuessDate == that.lastGuessDate && Objects.equals(id, that.id) && Objects.equals(dictionaryId, that.dictionaryId) && Objects.equals(hash, that.hash) && Objects.equals(original, that.original) && Objects.equals(translation, that.translation) && Objects.equals(pictureId, that.pictureId) && Objects.equals(soundId, that.soundId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dictionaryId, hash, original, translation, pictureId, soundId, lastGuessDate);
    }

    @NonNull
    @Override
    public String toString() {
        return "LanguageElement{" +
                "id='" + id + '\'' +
                ", dictionaryId='" + dictionaryId + '\'' +
                ", hash='" + hash + '\'' +
                ", original='" + original + '\'' +
                ", translation='" + translation + '\'' +
                ", pictureId='" + pictureId + '\'' +
                ", soundId='" + soundId + '\'' +
                ", lastGuessDate=" + lastGuessDate +
                '}';
    }
}
