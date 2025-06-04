package com.lexivo.data;

import java.util.List;

public class Word extends LanguageElement {
    private List<WordType> type;
    private Gender gender;
    private String past1;
    private String past2;
    private String plural;

    public Word(
            Dictionary dictionary,
            Text original,
            Text translation,
            String picture,
            String sound,
            String comment,
            List<WordType> type,
            Gender gender,
            String plural
    ) {
        super(dictionary, original, translation, picture, sound, comment);
        this.type = type;
        this.gender = gender;
        this.plural = plural;
    }

    public Word(Dictionary dictionary, Text original, Text translation, String photo, String sound, String comment, List<WordType> type, String past1, String past2) {
        super(dictionary, original, translation, photo, sound, comment);
        this.type = type;
        this.past1 = past1;
        this.past2 = past2;
    }

    public Word(Dictionary dictionary, Text original, Text translation, String photo, String sound, String comment, List<WordType> type) {
        super(dictionary, original, translation, photo, sound, comment);
        this.type = type;
    }

    public List<WordType> getType() {
        return type;
    }

    public void setType(List<WordType> type) {
        this.type = type;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPast1() {
        return past1;
    }

    public void setPast1(String past1) {
        this.past1 = past1;
    }

    public String getPast2() {
        return past2;
    }

    public void setPast2(String past2) {
        this.past2 = past2;
    }

    public String getPlural() {
        return plural;
    }

    public void setPlural(String plural) {
        this.plural = plural;
    }
}