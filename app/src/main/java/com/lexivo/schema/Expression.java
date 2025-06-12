package com.lexivo.schema;

import java.util.List;

public class Expression extends LanguageElement {
    private List<Integer> nounIndexes;
    private Tense tense;
    private boolean isQuestion;

    public Expression(Dictionary dictionary, Text original, Text translation, List<Integer> nounIndexes, Tense tense, boolean isQuestion, String comment) {
        super(original, translation, comment);
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
