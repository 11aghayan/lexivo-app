package com.lexivo.schema;

public class Word extends LanguageElement {
    private WordType type;
    private Gender gender;
    private String past1;
    private String past2;
    private String plural;

    public Word(
            Dictionary dictionary,
            WordType type,
            Gender gender,
            Text original,
            String plural,
            String past1,
            String past2,
            Text translation,
            String comment
    ) {
        super(dictionary, original, translation, comment);
        this.type = type;
        this.gender = gender;
        this.plural = plural;
        this.past1 = past1;
        this.past2 = past2;
    }

    public WordType getType() {
        return type;
    }

    public void setType(WordType type) {
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