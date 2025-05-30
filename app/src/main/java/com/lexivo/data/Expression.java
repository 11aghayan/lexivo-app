package com.lexivo.data;

import java.util.ArrayList;
import java.util.List;

public class Expression extends LanguageElement {
    private List<Integer> nounIndexes;
    private Tense tense;
    private boolean isQuestion;

    public Expression(String dictionaryId, Text original, Text translation, String pictureId, String soundId, List<Integer> nounIndexes, Tense tense, boolean isQuestion) {
        super(dictionaryId, original, translation, pictureId, soundId);
        this.nounIndexes = nounIndexes;
        this.tense = tense;
        this.isQuestion = isQuestion;
    }

    public Expression(String dictionaryId, Text original, Text translation, List<Integer> nounIndexes, Tense tense, boolean isQuestion) {
        super(dictionaryId, original, translation);
        this.nounIndexes = nounIndexes;
        this.tense = tense;
        this.isQuestion = isQuestion;
    }

    public List<Integer> getNounIndexes() {
        return nounIndexes;
    }

    public void setNounIndexes(List<Integer> nounIndexes) {
        this.nounIndexes = nounIndexes;
    }

    public Tense getTense() {
        return tense;
    }

    public void setTense(Tense tense) {
        this.tense = tense;
    }

    public boolean isQuestion() {
        return isQuestion;
    }

    public void setQuestion(boolean question) {
        isQuestion = question;
    }
}
